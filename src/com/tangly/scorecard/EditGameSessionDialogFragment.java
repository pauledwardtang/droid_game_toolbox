package com.tangly.scorecard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.tangly.framework.dialogs.NoticeDialogFragment;
import com.tangly.scorecard.datastore.Datastore;
import com.tangly.scorecard.datastore.DatastoreManager;
import com.tangly.scorecard.model.GameSession;
import com.tangly.scorecard.model.Player;
import com.tangly.scorecard.storage.Storable;

/**
 * The game information will just be Session name, and buttons to add players
 * (should just create a new textfield element for each player with a
 * "player name" label). Clicking ok or hitting back should save the information
 */
public class EditGameSessionDialogFragment extends NoticeDialogFragment implements
        DatastoreRetrieveTask.PostExecuteListener
{
    private static String DEFAULT_TITLE = "Game Session Editor";
    private String title = DEFAULT_TITLE;
    private EditText etSessionName;

    private ArrayAdapter<? extends Storable> spinnerAdapter;
    private Spinner addStorableSpinner;
    private GameSession session;
    private Datastore datastore;

    private StorableArrayAdapter<Player> adapter;
    private List<Player> items = new ArrayList<Player>();

    // Bundle strings
    public static String BUNDLE_GS_ID = "BUNDLE_GS_ID";
    public static String BUNDLE_GS_NAME = "BUNDLE_GS_NAME";
    public static String BUNDLE_GS_PLAYER_IDS = "BUNDLE_GS_PLAYER_IDS";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Context ctx = this.getActivity().getApplicationContext();
        this.datastore = DatastoreManager.getDatastore(ctx);
        this.session = new GameSession();

        // 2. Chain together various setter methods to set the dialog
        // characteristics
        LayoutInflater inflater = getActivity().getLayoutInflater();
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.new_game_session, null);
        this.etSessionName = (EditText) view.findViewById(R.id.etGameName);
        this.addStorableSpinner = (Spinner) view.findViewById(R.id.storableSpinner);
        ListView listView = (ListView) view.findViewById(android.R.id.list);

        // Populate the spinner in the background
        new DatastoreRetrieveTask(this.datastore, Player.class, new SpinnerPostExecuteListener())
                .execute();

        Button addPlyrBtn = (Button) view.findViewById(R.id.addPlyrBtn);
        addPlyrBtn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View p1)
            {
                adapter.add((Player) addStorableSpinner.getSelectedItem());
                adapter.notifyDataSetChanged();
                Log.v("DEBUG", ArrayUtils.toString(items));
            }
        });

        // Hook up adapter
        this.adapter = new StorableArrayAdapter<Player>(ctx, android.R.id.list, this.items,
                inflater, null, null);
        listView.setAdapter(this.adapter);

        // Populate the fields with the saved instance bundle or the args bundle
        this.populateFieldsFromSavedStateOrArgument(savedInstanceState, this.getArguments());

        builder.setTitle(this.title).setView(view)
                .setPositiveButton("Save", new PositiveButtonDialogOnClickListener())
                .setNegativeButton("Cancel", new NegativeButtonDialogOnClickListener());

        // 3. Get the AlertDialog from create()
        return builder.create();
    }

    /**
     * Removes the row if the remove button is clicked
     */
    public void removeOnClick(View v)
    {
        Toast.makeText(this.getActivity().getApplicationContext(), "Remove button clicked...",
                Toast.LENGTH_SHORT).show();
    }

    // Hack because I'm not using any array adapters
    private void syncFields()
    {
        this.session.setDisplayName(this.etSessionName.getText().toString());
        this.session.setPlayers(this.items);
    }

    /**
     * Populate fields from the saved instance state, or the arguments set just
     * after this object was constructed (in that order). Assumes the internal
     * views have been already populated.
     * 
     * @param savedInstance
     * @param args
     */
    private void populateFieldsFromSavedStateOrArgument(Bundle savedInstance, Bundle args)
    {
        if (savedInstance != null)
        {
            if (savedInstance.containsKey(BUNDLE_GS_ID))
            {
                this.session.setId(savedInstance.getLong(BUNDLE_GS_ID));
            }
            if (savedInstance.containsKey(BUNDLE_GS_NAME))
                this.etSessionName.setText(savedInstance.getString(BUNDLE_GS_NAME));
            if (savedInstance.containsKey(BUNDLE_GS_PLAYER_IDS))
                new DatastoreRetrieveTask(this.datastore, Player.class, this).execute(ArrayUtils
                        .toObject(savedInstance.getLongArray(BUNDLE_GS_PLAYER_IDS)));
        } else if (args != null)
        {
            if (args.containsKey(BUNDLE_GS_ID))
            {
                this.session.setId(args.getLong(BUNDLE_GS_ID));
            }
            if (args.containsKey(BUNDLE_GS_NAME))
                this.etSessionName.setText(args.getString(BUNDLE_GS_NAME));
            if (args.containsKey(BUNDLE_GS_PLAYER_IDS))
                new DatastoreRetrieveTask(this.datastore, Player.class, this).execute(ArrayUtils
                        .toObject(args.getLongArray(BUNDLE_GS_PLAYER_IDS)));
        }
        this.adapter.clear();
        this.adapter.addAll(this.session.getPlayers());
        this.adapter.notifyDataSetChanged();
    }

    public GameSession getSession()
    {
        this.syncFields();
        return this.session;
    }

    @Override
    public void onPostExecute(Collection<Storable> results)
    {
        // TODO do this better :)
        List<Player> players = new ArrayList<Player>();
        for (Storable plyr : results)
        {
            players.add((Player) plyr);
        }

        if (!players.isEmpty())
        {
            this.adapter.clear();
            this.adapter.addAll(players);
            this.adapter.notifyDataSetChanged();
        }
    }

    private class SpinnerPostExecuteListener implements DatastoreRetrieveTask.PostExecuteListener
    {
        @Override
        public void onPostExecute(Collection<Storable> results)
        {
            // Create an ArrayAdapter using the string array and a default
            // spinner layout
            // TODO do this better :)
            List<Player> players = new ArrayList<Player>();
            for (Storable plyr : results)
            {
                players.add((Player) plyr);
            }

            try
            {
                // Note that the text displayed is Player.toString()!!
                spinnerAdapter = new ArrayAdapter<Player>(getActivity().getApplicationContext(),
                        android.R.layout.simple_list_item_1, players);

                // Specify the layout to use when the list of choices appears
                spinnerAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                addStorableSpinner.setAdapter(spinnerAdapter);
            } catch (NullPointerException exc)
            {
                // It's possible the activity doesn't exist anymore, so ignore
            }
        }

    }
}
