package com.tangly.scorecard.datastore.adapters;

import android.database.sqlite.*;

/**
 * Default abstract class
 */
public abstract class DefaultDatabaseAdapter implements DatabaseAdapter
{
    protected String tableName;
    protected SQLiteStatement countStatement;

    DefaultDatabaseAdapter(String tableName)
    {
        this.tableName = tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    /**
     * @see DatabaseAdapter.getCount
     */
    public long getCount(SQLiteDatabase db)
    {
        if (countStatement == null)
        {
            countStatement = db.compileStatement("SELECT COUNT(*) FROM " + this.tableName);
        }
        return countStatement.simpleQueryForLong();
    }
}
