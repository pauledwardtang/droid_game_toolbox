package com.tangly.scorecard;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.model.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

/**
 * Main activity which populates a list of games 
 */
public class MainActivity extends StorableListViewActivity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button editButton = (Button) findViewById(R.id.addGameBtn);
		editButton.setOnClickListener(new OnClickListener(){
			private DefaultEditStorableCallback callback = new DefaultEditStorableCallback(getApplicationContext(),
																						   NewGameSessionActivity.class);
			public void onClick(View p1)
			{
				this.callback.editStorable(DatastoreDefs.INVALID_ID);
			}
		});

		Button mngPlyrsBtn = (Button) findViewById(R.id.mngPlyrsBtn);
		mngPlyrsBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1)
			{
				startActivity(new Intent(getApplicationContext(), ManagePlayersActivity.class));
			}
		});
		Button mngDiceBtn = (Button) findViewById(R.id.mngDiceBtn);
		mngDiceBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1)
			{
				startActivity(new Intent(getApplicationContext(), ManageDiceActivity.class));
			}
		});
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
		return new DefaultEditStorableCallback(getApplicationContext(),
											   this.getEditStorableActivity());
	}

	/**
	 * @see StorableListViewActivity.getStorableType
	 */
	@Override
	protected Class getStorableType() { return GameSession.class; }

	/**
	 * @see StorableListViewActivity.getEditStorableActivity
	 */
	@Override
	protected Class getEditStorableActivity() { return NewGameSessionActivity.class; }

	/**
	 * @see StorableListViewActivity.getTextViewResourceId
	 */
	@Override
	protected int getTextViewResourceId() { return R.layout.new_game_session;}
}
