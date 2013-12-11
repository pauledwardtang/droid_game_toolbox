package com.tangly.scorecard;

import android.view.*;
import android.view.View.*;

/**
 * Click listener for editing storables
 */
public class EditStorableOnClickListener implements OnClickListener
{
    private long id;
    private EditStorableCallback callback;

    // Workaround since the edit button has no knowledge of the ID
    EditStorableOnClickListener(long id, EditStorableCallback callback)
    {
        this.id = id;
        this.callback = callback;
    }

    @Override
    public void onClick(View view)
    {
        this.callback.editStorable(this.id);
    }
}
