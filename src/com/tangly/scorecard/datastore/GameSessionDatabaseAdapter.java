package com.tangly.scorecard.datastore;

import android.content.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import android.util.*;
import com.tangly.scorecard.datastore.cursors.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

/**
 * Data access class for retrieving GameSessions
 */
public class GameSessionDatabaseAdapter extends DefaultDatabaseAdapter implements DatabaseAdapter
{
 	// Workaround because we can't have public static interface functions.
	// Need to get the create statements using singletons :/
	private static GameSessionDatabaseAdapter instance;

	// Cursor factories
	public static CursorFactory gameSessionCursorFactory;
	
	private static SQLiteStatement updateGameSessionStatement;

	public static GameSessionDatabaseAdapter getInstance()
	{
		if (instance == null)
		{
			instance = new GameSessionDatabaseAdapter();
		}
		return instance;
	}

	private GameSessionDatabaseAdapter()
	{
		super("GameSession");
	}

	public List<String> getCreateStatements()
	{
		List<String> createStatements = new ArrayList<String>();
		createStatements.add(
			"CREATE TABLE GameSession(" +
			"    id               INTEGER PRIMARY KEY AUTOINCREMENT," +
			"    name             VARCHAR(30) NOT NULL);"
		);

		createStatements.add(
			"CREATE TABLE GameSession_Player( " + 
			"    gs_id     INTEGER FOREIGN_KEY REFERENCES GameSession(id), " +
			"    player_id INTEGER FOREIGN_KEY REFERENCES Player(id)" +
			");"
		);
		return createStatements;
	}

	public List<String> getDeleteStatements()
	{
		List<String> deleteStatements = new ArrayList<String>();
		deleteStatements.add("DROP TABLE IF EXISTS GameSession_Player;");
		deleteStatements.add("DROP TABLE IF EXISTS GameSession;");
		return deleteStatements;
	}

	@Override
	public long insert(SQLiteDatabase db, Storable s)
	{
		long gs_id = DatastoreDefs.INVALID_ID;
		GameSession gs = (GameSession) s;
		ContentValues values = new ContentValues();
		values.put("name", gs.getName());
		
		gs_id = db.insert(this.getTableName(), null, values);
		// Now associate the players with the game session
		for (Player p : gs.getPlayers())
		{
			ContentValues linkTableValues = new ContentValues();
			linkTableValues.put("gs_id", gs_id);
			linkTableValues.put("player_id", p.getId());
			db.insertWithOnConflict("GameSession_Player", null, linkTableValues, SQLiteDatabase.CONFLICT_REPLACE);
		}

		return gs_id;
	}

	@Override
	public void update(SQLiteDatabase db, Storable s)
	{
		if (updateGameSessionStatement == null)
		{
			updateGameSessionStatement = db.compileStatement("UPDATE GameSession SET name=:1 WHERE id=:2");
		}
		updateGameSessionStatement.bindString(1, ((GameSession) s).getName());
		updateGameSessionStatement.bindLong(2, s.getId());
		
		Log.d("SQLite", updateGameSessionStatement.toString());
		updateGameSessionStatement.executeUpdateDelete();
		// TODO This approach doesn't work for some reason. It fails to bind the name and ID
//		ContentValues values = new ContentValues();
//		values.put("name", ((GameSession) s).getName());
//		String[] args = { String.valueOf(s.getId()) };
//		db.update(this.getTableName(), values, "WHERE ID=?", args);
	}

	public Storable retrieve(SQLiteDatabase db, long id)
	{
		// Lazy initialize the player cursor factory
		if (gameSessionCursorFactory == null)
		{
			gameSessionCursorFactory = new GameSessionCursor.Factory();
		}

		// Lazy initialize the player cursor factory
		if (PlayerDatabaseAdapter.playerCursorFactory == null)
		{
			PlayerDatabaseAdapter.playerCursorFactory = new PlayerCursor.Factory();
		}

		String gsQuery = GameSessionCursor.getGameSessionByIdSelectQuery() + id;
		GameSessionCursor gsCursor =
			(GameSessionCursor) db.rawQueryWithFactory(gameSessionCursorFactory,
													   gsQuery,
													   null,
													   null);
		gsCursor.moveToFirst();
		GameSession gameSession = gsCursor.getGameSession();

		String gsPlayersQuery = GameSessionCursor.getGameSessionPlayersSelectQuery() + id;
		PlayerCursor pc =
			(PlayerCursor) db.rawQueryWithFactory(PlayerDatabaseAdapter.playerCursorFactory,
												  gsPlayersQuery,
												  null,
												  null);

		List<Player> gsPlayers = gameSession.getPlayers();
		pc.moveToFirst();
		do
		{
			gsPlayers.add(pc.getPlayer());
		}
		while (pc.moveToNext());
		
		Log.d("SQLite", "Query found "+ pc.getCount() + " players." +
			  "GameSession populated with " + gameSession.getPlayers().size() + " players");
		return gameSession;
	}
	
	public List<Storable> retrieveAll(SQLiteDatabase db)
	{
		ArrayList<Storable> gameSessions = new ArrayList<Storable>();
		// Lazy initialize the GS cursor factory
		if (gameSessionCursorFactory == null)
		{
			gameSessionCursorFactory = new GameSessionCursor.Factory();
		}
		
		String gsQuery = GameSessionCursor.getGameSessionSelectQuery();
		GameSessionCursor gsCursor =
			(GameSessionCursor) db.rawQueryWithFactory(gameSessionCursorFactory,
													   gsQuery,
													   null,
													   null);
		// If the table isn't empty create the game sessions
		if (gsCursor.getCount() > 0)
		{
			gsCursor.moveToFirst();
			do
			{
				Log.d("SQLite", "Retrieve all: Found GS ID:" + gsCursor.getGameSessionId());
				gameSessions.add(this.retrieve(db, gsCursor.getGameSessionId()));
			}
			while(gsCursor.moveToNext());
		}
		return gameSessions;
	}
	
}

