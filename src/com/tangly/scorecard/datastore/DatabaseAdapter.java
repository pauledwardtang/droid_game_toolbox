package com.tangly.scorecard.datastore;

/**
 * Interface that defines the most basic of commands
 */
import android.database.sqlite.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public interface DatabaseAdapter
{
    public List<String> getCreateStatements();

    public List<String> getDeleteStatements();

    public <T extends Storable> T retrieve(SQLiteDatabase db, long id);

    public <T extends Storable> List<T> retrieveAll(SQLiteDatabase db);

    public long insert(SQLiteDatabase db, Storable s);

    public void update(SQLiteDatabase db, Storable s);

    public long getCount(SQLiteDatabase db);
}
