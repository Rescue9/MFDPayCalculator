package com.corridor9design.mfdpaycalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DialogHandler extends DialogFragment {

	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();

	EditText dialog_edittext_value;

	public DialogHandler() {
		// empty constructor
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get arguments from bundle
		int value = getArguments().getInt("key");

		// System.out.println(value); //TESTING used to check value passed during dialog creation

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// create switch statement for dialog message display per button
		String dialogMessage = null;
		switch (value) {
		case 0:
			dialogMessage = getResources().getString(R.string.dialog_message_holidays);
			break;
		case 1:
			dialogMessage = getResources().getString(R.string.dialog_message_overtime);
			break;
		case 2:
			dialogMessage = getResources().getString(R.string.dialog_message_scheduled);
			break;

		}
		builder.setMessage(dialogMessage);

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_fragment, null))
		// Add action buttons
				.setPositiveButton(R.string.dialog_button_accept, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog_edittext_value = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialog_edittext1);
						vh.setBase_pay_total(Double.parseDouble(dialog_edittext_value.getText().toString()));
					}
				}).setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DialogHandler.this.getDialog().cancel();
					}
				});
		return builder.create();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
	    MainActivity ma = (MainActivity) getActivity();
	    ma.refreshGui();
	}
}
