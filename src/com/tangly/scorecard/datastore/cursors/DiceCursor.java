package com.tangly.scorecard.datastore.cursors;

import com.tangly.scorecard.model.Dice;
import android.database.sqlite.*;
import android.database.*;

public class DiceCursor extends SQLiteCursor
{
    private static final String BASE_QUERY = "SELECT * FROM Dice ";

    private DiceCursor(SQLiteDatabase db,
            SQLiteCursorDriver driver,
            String editTable,
            SQLiteQuery query)
    {
        super(db, driver, editTable, query);
    }

    /**
     * @return the query string
     */
    public static String getSelectQuery()
    {
        return BASE_QUERY;
    }

    public long getId()
    {
        return getLong(getColumnIndexOrThrow("id"));
    }

    public String getName()
    {
        return getString(getColumnIndexOrThrow("name"));
    }

    public int getNumSides()
    {
        return getInt(getColumnIndexOrThrow("numSides"));
    }

    public Dice getDice()
    {
        // TODO implement... a dice has a set of results mapped to each die
        // number or a
        // range.... need to retrieve everything associated with the dice too
        Dice retVal = new Dice(this.getNumSides(), this.getName());
        retVal.setId(this.getId());
        return retVal;
    }

    public static class Factory implements SQLiteDatabase.CursorFactory
    {
        @Override
        public Cursor newCursor(SQLiteDatabase db,
                SQLiteCursorDriver driver,
                String editTable,
                SQLiteQuery query)
        {
            return new DiceCursor(db, driver, editTable, query);
        }
    }
}
