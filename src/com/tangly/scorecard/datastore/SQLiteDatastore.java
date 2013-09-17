package com.tangly.scorecard.datastore;

import java.util.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import android.util.*;

import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import com.tangly.scorecard.datastore.cursors.*;

/**
 * SQLite implementation of datastore
 */
public class SQLiteDatastore extends SimpleDatastore
{
	private SQLiteOpenHelper helper;
	private SQLiteDatabase db;
	private Map<String, DatabaseAdapter> classAdapterMap;
	private static String TAG = "Datastore";

	public SQLiteDatastore(SQLiteOpenHelper h)
	{
		super();
		helper = h;
		db = h.getWritableDatabase();
		
		this.classAdapterMap = new HashMap<String, DatabaseAdapter>();
		this.classAdapterMap.put(Player.class.getName(), PlayerDatabaseAdapter.getInstance());
		this.classAdapterMap.put(GameSession.class.getName(), GameSessionDatabaseAdapter.getInstance());
		this.classAdapterMap.put(Dice.class.getName(), DiceDatabaseAdapter.getInstance());
	}
		
	@Override
	public long store(Storable s)
	{
		long retVal = s.getId();
		Class type = s.getClass();

		if (s instanceof GameSession)
		{
			// Update the player IDs first so we can associate them with the GameSession
			for ( Player player : ((GameSession) s).getPlayers())
			{
				// Will either create a new player or update existing players
				player.setId(this.store(player));
			}

			if (s.getId() == DatastoreDefs.INVALID_ID)
			{
				// Return the new ID!
				retVal = this.getDbAdapter(type).insert(this.db, s);
			}
			else
			{
				this.getDbAdapter(type).update(this.db, s);
			}
			for ( Player player : ((GameSession) s).getPlayers())
			{
				// Will either create a new player or update existing players
				this.store(player);
				Log.d("SQLite", "Stored/updated Player:" + player.toString());
			}

			// Cache off the object
			this.add(s);
		}
		else
		{
			if (s.getId() == DatastoreDefs.INVALID_ID)
			{
				// Return the new ID!
				retVal = this.getDbAdapter(type).insert(this.db, s);
			}
			else
			{
				this.getDbAdapter(type).update(this.db, s);
			}
		}

		return retVal;
	}

	@Override
	public void delete(Storable s)
	{
		// Delete it...
		s.delete(this);
		
 		// Remove internally
		if (this.has(s))
		{
			this.remove(s);
		}
	}

	@Override
	public Storable get(long id, Class type)
	{
		if (this.has(id))
		{
			return super.get(id, type);
		}
		else
		{
			// Retrieve from the database
			Storable retrievedStorable = this.getDbAdapter(type).retrieve(this.db,id);

			// Update internal list if the storable was 
			// retrieved from the database
			if (retrievedStorable != null)
			{
				this.add(retrievedStorable);
			}
			else
			{
				// TODO LOG THIS!
				Log.e(TAG, "Could not find storable: " + id);
			}
			return retrievedStorable;
		}
	}

	public Collection<Storable> getAll(Class type)
	{
		Log.d(TAG, "Retrieving all: " + type.getName());
		Collection<Storable> retrievedStorables;

		// Retrieve from the database
		// TODO is this safe? String comparisons with maps?
		retrievedStorables = this.getDbAdapter(type).retrieveAll(this.db);

		// Update internal list if the storable was 
		// retrieved from the database
		if (!retrievedStorables.isEmpty())
		{
			this.add(retrievedStorables);
		}

		return retrievedStorables;
	}
	
	// TODO THROWS
	private DatabaseAdapter getDbAdapter(Class type)
	{
		if (this.classAdapterMap.containsKey(type.getName()))
		{
			return this.classAdapterMap.get(type.getName());
		}
		else
		{
			return null;
		}
	}
}

