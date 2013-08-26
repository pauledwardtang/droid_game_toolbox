package com.tangly.scorecard;

import android.os.*;
import android.util.*;
import android.widget.*;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.storage.*;
import java.lang.ref.*;
import java.util.*;

/**
 * AsyncTask for retrieving objects from a Datastore
 */
public class DatastoreRetrieveTask extends AsyncTask<String, Void, Collection<Storable>>
{
 	private Datastore dsInstance;
	private Class recordType;
	private ArrayAdapter<? extends Storable> arrayAdapter;

	/**
	 * @param ds The Datastore instance to use to retrieve results
	 * @param recordType The record type to retrieve
	 * @param results The reference to the collection that will be populated with the results of this Task
	 */
 	public DatastoreRetrieveTask(Datastore ds, Class recordType, ArrayAdapter<? extends Storable> adapter)
	{
		this.dsInstance = ds;
		this.recordType = recordType;
		this.arrayAdapter = adapter;
	}

	/** 
	 * The system calls this to perform work in a worker thread and
	 * delivers it the parameters given to AsyncTask.execute()
	 */
	@Override
	protected Collection<Storable> doInBackground(String... ids) 
	{
		Collection<Storable> retVal;
		if (ids.length == 0)
		{
			retVal = this.dsInstance.getAll(recordType);
		}
		else
		{
			retVal = new ArrayList<Storable>();
			for (String id : ids)
			{
				retVal.add(this.dsInstance.get(Long.valueOf(id), recordType));
			}
		}
		return retVal;
	}

	/**
	 * The system calls this to perform work in the UI thread and delivers
	 * the result from doInBackground()
	 */
	@Override
	protected void onPostExecute(Collection<Storable> results)
	{
		this.arrayAdapter.addAll(results);
	}
}
