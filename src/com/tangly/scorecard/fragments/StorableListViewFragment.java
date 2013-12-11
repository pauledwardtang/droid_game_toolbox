package com.tangly.scorecard.fragments;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.support.v4.app.ListFragment;
import android.view.*;

import com.tangly.scorecard.DatastoreRetrieveTask;
import com.tangly.scorecard.EditStorableCallback;
import com.tangly.scorecard.EditStorableOnClickListener;
import com.tangly.scorecard.StorableArrayAdapter;
import com.tangly.scorecard.DatastoreRetrieveTask.PostExecuteListener;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.storage.*;

import java.util.*;

/**
 * Displays a list of Storables with an edit/remove button. Subclasses should
 * implement callbacks for binding to the edit button as well as the add button.
 * 
 * @author Paul Tang
 */
public abstract class StorableListViewFragment<T extends Storable> extends
        ListFragment implements DatastoreRetrieveTask.PostExecuteListener
{
    private StorableArrayAdapter<T> adapter;
    private Datastore datastore;
    private List<Storable> items = new ArrayList<Storable>();
    private Class<T> clazz;

    protected StorableListViewFragment(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public List<Storable> getItems()
    {
        return this.items;
    }

    public Storable getItemById(Long id)
    {
        Storable retVal = null;
        for (Storable s : this.items)
        {
            if (id.longValue() == s.getId())
            {
                retVal = s;
                break;
            }
        }
        return retVal;
    }

    /** Called when the activity is started. */
    @Override
    public void onStart()
    {
        super.onStart();
        this.init(null, null, null);
    }

    /**
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected void init(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Set the datastore first in case any of the templated functions need
        // to use the datastore
        Context ctx = this.getActivity().getApplicationContext();
        this.datastore = DatastoreManager.getDatastore(ctx);

        LayoutInflater theInflater = inflater;
        if (theInflater == null)
        {
            theInflater = getLayoutInflater(savedInstanceState);
        }

        // Hook up adapter
        this.adapter = new StorableArrayAdapter(ctx,
                this.getTextViewResourceId(), this.items, theInflater,
                this.getEditStorableOnClickListener(),
                this.getEditStorableCallback());

        this.setListAdapter(this.adapter);

        // Populate the adapter
        new DatastoreRetrieveTask(this.datastore, this.clazz, this).execute();
    }

    public void onPostExecute(Collection<Storable> results)
    {
        this.adapter.clear();
        this.adapter.addAll((Collection<T>) results);
        this.adapter.notifyDataSetChanged();
    }

    // Should only be used for unit testing
    protected void setDatastore(Datastore datastore)
    {
        this.datastore = datastore;
    }

    /**
     * @return A datastore instance
     */
    protected Datastore getDatastore()
    {
        return this.datastore;
    }

    /**
     * @return The adapter used by this activity.
     */
    public StorableArrayAdapter<T> getAdapter()
    {
        return this.adapter;
    }

    /**
     * Gets the storable on click listener. A return value of null indicates
     * that a default on click listener will be created using the callback.
     */
    protected EditStorableOnClickListener getEditStorableOnClickListener()
    {
        return null;
    };

    /**
     * Callback to be used if no onclick listener for the edit button is
     * defined. If this is not defined then the edit feature will be disabled
     * and the edit button will be hidden.
     */
    protected EditStorableCallback getEditStorableCallback()
    {
        return null;
    };

    /**
     * Gets the Activity that will be launched when the Storable item's edit
     * button is clicked
     */
    protected Class<? extends Activity> getEditStorableActivity()
    {
        return null;
    };

    /**
     * Gets the resource ID that will be used to inflate the ArrayAdapter.
     * Override to use a different resource
     */
    protected int getTextViewResourceId()
    {
        return android.R.id.list;
    }
}
