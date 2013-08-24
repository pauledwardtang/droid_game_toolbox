package com.tangly.scorecard;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;

import com.tangly.scorecard.model.*;
import android.app.*;
import com.tangly.scorecard.datastore.*;

/**
 * The game information will just be Session name, and buttons to add players (should
 * just create a new textfield element for each player with a "player name" label).
 * Clicking ok or hitting back should save the information
 */
public class NewGameSessionActivity extends Activity
{
 	private EditText etSessionName;
	private List<LinearLayout> playerViews;
	private GameSession session;
	private Datastore datastore;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game_session);
		this.etSessionName = (EditText) findViewById(R.id.etGameName);

		playerViews = new ArrayList<LinearLayout>();
		playerViews.add((LinearLayout) findViewById(R.id.row1));
		playerViews.add((LinearLayout) findViewById(R.id.row2));
		playerViews.add((LinearLayout) findViewById(R.id.row3));
		playerViews.add((LinearLayout) findViewById(R.id.row4));
		playerViews.add((LinearLayout) findViewById(R.id.row5));
		
		// Handle initialization here
		this.init(savedInstanceState);
    }


	public class ClickMeButtonListener implements OnClickListener
	{
		public void onClick(View view)
		{
			Log.d("ClickMeButtonListener","Clicked button");
			Toast.makeText(getApplicationContext(), "Hello, neighbor!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * TODO: Figure out what needs to created by default. Probably need to pull some 
	 * persistant information from the internal database or some serialized info. 
	 */
	protected void init(Bundle savedInstanceState)
	{
		this.datastore = DatastoreManager.getDatastore(this.getApplicationContext());
		Intent intent = getIntent();
		if (intent != null)
		{
			long gs_id = intent.getLongExtra(IntentDefs.GS_ID, DatastoreDefs.INVALID_ID);
			if (gs_id != DatastoreDefs.INVALID_ID)
			{
				this.session = (GameSession) this.datastore.get(gs_id, GameSession.class);
			}
		}
		if (savedInstanceState != null)
		{
			// TODO extract information from bundle or from some other saved state...
		}

		if (this.session == null)
		{
			this.session = new GameSession();

			// Bind the default players to the gamesession
			List<Player> players = this.session.getPlayers();
			TextView tv;
			for (int i = 0; i < this.playerViews.size(); i++)
			{
				tv = (TextView) this.playerViews.get(i).getChildAt(0);
				players.add(new Player(tv.getText().toString(), 0));
			}
			this.session.setPlayers(players);
		}
		else
		{
			// Populate the fields from the retrieved storable object
			this.etSessionName.setText(this.session.getName());

			for (int i = 0; i < this.playerViews.size(); i++)
			{
				TextView tv = (TextView) this.playerViews.get(i).getChildAt(0);
				if (i < this.session.getPlayers().size())
				{
					// Get player text view and set it to the saved one
					tv.setText(this.session.getPlayers().get(i).getName());
				}
				else
				{
					// Hide the players that aren't added
					this.playerViews.get(i).setVisibility(View.INVISIBLE);
				}
			}
		}
	}
	
	/**
	 * Removes the row if the remove button is clicked
	 */
	public void removeOnClick(View v)
	{
		Toast.makeText(getApplicationContext(), "Remove button clicked...", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * The done button was clicked! Save all the information on the page
	 * @param v the View object that was clicked
	 */
	public void doneOnClick(View v)
	{
		// Save off information
		this.saveSession();
	}
	
	/**
	 *
	 */
	protected void saveSession()
	{
		Log.d("NewGS", "Saving " + this.getSession().getId());
		this.syncFields();

		Log.d("NewGameSession", "Synced fields... new Session name is " + this.getSession().getName());
		long newId = this.datastore.store(this.getSession());
		this.session = (GameSession) this.datastore.get(newId, GameSession.class);
		Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();

	}
	
	// Hack because I'm not using any array adapters	
	private void syncFields()
	{
		this.session.setName(this.etSessionName.getText().toString());
		this.updateSessionPlayersFromView();
	}

	// quick and dirty 
	private void updateSessionPlayersFromView()
	{
		Log.d("NewGameSessionActivity", "Updating players from the view");
		for (int i = 0; i < this.playerViews.size(); i++)
		{
			TextView tv = (TextView) this.playerViews.get(i).getChildAt(0);
			if (this.playerViews.get(i).getVisibility() == View.VISIBLE)
			{
				// Get player text view and set it to the saved one
				Log.d("NewGS", "Setting player name to " + 
					  tv.getText().toString() + " for " + this.session.getPlayers().get(i).toString());
				this.session.getPlayers().get(i).setName(tv.getText().toString());
			}
		}
	}
	

	public GameSession getSession() { return this.session; }
}	
