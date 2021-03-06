package com.tangly.scorecard.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tangly.framework.dialogs.EditDiceDialogFragment;
import com.tangly.framework.dialogs.NoticeDialogFragment;
import com.tangly.scorecard.EditStorableCallback;
import com.tangly.scorecard.R;
import com.tangly.scorecard.R.id;
import com.tangly.scorecard.R.layout;
import com.tangly.scorecard.datastore.DatastoreDefs;
import com.tangly.scorecard.model.Dice;

/**
 * Fragment for managing Dice
 * @author Paul
 *
 */
public class ManageDiceFragment extends StorableListViewFragment<Dice>
{
    private static final String TAG = "DiceManager";
    private EditCallback callback;

    public ManageDiceFragment()
    {
        super(Dice.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.manage_model, null);

        Button addDiceBtn = (Button) view.findViewById(R.id.addBtn);
        addDiceBtn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View p1)
            {
                callback = new EditCallback();

                // Adding will show the fragment
                callback.addStorable();
            }
        });

        return view;
    }

    protected class EditCallback implements EditStorableCallback,
            NoticeDialogFragment.NoticeDialogListener
    {
        protected Dice dice;
        boolean isFromStorage = false;

        /**
         * Creates an EditDiceDialogFragment without specifying any arguments in
         * a bundle.
         */
        public void addStorable()
        {
            dice = new Dice();
            this.showDiceDialog(dice);
        }

        public void editStorable(long id)
        {
            // Get reference to the storable that will be used
            dice = (Dice) getItemById(id);
            this.isFromStorage = true;
            this.showDiceDialog(dice);
        }

        private void showDiceDialog(Dice dice)
        {
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
            if (!this.isFromStorage || this.dice.compareTo(updatedDice) != 0)
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
         * 
         * @see NoticeDialogFragment.NoticeDialogListener.onDialogNegativeClick
         */
        public void onDialogNegativeClick(DialogFragment dialog)
        {
        }
    }

    /**
     * @see StorableListViewFragment.getEditStorableCallback
     */
    @Override
    protected EditStorableCallback getEditStorableCallback()
    {
        return new EditCallback();
    }
}
