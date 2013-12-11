package com.tangly.scorecard.datastore.adapters;

import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import com.tangly.scorecard.datastore.cursors.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;
import android.util.*;

/**
 * Factory class for creating Players
 */
public class PlayerDatabaseAdapter extends DefaultDatabaseAdapter implements DatabaseAdapter
{
    // Workaround because we can't have public static interface functions.
    // Need to get the create statements using singletons :/
    private static PlayerDatabaseAdapter instance;

    // TODO PUT IN COMMON LOCATION
    // Cursor factories
    public static CursorFactory playerCursorFactory;
    private static SQLiteStatement playerInsertStatement;
    private static SQLiteStatement playerUpdateStatement;

    public static PlayerDatabaseAdapter getInstance()
    {
        if (instance == null)
        {
            instance = new PlayerDatabaseAdapter();
        }
        return instance;
    }

    private PlayerDatabaseAdapter()
    {
        super("Player");
    }

    public List<String> getCreateStatements()
    {
        List<String> createStatements = new ArrayList<String>();
        createStatements.add(
                "CREATE TABLE Player(" +
                        "    id               INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    name             VARCHAR(30) NOT NULL," +
                        "    score            INTEGER);"
                );
        return createStatements;
    }

    public List<String> getDeleteStatements()
    {
        List<String> deleteStatements = new ArrayList<String>();
        deleteStatements.add("DROP TABLE IF EXISTS Player");
        return deleteStatements;
    }

    public Storable retrieve(SQLiteDatabase db, long id)
    {
        // Lazy initialize the player cursor factory
        if (playerCursorFactory == null)
        {
            playerCursorFactory = new PlayerCursor.Factory();
        }

        String query = PlayerCursor.getPlayerSelectQuery() + "WHERE ID = " + id;
        Log.d(PlayerDatabaseAdapter.class.getName(), query);
        PlayerCursor pc =
                (PlayerCursor) db.rawQueryWithFactory(playerCursorFactory,
                        query,
                        null,
                        null);
        pc.moveToFirst();
        return pc.getPlayer();
    }

    public List<Storable> retrieveAll(SQLiteDatabase db)
    {
        ArrayList<Storable> players = new ArrayList<Storable>();

        // Lazy initialize the cursor factory
        if (playerCursorFactory == null)
        {
            playerCursorFactory = new PlayerCursor.Factory();
        }

        String query = PlayerCursor.getPlayerSelectQuery();
        PlayerCursor cursor =
                (PlayerCursor) db.rawQueryWithFactory(playerCursorFactory,
                        query,
                        null,
                        null);
        // If the table isn't empty create the game sessions
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                Log.d("SQLite", "Retrieve all: Found Player ID:" + cursor.getPlayerId());
                players.add(cursor.getPlayer());
            } while (cursor.moveToNext());
        }
        return players;
    }

    public long insert(SQLiteDatabase db, Storable s)
    {
        if (playerInsertStatement == null)
        {
            playerInsertStatement = db
                    .compileStatement("INSERT INTO Player(name, score) values (:1, :2)");
        }
        playerInsertStatement.bindString(1, s.getDisplayName());
        playerInsertStatement.bindLong(2, ((Player) s).getScore());
        return playerInsertStatement.executeInsert();
    }

    public void update(SQLiteDatabase db, Storable s)
    {
        if (playerUpdateStatement == null)
        {
            playerUpdateStatement = db
                    .compileStatement("UPDATE Player SET name=:1, score=:2 WHERE id=:3");
        }
        playerUpdateStatement.bindString(1, s.getDisplayName());
        playerUpdateStatement.bindLong(2, ((Player) s).getScore());
        playerUpdateStatement.bindLong(3, s.getId());
        playerUpdateStatement.executeUpdateDelete();
    }
}
