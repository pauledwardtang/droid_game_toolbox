package com.tangly.framework.dialogs;

import com.tangly.scorecard.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Dialog fragment which displays a single edit text field
 */
public class EditTextDialogFragment extends NoticeDialogFragment
{
	private static String DEFAULT_TITLE = "Edit field";
	private String title;
	private String defaultTextFieldString;
	private EditText textField;

	/**
	 * Default cstor which populates a default title and an empty text field
	 */
	public EditTextDialogFragment()
	{
		this(DEFAULT_TITLE, "");
	}

	// TODO non-default constructors are highly discouraged since the restoring the state will use the empty args cstor
        // TODO when this is fixed, remove the lint.xml
	/**
	 * Populates a default title and a text field with the given default text
	 * @param defaultText The given default text with which to populate the text field
	 */
	public EditTextDialogFragment(String defaultText)
	{
		this(DEFAULT_TITLE, defaultText);
	}

	/**
	 * Default cstor
	 * @param title The dialog title
	 * @param 
	 */
 	public EditTextDialogFragment(String title, String defaultText)
	{
		this.title = title;
		this.defaultTextFieldString = defaultText;
	}

	public String getTextFieldString()
	{
		return this.textField.getText().toString();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// 2. Chain together various setter methods to set the dialog characteristics
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.single_edit_text, null);
		this.textField = (EditText) ((LinearLayout) view).getChildAt(0);
		this.textField.setText(this.defaultTextFieldString);

		builder.setTitle(this.title)
			.setView(view)
			.setPositiveButton("Save", new PositiveButtonDialogOnClickListener())
			.setNegativeButton("Cancel",new NegativeButtonDialogOnClickListener())
		;

		// 3. Get the AlertDialog from create()
		return builder.create();
	}
}
