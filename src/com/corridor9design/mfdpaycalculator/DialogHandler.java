package com.corridor9design.mfdpaycalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

public class DialogHandler extends DialogFragment {

	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();

	EditText dialog_edittext_value;
	int value;
	View view;

	public DialogHandler() {
		// empty constructor
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get arguments from bundle
		value = getArguments().getInt("key");

		// System.out.println(value); //TESTING used to check value passed during dialog creation

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_fragment, null);

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
		builder.setView(view)
		// Add action buttons
				.setPositiveButton(R.string.dialog_button_accept, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog_edittext_value = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialog_edittext1);

						// check entry for empty
						if (dialog_edittext_value.getText().length() == 0) {
							dialog_edittext_value.setText("0");
						}

						// check for int
						// FIXME START HERE!!!! CHECK FOR INT in edittext vox

						// check for double
						// FIXME Then check for double!

						switch (value) {
						case 0:
							vh.setHolidays_during_pay(Integer.parseInt(dialog_edittext_value.getText().toString()));
							break;
						case 1:
							vh.setOvertime_hours(Double.parseDouble(dialog_edittext_value.getText().toString()));
							break;
						case 2:
							vh.setScheduled_days(Integer.parseInt(dialog_edittext_value.getText().toString()));
							break;

						}
					}
				}).setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DialogHandler.this.getDialog().cancel();
					}
				});
		return builder.create();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialog_edittext_value = (EditText) view.findViewById(R.id.dialog_edittext1);
		dialog_edittext_value.clearFocus();
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		switch (value) {
		case 0:
			if (vh.getHolidays_during_pay() != 0) {
				dialog_edittext_value.setText(vh.getHolidays_during_pay() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			dialog_edittext_value.selectAll();
			dialog_edittext_value.requestFocus();
			break;
		case 1:
			if (vh.getCallback_hours() != 0) {
				dialog_edittext_value.setText(vh.getCallback_hours() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dialog_edittext_value.selectAll();
			dialog_edittext_value.requestFocus();
			break;
		case 2:
			if (vh.getScheduled_days() != 0) {
				dialog_edittext_value.setText(vh.getScheduled_days() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			dialog_edittext_value.selectAll();
			dialog_edittext_value.requestFocus();
			break;

		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MainActivity ma = (MainActivity) getActivity();
		ma.refreshGui();
	}
}
