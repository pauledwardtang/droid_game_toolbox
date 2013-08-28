package com.tangly.scorecard.datastore.cursors;

import com.tangly.scorecard.model.Player;
import android.database.sqlite.*;
import android.database.*;

public class PlayerCursor extends SQLiteCursor
{
	private static final String BASE_QUERY = "SELECT * FROM Player ";
	
	private PlayerCursor(SQLiteDatabase db,
						 SQLiteCursorDriver driver,
						 String editTable,
						 SQLiteQuery query)
	{
		super(db, driver, editTable, query);
	}

	/**
	 * @return the query string
	 */
	public static String getPlayerSelectQuery()
	{
		return BASE_QUERY;
	}

	public long getPlayerId() { return getLong(getColumnIndexOrThrow("id"));}
	public String getPlayerName() { return getString(getColumnIndexOrThrow("name")); }
	public long getPlayerScore() { return getLong(getColumnIndexOrThrow("score")); }

	public Player getPlayer()
	{
		Player player = new Player(getPlayerName(), (int) getPlayerScore());
		player.setId(this.getPlayerId());
		return player;
	}
	
	public static class Factory implements SQLiteDatabase.CursorFactory
	{
		@Override
		public Cursor newCursor(SQLiteDatabase db,
								SQLiteCursorDriver driver,
								String editTable,
								SQLiteQuery query)
		{
			return new PlayerCursor(db, driver, editTable, query);
		}
	}
}

