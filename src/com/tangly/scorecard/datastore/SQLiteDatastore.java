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

	public SQLiteDatastore(SQLiteOpenHelper h)
	{
		super();
		helper = h;
		db = h.getWritableDatabase();
	}
		
	@Override
	public long store(Storable s)
	{
		long retVal = s.getId();
		if (s instanceof Player)
		{
			if (s.getId() == DatastoreDefs.INVALID_ID)
			{
				// Return the new ID!
				retVal = PlayerDatabaseAdapter.getInstance().insert(this.db, (Player) s);
			}
			else
			{
				PlayerDatabaseAdapter.getInstance().update(this.db, (Player) s);
			}
		}
		
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
				retVal = GameSessionDatabaseAdapter.getInstance().insert(this.db, (GameSession) s);
			}
			else
			{
				GameSessionDatabaseAdapter.getInstance().update(this.db, (GameSession) s);
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
			Storable retrievedStorable = null;
			
			// TODO USE POLYMORPHISM
			if (type.getName().compareTo(Player.class.getName()) == 0)
			{
				retrievedStorable = PlayerDatabaseAdapter.getInstance().retrieve(this.db, id);
			}
			
			// TODO USE POLYMORPHISM
			if (type.getName().compareTo(GameSession.class.getName()) == 0)
			{
				retrievedStorable = GameSessionDatabaseAdapter.getInstance().retrieve(this.db, id);
			}

			// Update internal list if the storable was 
			// retrieved from the database
			if (retrievedStorable != null)
			{
				this.add(retrievedStorable);
			}
			else
			{
				// TODO LOG THIS!
			}
			return retrievedStorable;
		}
	}

	public Collection<Storable> getAll(Class type)
	{
		Log.d("Datastore", "Retrieving all: " + type.getName());
		Collection<Storable> retrievedStorables = new ArrayList<Storable>();
		
		// TODO This is a hack, should have another interface to use
		// to extract the SQL queries from??
		// Retrieve from the database
		
		if (type.getName().compareTo(Player.class.getName()) == 0)
		{
			// TODO we shouldn't be getting all players
		}
		else if (type.getName().compareTo(GameSession.class.getName()) == 0)
		{
			retrievedStorables = GameSessionDatabaseAdapter.getInstance().retrieveAll(this.db);
		}

		// Update internal list if the storable was 
		// retrieved from the database
		if (!retrievedStorables.isEmpty())
		{
			this.add(retrievedStorables);
		}

		return retrievedStorables;
	}
}

