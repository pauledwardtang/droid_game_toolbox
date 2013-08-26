package com.tangly.scorecard;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public abstract class StorableListViewActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// Handle initialization here
		this.init();		
    }

	/** Called when the activity is started. */
    @Override
    public void onStart()
	{
		super.onStart();
		this.init();
	}

	protected void init()
	{
		// Hook up adapters
		StorableArrayAdapter gsAdapter
			= new StorableArrayAdapter(getApplicationContext(),
									   this.getTextViewResourceId(),
									   new ArrayList<Storable>(),
									   (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
									   this.getEditStorableOnClickListener(),
									   this.getEditStorableCallback());

		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(gsAdapter);

		// Populate the adapter
		Datastore ds = DatastoreManager.getDatastore(getApplicationContext());
		new DatastoreRetrieveTask(ds, this.getStorableType(), gsAdapter).execute();
	}

	/**
	 * Gets the storable on click listener. A return value of null indicates that a default on
	 * click listener will be created using the callback.
	 */
	protected abstract EditStorableOnClickListener getEditStorableOnClickListener();

	/**
	 * Callback to be used if no onclick listener for the edit button is defined. If this is not
	 * defined then the edit feature will be disabled and the edit button will be hidden.
	 */
	protected abstract EditStorableCallback getEditStorableCallback();

	/**
	 * Get the storable type that will be used to populate the view with row entries
	 */
	protected abstract Class getStorableType();
	
	/**
	 * Gets the Activity that will be launched when the Storable item's edit button is clicked
	 */
	protected abstract Class getEditStorableActivity();

	/**
	 * Gets the resource ID that will be used to inflate the ArrayAdapter
	 */	
	protected abstract int getTextViewResourceId();
}
