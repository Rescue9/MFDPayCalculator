package com.corridor9design.mfdpaycalculator;

import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;
import com.corridor9design.mfdpaycalculator.database.MyDeductionDbHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class DeductionEditActivity extends Activity {

	// declare variables
	String deduction_name;
	String deduction_amount;
	String deduction_description;
	String first_payday;
	String second_payday;
	String third_payday;

	// declare gui elements
	EditText deduction_name_edit;
	EditText deduction_amount_edit;
	EditText deduction_description_edit;
	CheckBox deduction_first_pay_checkbox;
	CheckBox deduction_second_pay_checkbox;
	CheckBox deduction_third_pay_checkbox;
	Button deduction_accept_button;
	Button deduction_cancel_button;
	Button deduction_clear_button;

	MyDeductionDbHelper db = new MyDeductionDbHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deduction_editor);

		setupGuiInstances();
		startListening();
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
		deduction_description_edit = (EditText) findViewById(R.id.deduction_description);
		deduction_first_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_first_payday);
		deduction_second_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_second_payday);
		deduction_third_pay_checkbox = (CheckBox) findViewById(R.id.deduction_checkbox_third_payday);
		deduction_accept_button = (Button) findViewById(R.id.deduction_accept_button);
		deduction_cancel_button = (Button) findViewById(R.id.deduction_cancel_button);
		deduction_clear_button = (Button) findViewById(R.id.deduction_clear_button);
	}

	private void getValues() {
		// get values from current input
		deduction_name = deduction_name_edit.getText().toString();
		deduction_amount = deduction_amount_edit.getText().toString();
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

	private void clearValues() {
		deduction_name_edit.setText("");
		deduction_amount_edit.setText("");
		deduction_description_edit.setText("");
		deduction_first_pay_checkbox.setChecked(false);
		deduction_second_pay_checkbox.setChecked(false);
		deduction_third_pay_checkbox.setChecked(false);
	}

	private void cancelThis() {
		finish();
	}

	private void startListening() {
		deduction_accept_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getValues();
				writeValuesToDatabase();
				finish();
			}

		});

		deduction_cancel_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelThis();
			}
		});

		deduction_clear_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearValues();
			}
		});
	}

	private void writeValuesToDatabase() {
		// create a resolver to connect to the content provider
		ContentResolver resolver = getContentResolver();

		ContentValues values = new ContentValues();
		values.put(Deduction.COLUMN_AMOUNT, deduction_amount);
		values.put(Deduction.COLUMN_DESCRIPTION, deduction_description);
		values.put(Deduction.COLUMN_NAME, deduction_name);
		values.put(Deduction.COLUMN_PAYDAY1, first_payday);
		values.put(Deduction.COLUMN_PAYDAY2, second_payday);
		values.put(Deduction.COLUMN_PAYDAY3, third_payday);

		resolver.insert(DeductionContentProvider.CONTENT_URI, values);
	}

}
