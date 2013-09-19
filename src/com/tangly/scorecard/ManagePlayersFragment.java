package com.tangly.scorecard;

import android.support.v4.app.*;
import android.app.Activity;
import android.os.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tangly.framework.dialogs.*;
import com.tangly.scorecard.datastore.DatastoreDefs;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;

public class ManagePlayersFragment extends StorableListViewFragment
{
	private static final String TAG = "PlayerManager";
	private EditCallback callback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.manage_model, null);

		Button addDiceBtn = (Button) view.findViewById(R.id.addBtn);
		addDiceBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1)
			{
				callback = new EditCallback();

				// Adding will show the fragment
				callback.addStorable();
			}
		});

		return view;
    }

	protected class EditCallback implements EditStorableCallback, NoticeDialogFragment.NoticeDialogListener
	{
		protected Storable storable;

		/**
		 * Creates an EditDiceDialogFragment without specifying any arguments in a bundle.
		 */
		public void addStorable()
		{
			storable = new Player();
			NoticeDialogFragment newFragment = new EditTextDialogFragment();
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "diceEdit");
		}

		public void editStorable(long id)
		{
			// Get reference to the storable that will be used
			storable = getItemById(id);

			NoticeDialogFragment newFragment = new EditTextDialogFragment(storable.getDisplayName());
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "playerEdit");
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
				// New storable if the ID is an invalid ID
				Log.d(TAG, "Changes detected, updating...");
				boolean newStorable = (this.storable.getId() == DatastoreDefs.INVALID_ID);
				this.storable.setDisplayName(fragment.getTextFieldString());
				this.storable.save(getDatastore());

				// Update adapter since there has been a change
				if (newStorable)
				{
					getAdapter().add(this.storable);
				}
				else
				{
					getAdapter().notifyDataSetChanged();
				}
			}
		}

		/**
		 * Do nothing if the user clicked cancel
		 * @see NoticeDialogFragment.NoticeDialogListener.onDialogNegativeClick
		 */	
		public void onDialogNegativeClick(DialogFragment dialog) {}
	}

	/**
	 * @see StorableListViewFragment.getEditStorableOnClickListener
	 */
	protected EditStorableOnClickListener getEditStorableOnClickListener()
	{
		return null;
	}

	/**
	 * @see StorableListViewFragment.getEditStorableCallback
	 */
	@Override
	protected EditStorableCallback getEditStorableCallback()
	{
		return new EditCallback();
	}

	/**
	 * @see StorableListViewFragment.getStorableType
	 */
	@Override
	protected Class<? extends Storable> getStorableType() { return Player.class; }

	/**
	 * @see StorableListViewFragment.getEditStorableActivity
	 */
	@Override
	protected Class<? extends Activity> getEditStorableActivity() { return null; }

	/**
	 * @see StorableListViewFragment.getTextViewResourceId
	 */
	@Override
	protected int getTextViewResourceId() { return android.R.id.list;}
}
