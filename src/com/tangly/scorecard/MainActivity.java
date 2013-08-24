package com.tangly.scorecard;

import android.app.*;
import android.content.*;
import android.database.sqlite.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.*;

import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import com.tangly.scorecard.datastore.*;
import android.util.*;

public class MainActivity extends Activity
{
	private List<GameSession> gameSessions;
	private GameSessionAdapter gsAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// Handle initialization here
		this.init();
		
    }

	/** Called when the activity is restarted. */
    @Override
    public void onRestart()
	{
		super.onRestart();
		Log.d("Lifecycle","Restarting main activity...");
	}

	/** Called when the activity is started. */
    @Override
    public void onStart()
	{
		super.onStart();
		Log.d("Lifecycle","Starting main activity...");
		this.init();
		
		// TODO THIS ISNT WORKING FOR SOME REASON!!
//		// Should be cached!
//		Datastore ds = DatastoreManager.getDatastore(getApplicationContext());
//		this.gameSessions = (List<GameSession>) ds.getAll(GameSession.class);
//
//		// TODO DEBUG code, remove
//		Log.d("DEBUG", "Resyncing game sessions and updating adapter");
//		for (GameSession gs : this.gameSessions)
//		{
//			Log.d("DEBUG",gs.toString());
//		}
//		this.gsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d("Lifecycle","Resuming main activity...");
	}
	
	protected void init()
	{
		// Hook up adapters
		Datastore ds = DatastoreManager.getDatastore(getApplicationContext());
		this.gameSessions = (List<GameSession>) ds.getAll(GameSession.class);
		this.gsAdapter = new GameSessionAdapter(this, R.layout.new_game_session, this.gameSessions);
		
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(this.gsAdapter);

		// Create and display the list of active games/sessions

		// Create and display inactive games/sessions (preceded by a divider of some kind)
		
	}

	public void editGameSessionOnClick(GameSession gs)
	{
		// Get the view's session info so we can add it to the intent
		Intent intent = new Intent(this, NewGameSessionActivity.class);
		
		Log.d("MainActivity", "Editing GS: " + gs.toString());
		// Populate intent with game session details
		intent.putExtra(IntentDefs.GS_ID, gs.getId());
		
		// Start the activity
		startGameSessionActivity(intent);
	}
	
	public void addNewGameSessionOnClick(View v)
	{
		startGameSessionActivity(null);
		Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Starts a new activity for creating a new game session. If intent is null, the default is shown
	 */
	public void startGameSessionActivity(Intent intent)
	{
		if (intent == null)
		{
			startActivity(new Intent(this, NewGameSessionActivity.class));
		}
		else
		{
			startActivity(intent);
		}
	}

	public class GameSessionAdapter extends ArrayAdapter<GameSession>  
	{
		private OnClickListener onClickListener = new GameSessionOnClickListener();
		private OnLongClickListener onLongClickListener = new GameSessionOnLongClickListener();

		public GameSessionAdapter(Context context, int textViewResourceId, List<GameSession> items)
		{
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View v = convertView;
			if (v == null) 
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.game_session_row_item, null);
			}
			GameSession o = gameSessions.get(position);
			if (o != null) 
			{
				TextView et = (TextView) v.findViewById(R.id.etGameName);
				if (et != null) 
				{
					et.setText(o.getName());
				}
			}
			v.setOnClickListener(this.onClickListener);
			v.setOnLongClickListener(this.onLongClickListener);
			return v;
		}

		protected class GameSessionOnLongClickListener implements OnLongClickListener
		{
			@Override
			public boolean onLongClick(View view)
			{
				editGameSessionOnClick(getGameSessionFromView(view));
				return true;
			}
		}

		protected class GameSessionOnClickListener implements OnClickListener
		{
			@Override
			public void onClick(View p1)
			{
				Toast.makeText(getApplicationContext(), "Game session clicked!", Toast.LENGTH_SHORT).show();
				// TODO, new activity that has the calculator and all that jazz
			}
		}
		
		private GameSession getGameSessionFromView(View v)
		{
			TextView tv = (TextView) v.findViewById(R.id.etGameName);
			String name = tv.getText().toString();
			Log.d("MainActivity:getGameSessionFromView", "Searching for GS with name of: " + name);
			for (GameSession gs : gameSessions)
			{
				Log.d("MainActivity:getGameSessionFromView", gs.toString());
				if (name.compareTo(gs.getName()) == 0)
				{
					return gs;
				}
			}
			return null;
		}
	}
		
//protected abstract class DefaultParcelable<T> implements Parcelable
//{
//	// TODO Figure out how to create a instance of a generic type (factory...)
//	// this is used to regenerate the object
//	public static final Parcelable.Creator<? extends ParcelableParser> CREATOR 
//		= new Parcelable.Creator<? extends ParcelableParser>()
//    {
//        public ParcelableParser createFromParcel(Parcel in)
//        {
//			return MainActivity.getDefaultParcelableParser(in);
//        }
//
//        public ParcelableParser[] newArray(int size)
//        {
//            return new ParcelableParser[size];
//        }
//    };
//
//	// Parcelable overrides
//	@Override
//	public int describeContents() 
//	{
//		return this.hashCode();
//	}
//}
	
/**
 * Abstract class for ensuring a class has a way to deserialize from a
 * parcel
 */
//protected class ParcelableParser
//{
// 	// Default constructor
//	public ParcelableParser(Parcel in)
//	{
//		parseParcel(in);
//	}
//
//	public void parseParcel(Parcel in)
//	{
//		throw new UnsupportedOperationException("Parcel parsing not supported");
//	}
//} 

}
