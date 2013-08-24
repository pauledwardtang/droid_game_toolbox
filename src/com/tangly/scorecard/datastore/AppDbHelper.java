package com.tangly.scorecard.datastore;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;
import java.util.*;

public class AppDbHelper extends SQLiteOpenHelper
{
	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "GameBuddy.db";

	public AppDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onConfigure(SQLiteDatabase db)
	{
		// Enabling foreign keys and WAL mode cuz I lov WAL its de best
//		db.setForeignKeyConstraintsEnabled(true);
		//db.enableWriteAheadLogging();
	}

	public void onCreate(SQLiteDatabase db)
	{
//		db.beginTransaction();
		
		// Loop through all models and get their create entries
		List<String> createStatements = new ArrayList<String>();
		createStatements.addAll(PlayerDatabaseAdapter.getInstance().getCreateStatements());
		createStatements.addAll(GameSessionDatabaseAdapter.getInstance().getCreateStatements());
		for (String sql: createStatements)
		{
			// TODO REMOVE
			Log.d("SQLite", sql);
			db.execSQL(sql);
		}
//		db.endTransaction();
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.beginTransaction();

		// Loop through all models and get their create entries
		List<String> deleteStatements = new ArrayList<String>();
		deleteStatements.addAll(PlayerDatabaseAdapter.getInstance().getDeleteStatements());
		deleteStatements.addAll(GameSessionDatabaseAdapter.getInstance().getDeleteStatements());
		for (String sql: deleteStatements)
		{
			db.execSQL(sql);
		}
		db.endTransaction();
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onUpgrade(db, oldVersion, newVersion);
	}
}
