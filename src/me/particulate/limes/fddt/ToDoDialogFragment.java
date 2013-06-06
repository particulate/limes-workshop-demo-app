package me.particulate.limes.fddt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ToDoDialogFragment extends DialogFragment {

	public interface ToDoDialogListener {
		public void onDialogDeleteClick();

		public void onDialogEditClick();
	}

	ToDoDialogListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the ToDoDialogListener so we can send events to the
			// host
			listener = (ToDoDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement ToDoDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(new String[] { /*"Edit",*/ "Delete" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//						if (which == 0) {
//							listener.onDialogEditClick();
//						} else {
							listener.onDialogDeleteClick();
//						}
					}
				});
		AlertDialog dialog = builder.create();
		return dialog;
	}
}
