/**
 * Program: DeductionEditActivity.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a deduction activity for entering deduction information.
 *  An activity is used versus a fragment for ease of use.
 */

package com.corridor9design.mfdpaycalculator.deductions;

import com.corridor9design.mfdpaycalculator.R;
import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;
import com.corridor9design.mfdpaycalculator.database.MyDeductionDbHelper;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class DeductionEditActivity extends Activity {

	// declare variables
	String deduction_name;
	String deduction_amount;
	String deduction_number;
	String deduction_description;
	String first_payday;
	String second_payday;
	String third_payday;
	long database_id;

	// declare gui elements
	EditText deduction_name_edit;
	EditText deduction_amount_edit;
	EditText deduction_number_edit;
	EditText deduction_description_edit;
	CheckBox deduction_first_pay_checkbox;
	CheckBox deduction_second_pay_checkbox;
	CheckBox deduction_third_pay_checkbox;
	Button deduction_positive_button;
	Button deduction_negative_button;
	Button deduction_neutral_button;

	MyDeductionDbHelper db = new MyDeductionDbHelper(this);
	ContentValues values = new ContentValues();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deduction_editor);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setupGuiInstances();
		startListening();
	}

	@Override
	public void onResume() {
		super.onResume();

		// get the database id
		long db_id = getIntent().getLongExtra("database_id", -1);

		// if the id isn't negative, then we edit ti
		if (db_id > -1) {
			editDeduction(db_id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
		// Inflate the menu; this adds items to the action bar if it is present.
	}

	public void setupGuiInstances() {
		// gui display elements
		deduction_name_edit = (EditText) findViewById(R.id.deduction_name);
		deduction_amount_edit = (EditText) findViewById(R.id.deduction_amount);
		deduction_number_edit = (EditText) findViewById(R.id.deduction_number);
		deduction_description_edit = (EditText) findViewById(R.id.deduction_description);
		deduction_first_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_first_payday);
		deduction_second_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_second_payday);
		deduction_third_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_third_payday);
		deduction_positive_button = (Button) findViewById(R.id.deduction_positive_button);
		deduction_negative_button = (Button) findViewById(R.id.deduction_negative_button);
		deduction_neutral_button = (Button) findViewById(R.id.deduction_neutral_button);
	}

	private void getValues() {
		// get values from current input
		deduction_name = deduction_name_edit.getText().toString();
		deduction_amount = deduction_amount_edit.getText().toString();
		deduction_number = deduction_number_edit.getText().toString();
		deduction_description = deduction_description_edit.getText().toString();

		if (deduction_first_pay_checkbox.isChecked()) {
			first_payday = "true";
		} else
			first_payday = "false";

		if (deduction_second_pay_checkbox.isChecked()) {
			second_payday = "true";
		} else
			second_payday = "false";

		if (deduction_third_pay_checkbox.isChecked()) {
			third_payday = "true";
		} else
			third_payday = "false";
	}

	private void editDeduction(long id) {
		// setup resolver to get from content provider
		database_id = id;
		ContentResolver resolver = getContentResolver();

		// set arrays for querying database
		String[] projection = new String[] { "_id", "name", "amount", "number", "description", "payday1", "payday2", "payday3" };
		String[] selectionArgs = new String[] { id + "" };

		// clear previous values
		values.clear();

		// get these columns into the content values object
		values.get(Deduction.COLUMN_AMOUNT);
		values.get(Deduction.COLUMN_DESCRIPTION);
		values.get(Deduction.COLUMN_ID);
		values.get(Deduction.COLUMN_NAME);
		values.get(Deduction.COLUMN_NUMBER);
		values.get(Deduction.COLUMN_PAYDAY1);
		values.get(Deduction.COLUMN_PAYDAY2);
		values.get(Deduction.COLUMN_PAYDAY3);

		// we want a singular row, so we'll create a new URI with the row id
		Uri singleUri = ContentUris.withAppendedId(DeductionContentProvider.CONTENT_URI, id);

		// get the row from the content provider into a cursor
		Cursor cursor = resolver.query(singleUri, projection, Deduction.COLUMN_ID + "=?", selectionArgs, null);
		cursor.moveToFirst();

		// set the edittext areas to the cursor strings from the database
		deduction_name_edit.setText(cursor.getString(1));
		deduction_amount_edit.setText(cursor.getString(2));
		deduction_number_edit.setText(cursor.getString(3));
		deduction_description_edit.setText(cursor.getString(4));

		// use these if statements to check boxes as return results are strings
		if (cursor.getString(5).equals("true")) {
			deduction_first_pay_checkbox.setChecked(true);
		}
		if (cursor.getString(6).equals("true")) {
			deduction_second_pay_checkbox.setChecked(true);
		}
		if (cursor.getString(7).equals("true")) {
			deduction_third_pay_checkbox.setChecked(true);
		}

		// change the names of the buttons since we're not adding a new item
		deduction_positive_button.setText(R.string.deduction_button_delete);
		deduction_positive_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDeductionItem();
				finish();
			}
		});

		deduction_neutral_button.setText(R.string.deduction_button_update);
		deduction_neutral_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(deduction_name_edit.getText().length()==0 && deduction_amount_edit.getText().length()==0){
					deleteDeductionItem();
					finish();
				}
				updateDeductionItem();
				finish();
			}
		});
		
		deduction_negative_button.setText(R.string.deduction_button_ok);
		deduction_negative_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// clear all the values so the user can input new information
	private void clearValues() {
		deduction_name_edit.setText("");
		deduction_amount_edit.setText("");
		deduction_number_edit.setText("");
		deduction_description_edit.setText("");
		deduction_first_pay_checkbox.setChecked(false);
		deduction_second_pay_checkbox.setChecked(false);
		deduction_third_pay_checkbox.setChecked(false);
	}

	private void startListening() {
		deduction_positive_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		deduction_negative_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Added deduction");
				getValues();
				createDeductionItem();
				finish();
			}
		});

		deduction_neutral_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearValues();
			}
		});
	}

	// create the deduction item and insert it
	private void createDeductionItem() {
		// create a resolver to connect to the content provider
		ContentResolver resolver = getContentResolver();

		// clear the ContentValues object; values
		values.clear();

		values.put(Deduction.COLUMN_AMOUNT, deduction_amount);
		values.put(Deduction.COLUMN_DESCRIPTION, deduction_description);
		values.put(Deduction.COLUMN_NAME, deduction_name);
		values.put(Deduction.COLUMN_NUMBER, deduction_number);
		values.put(Deduction.COLUMN_PAYDAY1, first_payday);
		values.put(Deduction.COLUMN_PAYDAY2, second_payday);
		values.put(Deduction.COLUMN_PAYDAY3, third_payday);

		resolver.insert(DeductionContentProvider.CONTENT_URI, values);
	}

	// update the deduction item and insert it
	private void updateDeductionItem() {
		// get values from gui
		getValues();
		// create a resolver to connect to the content provider
		ContentResolver resolver = getContentResolver();
		String[] selectionArgs = new String[] { database_id + "" };

		// clear values
		values.clear();

		values.put(Deduction.COLUMN_AMOUNT, deduction_amount);
		values.put(Deduction.COLUMN_DESCRIPTION, deduction_description);
		values.put(Deduction.COLUMN_NAME, deduction_name);
		values.put(Deduction.COLUMN_NUMBER, deduction_number);
		values.put(Deduction.COLUMN_PAYDAY1, first_payday);
		values.put(Deduction.COLUMN_PAYDAY2, second_payday);
		values.put(Deduction.COLUMN_PAYDAY3, third_payday);

		resolver.update(DeductionContentProvider.CONTENT_URI, values, Deduction.COLUMN_ID + "=?", selectionArgs);
	}

	// delete the deduction item from the database
	private void deleteDeductionItem() {
		// create a resolver to connect to the content provider
		ContentResolver resolver = getContentResolver();
		String[] selectionArgs = new String[] { database_id + "" };

		resolver.delete(DeductionContentProvider.CONTENT_URI, Deduction.COLUMN_ID + "=?", selectionArgs);
	}
}
