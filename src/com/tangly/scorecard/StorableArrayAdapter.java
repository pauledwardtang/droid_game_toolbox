package com.tangly.scorecard;

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public class StorableArrayAdapter<T extends Storable> extends ArrayAdapter<T>
{
    private OnClickListener onClickListener;
    private List<T> items;
    private LayoutInflater inflater;
    private EditStorableOnClickListener editStorableOnClickListener;
    private EditStorableCallback editStorableCallback;

    /**
     * Create an ArrayAdapter for Storables.
     * 
     * @param context
     *            The Context
     * @param textViewResourceId
     *            The resource ID
     * @param items
     *            The list for Storable items
     * @param inflater
     *            LayoutInflater to inflate the view
     * @param editListener
     *            The listener to bind to each edit button. If not specified,
     *            the callback will be used to create a listener that creates a
     *            new EditActivity
     * @param callback
     *            Callback function that will be used if no listener is defined
     */
    public StorableArrayAdapter(Context context,
            int textViewResourceId,
            List<T> items,
            LayoutInflater inflater,
            EditStorableOnClickListener editListener,
            EditStorableCallback callback)
    {
        this(context, textViewResourceId, items);
        this.inflater = inflater;
        this.editStorableOnClickListener = editListener;

        if (this.editStorableOnClickListener == null)
        {
            this.editStorableCallback = callback;
        }
    }

    private StorableArrayAdapter(Context context, int textViewResourceId, List<T> items)
    {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.onClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            // LayoutInflater vi = (LayoutInflater)
            // getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = this.inflater.inflate(R.layout.editable_row_item, null);

            // Set listeners for the parent view, if the listener has been set
            if (this.onClickListener != null)
            {
                v.setOnClickListener(this.onClickListener);
            }
        }

        Storable item = items.get(position);
        if (item != null)
        {
            // Set name
            TextView et = (TextView) v.findViewById(R.id.etTitle);
            if (et != null)
            {
                et.setText(item.getDisplayName());
            }

            Button btnEdit = (Button) v.findViewById(R.id.btnEdit);

            // Set onClick listener for edit button
            if (this.editStorableOnClickListener != null)
            {
                btnEdit.setOnClickListener(this.editStorableOnClickListener);
            }
            else if (this.editStorableCallback != null)
            {
                OnClickListener listener = new EditStorableOnClickListener(item.getId(),
                        this.editStorableCallback);
                btnEdit.setOnClickListener(listener);
            }
            else
            {
                // Hide this button if there's no listener to attach!
                btnEdit.setVisibility(View.INVISIBLE);
            }
        }

        return v;
    }
}
