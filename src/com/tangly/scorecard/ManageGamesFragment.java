package com.tangly.scorecard;

import android.app.Activity;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.tangly.framework.dialogs.NoticeDialogFragment;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;

/**
 * Fragment for managing the list of games 
 */
public class ManageGamesFragment extends StorableListViewFragment
{
	private static final String TAG = "GameSessionManager";
	private EditCallback callback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.manage_model, null);

		Button addGamesBtn = (Button) view.findViewById(R.id.addBtn);
		addGamesBtn.setOnClickListener(new OnClickListener(){
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
		protected GameSession gs;

		/**
		 * Creates a Fragment without specifying any arguments in a bundle.
		 */
		public void addStorable()
		{
			this.gs = new GameSession();
			EditGameSessionDialogFragment newFragment = new EditGameSessionDialogFragment();
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "gsEdit");
		}

		public void editStorable(long id)
		{
			// Get reference to the storable that will be used
			this.gs = (GameSession) getItemById(id);

			EditGameSessionDialogFragment newFragment = new EditGameSessionDialogFragment();

			Bundle argsBundle = new Bundle();
			argsBundle.putLong(EditGameSessionDialogFragment.BUNDLE_GS_ID, this.gs.getId());
			argsBundle.putString(EditGameSessionDialogFragment.BUNDLE_GS_NAME, this.gs.getDisplayName());
			long[] playerIds = new long[this.gs.getPlayers().size()];
			for (int i = 0; i < this.gs.getPlayers().size(); i++)
			{
				playerIds[i] = this.gs.getPlayers().get(i).getId();
			}
			argsBundle.putLongArray(EditGameSessionDialogFragment.BUNDLE_GS_PLAYER_IDS, playerIds);
			newFragment.setArguments(argsBundle);
			
			newFragment.setMListener(EditCallback.this);
			newFragment.show(getFragmentManager(), "gsEdit");
		}

		/**
		 * @see NoticeDialogFragment.NoticeDialogListener.onDialogPositiveClick
		 */	
		public void onDialogPositiveClick(DialogFragment dialog)
		{
			EditGameSessionDialogFragment fragment = (EditGameSessionDialogFragment) dialog;
			GameSession updatedGs = fragment.getSession();

			// Update the underlying model if there are changes
			if (this.gs.compareTo(updatedGs) != 0)
			{
				// New storable if the ID is an invalid ID
				boolean newStorable = (this.gs.getId() == DatastoreDefs.INVALID_ID);
				Log.d(TAG, "Changes detected, updating...");

				this.gs.setDisplayName(updatedGs.getDisplayName());
				this.gs.setPlayers(updatedGs.getPlayers());
				getDatastore().store(this.gs);
				
				// TODO deprecate? Or double dispatch?
//				this.gs.save(getDatastore());

				// Update adapter since there has been a change
				if (newStorable)
				{
					getAdapter().add(this.gs);
				}
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
	protected Class<? extends Storable> getStorableType() { return GameSession.class; }

	/**
	 * @see StorableListViewFragment.getEditStorableActivity
	 */
	@Override
	protected Class<? extends Activity> getEditStorableActivity() { return null; }

	/**
	 * @see StorableListViewFragment.getTextViewResourceId
	 */
	@Override
	protected int getTextViewResourceId() { return android.R.id.list; }
}
