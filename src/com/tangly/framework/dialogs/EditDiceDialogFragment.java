package com.tangly.framework.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.tangly.scorecard.R;
import com.tangly.scorecard.model.Dice;
import com.tangly.scorecard.model.Dice.DiceBuilder;

/**
 * Dialog fragment for creating a custom dice
 */
public class EditDiceDialogFragment extends NoticeDialogFragment
{
    private static String DEFAULT_TITLE = "Dice Editor";
    private static String DEFAULT_DICE_TEXT = "D6";
    private static int MIN_SIDES = 2;
    private static int MAX_SIDES = 100;

    public static String BUNDLE_DICE_NAME = "DICE_NAME";
    public static String BUNDLE_NUM_SIDES = "NUM_SIDES";

    private String title;
    private EditText diceName;
    private NumberPicker numSides;

    /**
     * Default cstor which populates a default title and an empty text field. To
     * populate the fields you MUST call setArguments with a bundle using the
     * public bundle strings provided in this class
     */
    public EditDiceDialogFragment ()
    {
        this.title = DEFAULT_TITLE;
    }

    /**
     * Gets a Dice using the underlying form fields. For now, ID is always unset
     * 
     * @return a Dice using the underlying form fields
     */
    public Dice getDice ()
    {
        Dice.DiceBuilder builder = DiceBuilder.getBuilder();
        builder.setName(this.getDiceName());

        // For now just always create default result names
        for (int i = 0; i < this.getNumSides(); i++)
            builder.add(i, String.valueOf(i));

        return builder.build();
    }

    public String getDiceName ()
    {
        return this.diceName.getText().toString();
    }

    public int getNumSides ()
    {
        return this.numSides.getValue();
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog
        // characteristics
        LayoutInflater inflater = getActivity().getLayoutInflater();
        RelativeLayout view = (RelativeLayout) inflater.inflate(
                R.layout.new_dice, null);

        // Populate the view instances
        this.diceName = (EditText) ((LinearLayout) view.getChildAt(0))
                .getChildAt(1);
        this.numSides = (NumberPicker) ((LinearLayout) view.getChildAt(1))
                .getChildAt(1);
        this.numSides.setMinValue(MIN_SIDES);
        this.numSides.setMaxValue(MAX_SIDES);
        this.numSides.setWrapSelectorWheel(false);

        // Populate the fields with the saved instance bundle or the args bundle
        this.populateFieldsFromSavedStateOrArgument(savedInstanceState,
                this.getArguments());

        builder.setTitle(this.title)
                .setView(view)
                .setPositiveButton("Save",
                        new PositiveButtonDialogOnClickListener())
                .setNegativeButton("Cancel",
                        new NegativeButtonDialogOnClickListener());

        // 3. Get the AlertDialog from create()
        return builder.create();
    }

    /**
     * Populate fields from the saved instance state, or the arguments set just
     * after this object was constructed (in that order). Assumes the internal
     * views have been already populated.
     * 
     * @param savedInstance
     * @param args
     */
    private void populateFieldsFromSavedStateOrArgument (Bundle savedInstance,
            Bundle args)
    {
        if (savedInstance != null)
        {
            if (savedInstance.containsKey(BUNDLE_DICE_NAME))
                this.diceName
                        .setText(savedInstance.getString(BUNDLE_DICE_NAME));
            if (savedInstance.containsKey(BUNDLE_NUM_SIDES))
                this.numSides.setValue(savedInstance.getInt(BUNDLE_NUM_SIDES));
            // TODO restore the result map too
        } else if (args != null)
        {
            if (args.containsKey(BUNDLE_DICE_NAME))
                this.diceName.setText(args.getString(BUNDLE_DICE_NAME));
            if (args.containsKey(BUNDLE_NUM_SIDES))
                this.numSides.setValue(args.getInt(BUNDLE_NUM_SIDES));
            // TODO restore the result map too
        } else
        {
            this.diceName.setText(DEFAULT_DICE_TEXT);
            this.numSides.setValue(Dice.DEFAULT_NUM_SIDES);
        }
    }

    // TODO on pause this should be called?
    @Override
    public void onSaveInstanceState (Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        // savedInstanceState.putBoolean("MyBoolean", true);
        // savedInstanceState.putDouble("myDouble", 1.9);
        // savedInstanceState.putInt("MyInt", 1);
        // savedInstanceState.putString("MyString", "Welcome back to Android");
    }
}
