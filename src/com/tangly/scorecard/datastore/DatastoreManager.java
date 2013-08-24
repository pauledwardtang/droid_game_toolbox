package com.tangly.scorecard.datastore;

import android.content.*;

/**
 *
 */
public class DatastoreManager
{
	private static Datastore datastore;

	public static Datastore getDatastore(Context context)
	{
		if (datastore == null)
		{
			datastore = DatastoreFactory.createSQLiteDatastore(context);
		}
		return datastore;
	}
}
