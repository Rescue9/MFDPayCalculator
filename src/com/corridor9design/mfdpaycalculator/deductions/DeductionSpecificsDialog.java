/**
 * Program: DeductionSpecificsDialog.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a dialog fragment for displaying
 *  the specific information about a selected deduction.
 */

package com.corridor9design.mfdpaycalculator.deductions;

import com.corridor9design.mfdpaycalculator.R;
import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class DeductionSpecificsDialog extends DialogFragment {
	// declare variables
	String deduction_name;
	String deduction_amount;
	String deduction_description;
	String first_payday;
	String second_payday;
	String third_payday;
	long database_id;

	// declare gui elements
	TextView deduction_specific_amount;
	TextView deduction_specific_description;

	CheckBox deduction_first_payday;
	CheckBox deduction_second_payday;
	CheckBox deduction_third_payday;

	View view;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database_id = getArguments().getLong("database_row");

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		view = getActivity().getLayoutInflater().inflate(R.layout.dialog_deduction_specifics, null);

		// setup gui
		setupGuiElements();
		getDeduction();

		alertDialogBuilder.setTitle(deduction_name);
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setPositiveButton(R.string.deduction_button_ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialogBuilder.setNegativeButton(R.string.deduction_button_delete, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteDeductionItem();
			}
		});
		return alertDialogBuilder.create();
	}

	public void onResume() {
		super.onResume();

	}

	public void setupGuiElements() {

		deduction_specific_amount = (TextView) view.findViewById(R.id.deduction_specific_amount);
		deduction_specific_description = (TextView) view.findViewById(R.id.deduction_specific_description);
		deduction_first_payday = (CheckBox) view.findViewById(R.id.deduction_specific_checkbox_first_payday);
		deduction_second_payday = (CheckBox) view.findViewById(R.id.deduction_specific_checkbox_second_payday);
		deduction_third_payday = (CheckBox) view.findViewById(R.id.deduction_specific_checkbox_third_payday);
	}

	public void getDeduction() {
		ContentResolver resolver = getActivity().getContentResolver();
		ContentValues values = new ContentValues();

		// set arrays for querying database
		String[] projection = new String[] { "_id", "name", "amount", "description", "payday1", "payday2", "payday3" };
		String[] selectionArgs = new String[] { database_id + "" };

		values.clear();

		// get these columns into the content values object
		values.get(Deduction.COLUMN_AMOUNT);
		values.get(Deduction.COLUMN_DESCRIPTION);
		values.get(Deduction.COLUMN_ID);
		values.get(Deduction.COLUMN_NAME);
		values.get(Deduction.COLUMN_PAYDAY1);
		values.get(Deduction.COLUMN_PAYDAY2);
		values.get(Deduction.COLUMN_PAYDAY3);

		// we want a singular row, so we'll create a new URI with the row id
		Uri singleUri = ContentUris.withAppendedId(DeductionContentProvider.CONTENT_URI, database_id);

		// get the row from the content provider into a cursor
		Cursor cursor = resolver.query(singleUri, projection, Deduction.COLUMN_ID + "=?", selectionArgs, null);
		cursor.moveToFirst();

		deduction_name = cursor.getString(1);
		deduction_specific_amount.setText(cursor.getString(2));
		deduction_specific_description.setText(cursor.getString(3));

		// use these if statements to check boxes as return results are strings
		if (cursor.getString(4).equals("true")) {
			deduction_first_payday.setChecked(true);
		}
		if (cursor.getString(5).equals("true")) {
			deduction_second_payday.setChecked(true);
		}
		if (cursor.getString(6).equals("true")) {
			deduction_third_payday.setChecked(true);
		}

	}

	private void deleteDeductionItem() {
		// create a resolver to connect to the content provider
		ContentResolver resolver = getActivity().getContentResolver();
		String[] selectionArgs = new String[] { database_id + "" };

		resolver.delete(DeductionContentProvider.CONTENT_URI, Deduction.COLUMN_ID + "=?", selectionArgs);
	}
}
