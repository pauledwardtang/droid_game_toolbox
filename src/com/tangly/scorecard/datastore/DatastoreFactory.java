package com.tangly.scorecard.datastore;


import android.content.*;
import android.database.sqlite.*;
import com.tangly.scorecard.*;

/**
 * Simple class for decoupling Datastore constructors
 */
public class DatastoreFactory 
{
	public static Datastore createSQLiteDatastore(Context context)
	{
//		Context context = ApplicationContextProvider.getContext();
		SQLiteOpenHelper helper = new AppDbHelper(context);
		return new SQLiteDatastore(helper);
	}
 }
