package com.tangly.scorecard.datastore.adapters;

import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import com.tangly.scorecard.datastore.cursors.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;
import android.util.*;

public class DiceDatabaseAdapter extends DefaultDatabaseAdapter
{
    // Workaround because we can't have public static interface functions.
    // Need to get the create statements using singletons :/
    private static DiceDatabaseAdapter instance;

    // TODO PUT IN COMMON LOCATION
    // Cursor factories
    public static CursorFactory diceCursorFactory;
    private static SQLiteStatement insertStatement;
    private static SQLiteStatement updateStatement;

    public static DiceDatabaseAdapter getInstance()
    {
        if (instance == null)
        {
            instance = new DiceDatabaseAdapter();
        }
        return instance;
    }

    private DiceDatabaseAdapter()
    {
        super("Dice");
    }

    public List<String> getCreateStatements()
    {
        List<String> createStatements = new ArrayList<String>();
        createStatements.add(
                "CREATE TABLE Dice(" +
                        "    id               INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    name             VARCHAR(30) NOT NULL, " +
                        "    numSides         INTEGER NOT NULL);"
                );
        return createStatements;
    }

    public List<String> getDeleteStatements()
    {
        List<String> deleteStatements = new ArrayList<String>();
        deleteStatements.add("DROP TABLE IF EXISTS " + this.getTableName());
        return deleteStatements;
    }

    public Storable retrieve(SQLiteDatabase db, long id)
    {
        // Lazy initialize the cursor factory
        if (diceCursorFactory == null)
        {
            diceCursorFactory = new DiceCursor.Factory();
        }

        String query = DiceCursor.getSelectQuery() + "WHERE ID = " + id;
        Log.d(DiceDatabaseAdapter.class.getName(), query);
        DiceCursor cursor =
                (DiceCursor) db.rawQueryWithFactory(diceCursorFactory,
                        query,
                        null,
                        null);
        cursor.moveToFirst();
        return cursor.getDice();
    }

    public List<Storable> retrieveAll(SQLiteDatabase db)
    {
        ArrayList<Storable> retVals = new ArrayList<Storable>();

        // Lazy initialize the cursor factory
        if (diceCursorFactory == null)
        {
            diceCursorFactory = new DiceCursor.Factory();
        }

        String query = DiceCursor.getSelectQuery();
        DiceCursor cursor =
                (DiceCursor) db.rawQueryWithFactory(diceCursorFactory,
                        query,
                        null,
                        null);
        // If the table isn't empty create the game sessions
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                Log.d("SQLite", "Retrieve all: Found Dice ID:" + cursor.getId());
                retVals.add(cursor.getDice());
            } while (cursor.moveToNext());
        }
        return retVals;
    }

    public long insert(SQLiteDatabase db, Storable s)
    {
        if (insertStatement == null)
        {
            insertStatement = db
                    .compileStatement("INSERT INTO Dice(name, numSides) values (:1, :2)");
        }
        insertStatement.bindString(1, s.getDisplayName());
        insertStatement.bindLong(2, ((Dice) s).getNumSides());
        return insertStatement.executeInsert();
    }

    public void update(SQLiteDatabase db, Storable s)
    {
        // TODO update the remaining fields e.g. number of sides
        if (updateStatement == null)
        {
            updateStatement = db
                    .compileStatement("UPDATE Dice SET name=:1, numSides=:2 WHERE id=:3");
        }
        updateStatement.bindString(1, s.getDisplayName());
        updateStatement.bindLong(2, ((Dice) s).getNumSides());
        updateStatement.bindLong(3, s.getId());
        updateStatement.executeUpdateDelete();
    }

}
