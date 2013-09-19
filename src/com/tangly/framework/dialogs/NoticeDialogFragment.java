package com.tangly.framework.dialogs;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.*;

public class NoticeDialogFragment extends DialogFragment
{
	// Use this instance of the interface to deliver action events
	protected NoticeDialogListener mListener; 

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener
	{
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if (this.getMListener() == null)
		{
			throw new IllegalStateException("Listener must be attached to this Fragment");
		}
	}

	protected class PositiveButtonDialogOnClickListener implements DialogInterface.OnClickListener
	{
		public void onClick(DialogInterface dialog, int id)
		{
			mListener.onDialogPositiveClick(NoticeDialogFragment.this);
		}
	}

	protected class NegativeButtonDialogOnClickListener implements DialogInterface.OnClickListener
	{
		public void onClick(DialogInterface dialog, int id)
		{
			mListener.onDialogNegativeClick(NoticeDialogFragment.this);
		}
	}

	public void setMListener(NoticeDialogListener mListener)
	{
		this.mListener = mListener;
	}

	public NoticeDialogListener getMListener()
	{
		return mListener;
	}
}
