package com.tangly.scorecard;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.tangly.framework.dialogs.*;
import com.tangly.scorecard.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;

// TODO make yet another base class and specify the class name through the
// intent!
public class ManageDiceActivity extends StorableListViewActivity
{
	private static final String TAG = "DiceManager";

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_model);

		Button addDiceBtn = (Button) findViewById(R.id.addBtn);
		addDiceBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1)
			{
				// TODO implement add dice fragment
//				startActivity(new Intent(getApplicationContext(), ManageDiceActivity.class));
			}
		});
    }

	protected class EditCallback implements EditStorableCallback, NoticeDialogFragment.NoticeDialogListener
	{
		protected Storable storable;
		public void editStorable(long id)
		{
			// Get refernce to the storable that will be used
			storable = getItemById(id);

			NoticeDialogFragment newFragment = new EditTextDialogFragment(storable.getDisplayName());
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "diceEdit");
		}

		/**
		 * @see NoticeDialogFragment.NoticeDialogListener.onDialogPositiveClick
		 */	
		public void onDialogPositiveClick(DialogFragment dialog)
		{
			EditTextDialogFragment fragment = (EditTextDialogFragment) dialog;

			// Update the underlying model if the name has changed
			if (this.storable.getDisplayName().compareTo(fragment.getTextFieldString()) != 0)
			{
				Log.d(TAG, "Changes detected, updating...");
				this.storable.setDisplayName(fragment.getTextFieldString());
				this.storable.save(getDatastore());

				// Update adapter since there has been a change
				getAdapter().notifyDataSetChanged();
			}
		}

		/**
		 * Do nothing if the user clicked cancel
		 * @see NoticeDialogFragment.NoticeDialogListener.onDialogNegativeClick
		 */	
		public void onDialogNegativeClick(DialogFragment dialog) {}
	}

	/**
	 * @see StorableListViewActivity.getEditStorableOnClickListener
	 */
	protected EditStorableOnClickListener getEditStorableOnClickListener()
	{
		return null;
	}

	/**
	 * @see StorableListViewActivity.getEditStorableCallback
	 */
	@Override
	protected EditStorableCallback getEditStorableCallback()
	{
		return new EditCallback();
	}

	/**
	 * @see StorableListViewActivity.getStorableType
	 */
	@Override
	protected Class getStorableType() { return Dice.class; }

	/**
	 * @see StorableListViewActivity.getEditStorableActivity
	 */
	@Override
	protected Class getEditStorableActivity() { return null; }

	/**
	 * @see StorableListViewActivity.getTextViewResourceId
	 */
	@Override
	protected int getTextViewResourceId() { return android.R.id.list;}
}
