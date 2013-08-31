package com.tangly.scorecard;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public abstract class StorableListViewActivity extends Activity
	implements DatastoreRetrieveTask.PostExecuteListener
{
	private StorableArrayAdapter adapter;
	private Datastore datastore;
	private ListView listView;
	private List<Storable> items = new ArrayList<Storable>();

	public List<Storable> getItems()
	{
		return this.items;
	}

	public Storable getItemById(Long id)
	{
		Storable retVal = null;
		for (Storable s : this.items)
		{
			if (id.longValue() == s.getId())
			{
				retVal = s;
				break;
			}
		}
		return retVal;
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
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
		// Set the datastore first in case any of the templated functions need to use the datastore
		this.datastore = DatastoreManager.getDatastore(getApplicationContext());

		// Hook up adapters
		this.adapter = 
			new StorableArrayAdapter(getApplicationContext(),
									 this.getTextViewResourceId(),
									 this.items,
									 (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
									 this.getEditStorableOnClickListener(),
									 this.getEditStorableCallback());
		this.listView = (ListView) findViewById(android.R.id.list);
		this.listView.setAdapter(this.adapter);

		// Populate the adapter
		new DatastoreRetrieveTask(this.datastore, this.getStorableType(), this).execute();
	}

	public void onPostExecute(Collection<Storable> results)
	{
		this.items.clear();
		this.items.addAll(results);
		this.adapter.notifyDataSetChanged();
	}

	// Should only be used for unit testing
	protected void setDatastore(Datastore datastore) { this.datastore = datastore; }

	/**
	 * @return A datastore instance
	 */
	protected Datastore getDatastore() { return this.datastore; }

	/**
	 * @return The adapter used by this activity.
	 */
	public StorableArrayAdapter getAdapter() { return this.adapter; }

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
