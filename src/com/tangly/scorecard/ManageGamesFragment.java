package com.tangly.scorecard;

import android.app.Activity;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;

/**
 * Fragment for managing the list of games 
 */
public class ManageGamesFragment extends StorableListViewFragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.manage_model, null);

        Button addBtn = (Button) view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new OnClickListener(){
			private DefaultEditStorableCallback callback
				= new DefaultEditStorableCallback(getActivity().getApplicationContext(),NewGameSessionActivity.class);
			public void onClick(View p1){
				this.callback.editStorable(DatastoreDefs.INVALID_ID);
			}
		});
		return view;
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
		return new DefaultEditStorableCallback(getActivity().getApplicationContext(),
											   this.getEditStorableActivity());
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
	protected Class<? extends Activity> getEditStorableActivity() { return NewGameSessionActivity.class; }

	/**
	 * @see StorableListViewFragment.getTextViewResourceId
	 */
	@Override
	protected int getTextViewResourceId() { return R.layout.new_game_session;}
}
