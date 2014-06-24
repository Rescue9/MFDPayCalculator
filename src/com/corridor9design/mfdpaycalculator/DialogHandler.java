/**
 * Program: DialogHandler.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create handler for various fragments that will 
 *  gather information to be used in the calculation formula.
 */

package com.corridor9design.mfdpaycalculator;

import com.corridor9design.mfdpaycalculator.engine.ValueModifier;
import com.corridor9design.mfdpaycalculator.engine.ValuesHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DialogHandler extends DialogFragment {
	// create value handling objects
	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();

	// declare objects for getting the specific dialog selected
	// and viewing elements
	EditText dialog_edittext_value;
	int value;
	View view;
	AlertDialog alertDialog;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get arguments from bundle
		value = getArguments().getInt("key");

		// create a dialog builder for easier dialog handling
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// inflate the dialog fragment into this view
		view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment, null);

		// dialog display message chosen by switch statement based upon key
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

		// set the view to be inflated by the builder
		builder.setView(view);
		dialog_edittext_value = (EditText) view.findViewById(R.id.dialog_edittext1);


		// Add positive action button
		builder.setPositiveButton(R.string.dialog_button_accept, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				// set value to 0 if empty
				if (dialog_edittext_value.getText().length() == 0) {
					dialog_edittext_value.setText("0");
				}

				// set the values for the calc engine based upon which button pressed
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
		});

		// add negative action button
		builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				DialogHandler.this.getDialog().cancel();
			}
		});

		dialog_edittext_value.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE){
					alertDialog.getButton(Dialog.BUTTON_POSITIVE).performClick();
				}
				
				return false;
			}
			
		});
		// create the dialog
		alertDialog = builder.create();
		return alertDialog;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// instantiate text entry field
		dialog_edittext_value = (EditText) view.findViewById(R.id.dialog_edittext1);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE); // force soft keyboard visible

		// execute code based upon which button pressed
		switch (value) {
		case 0:
			if (vh.getHolidays_during_pay() != 0) {
				dialog_edittext_value.setText(vh.getHolidays_during_pay() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			dialog_edittext_value.selectAll();
			break;
		case 1:
			if (vh.getCallback_hours() != 0) {
				dialog_edittext_value.setText(vh.getCallback_hours() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dialog_edittext_value.selectAll();
			break;
		case 2:
			if (vh.getScheduled_days() != 0) {
				dialog_edittext_value.setText(vh.getScheduled_days() + "");
			}
			dialog_edittext_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			dialog_edittext_value.selectAll();
			break;

		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// refresh the main activity window on closing the dialog
		// this force refreshes the button text based upon values entered in the dialog
		MainActivity ma = (MainActivity) getActivity();
		ma.refreshGui();
	}
}
