package com.tangly.scorecard;

import android.support.v4.app.*;
import android.app.Activity;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.tangly.framework.dialogs.*;
import com.tangly.scorecard.datastore.DatastoreDefs;
import com.tangly.scorecard.model.*;

// TODO make yet another base class and specify the class name through the
// intent?!
public class ManageDiceFragment extends StorableListViewFragment
{
	private static final String TAG = "DiceManager";
	private EditCallback callback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.manage_model, null);

		Button addDiceBtn = (Button) view.findViewById(R.id.addBtn);
		addDiceBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				callback = new EditCallback();

				// Adding will show the fragment
				callback.addStorable();
			}
		});
		
		return view;
	}

	protected class EditCallback implements EditStorableCallback, NoticeDialogFragment.NoticeDialogListener
	{
		protected Dice dice;

		/**
		 * Creates an EditDiceDialogFragment without specifying any arguments in a bundle.
		 */
		public void addStorable()
		{
			dice = new Dice();
			EditDiceDialogFragment newFragment = new EditDiceDialogFragment();
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "diceEdit");
		}

		public void editStorable(long id)
		{
			// Get reference to the storable that will be used
			dice = (Dice) getItemById(id);

			EditDiceDialogFragment newFragment = new EditDiceDialogFragment();
			Bundle argsBundle = new Bundle();
			argsBundle.putString(EditDiceDialogFragment.BUNDLE_DICE_NAME, dice.getDisplayName());
			argsBundle.putInt(EditDiceDialogFragment.BUNDLE_NUM_SIDES, dice.getNumSides());

			newFragment.setArguments(argsBundle);
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "diceEdit");
		}

		/**
		 * @see NoticeDialogFragment.NoticeDialogListener.onDialogPositiveClick
		 */	
		public void onDialogPositiveClick(DialogFragment dialog)
		{
			EditDiceDialogFragment fragment = (EditDiceDialogFragment) dialog;
			Dice updatedDice = fragment.getDice();

			// Update the underlying model if there are changes
			if (this.dice.compareTo(updatedDice) != 0)
			{
				// New dice if the ID is an invalid ID
				boolean newDie = (this.dice.getId() == DatastoreDefs.INVALID_ID);
				Log.d(TAG, "Changes detected, updating...");

				this.dice.setDisplayName(updatedDice.getDisplayName());
				this.dice.setResultMap(updatedDice.getResultMap());
				this.dice.save(getDatastore());

				// Update adapter since there has been a change
				if (newDie)
				{
					getAdapter().add(this.dice);
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
	protected Class<Dice> getStorableType() { return Dice.class; }

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
