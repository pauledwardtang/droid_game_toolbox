package com.tangly.scorecard;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tangly.framework.dialogs.EditTextDialogFragment;
import com.tangly.framework.dialogs.NoticeDialogFragment;
import com.tangly.scorecard.datastore.DatastoreDefs;
import com.tangly.scorecard.model.Player;
import com.tangly.scorecard.storage.Storable;

public class ManagePlayersFragment extends StorableListViewFragment<Player>
{
    private static final String TAG = "PlayerManager";
    private EditCallback callback;

    public ManagePlayersFragment()
    {
        super(Player.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.manage_model, null);

        Button addDiceBtn = (Button) view.findViewById(R.id.addBtn);
        addDiceBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View p1)
            {
                callback = new EditCallback();

                // Adding will show the fragment
                callback.addStorable();
            }
        });

        return view;
    }

    protected class EditCallback implements EditStorableCallback, NoticeDialogFragment.NoticeDialogListener
    {
        protected Storable storable;

        /**
         * Creates an EditDiceDialogFragment without specifying any arguments in
         * a bundle.
         */
        public void addStorable()
        {
            storable = new Player();
            NoticeDialogFragment newFragment = new EditTextDialogFragment();
            newFragment.setMListener(EditCallback.this);
            newFragment.show(getFragmentManager(), "diceEdit");
        }

        public void editStorable(long id)
        {
            // Get reference to the storable that will be used
            storable = getItemById(id);

            NoticeDialogFragment newFragment = new EditTextDialogFragment(
                    storable.getDisplayName());
            newFragment.setMListener(EditCallback.this);
            newFragment.show(getFragmentManager(), "playerEdit");
        }

        /**
         * @see NoticeDialogFragment.NoticeDialogListener.onDialogPositiveClick
         */
        public void onDialogPositiveClick(DialogFragment dialog)
        {
            EditTextDialogFragment fragment = (EditTextDialogFragment) dialog;

            // Update the underlying model if the name has changed
            if (this.storable.getDisplayName().compareTo(
                    fragment.getTextFieldString()) != 0)
            {
                // New storable if the ID is an invalid ID
                Log.d(TAG, "Changes detected, updating...");
                boolean newStorable = (this.storable.getId() == DatastoreDefs.INVALID_ID);
                this.storable.setDisplayName(fragment.getTextFieldString());
                this.storable.save(getDatastore());

                // Update adapter since there has been a change
                if (newStorable)
                {
                    getAdapter().add((Player) this.storable);
                } else
                {
                    getAdapter().notifyDataSetChanged();
                }
            }
        }

        /**
         * Do nothing if the user clicked cancel
         * 
         * @see NoticeDialogFragment.NoticeDialogListener.onDialogNegativeClick
         */
        public void onDialogNegativeClick(DialogFragment dialog)
        {
        }
    }

    /**
     * @see StorableListViewFragment.getEditStorableCallback
     */
    @Override
    protected EditStorableCallback getEditStorableCallback()
    {
        return new EditCallback();
    }
}
