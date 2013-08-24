package com.tangly.scorecard.datastore.cursors;

import com.tangly.scorecard.model.GameSession;
import android.database.sqlite.*;
import android.database.*;

public class GameSessionCursor extends SQLiteCursor
{
	private static final String BASE_QUERY
		= "SELECT * FROM GameSession ";
	
	private static final String GS_SELECT_QUERY
		= "SELECT * FROM GameSession WHERE id = ";

	// Requires that the target game session ID is specified
	private static final String SESSION_PLAYERS_QUERY
		= "SELECT Player.id, Player.name, Player.score " + 
		  "FROM Player, GameSession_Player " +
		  "WHERE " +
		  "Player.id = GameSession_Player.player_id AND " +
		  "GameSession_Player.gs_id = ";

	private GameSessionCursor(SQLiteDatabase db,
						 	  SQLiteCursorDriver driver,
						      String editTable,
						      SQLiteQuery query)
	{
		super(db, driver, editTable, query);
	}

	/**
	 * @return the query string
	 */
	public static String getGameSessionSelectQuery()
	{
		return BASE_QUERY;
	}


	/**
	 * @return the query string
	 */
	public static String getGameSessionByIdSelectQuery()
	{
		return GS_SELECT_QUERY;
	}

	/**
	 * @return the SQL string that retrieves Players associated with a particular GameSession. GS ID MUST BE APPENDED
	 * and WILL return a player, not a gamesession
	 */
	public static String getGameSessionPlayersSelectQuery()
	{
		return SESSION_PLAYERS_QUERY;
	}

	public long getGameSessionId() { return getLong(getColumnIndexOrThrow("id"));}
	public String getGameSessionName() { return getString(getColumnIndexOrThrow("name")); }
	
	public GameSession getGameSession()
	{
		GameSession gameSession = new GameSession(this.getGameSessionName());
		if (this.getCount() > 0)
		{
			gameSession.setId(this.getGameSessionId());
		}
		return gameSession;
	}

	public static class Factory implements SQLiteDatabase.CursorFactory
	{
		@Override
		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable,SQLiteQuery query)
		{
			return new GameSessionCursor(db, driver, editTable, query);
		}
	}
}

