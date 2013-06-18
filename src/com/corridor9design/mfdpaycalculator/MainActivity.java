/**
 * Program: MainActivity.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create an Android application that can be used by members
 *  of the Madisonville Fire Department to properly calculate their individual
 *  paycheck amounts based upon the number of holidays, overtime hours, and 
 *  scheduled days worked in a specific pay period.
 */

package com.corridor9design.mfdpaycalculator;

import java.text.DecimalFormat;

import com.corridor9design.mfdpaycalculator.deductions.DeductionListDialog;
import com.corridor9design.mfdpaycalculator.engine.CalcEngine;
import com.corridor9design.mfdpaycalculator.engine.ValueModifier;
import com.corridor9design.mfdpaycalculator.engine.ValuesHandler;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// debug tag for logging
	static final String TAG = "MFDPayCalc";

	// gui text elements
	TextView base_pay_total;
	TextView gross_pay_total;
	TextView taxes_total;
	TextView deposited_total;
	TextView rank_label;

	// gui entry elements
	EditText base_pay_rate;
	EditText overtime1_rate;
	EditText overtime2_rate;
	EditText years_worked;

	// gui button elements
	Button holidays_button;
	Button overtime_button;
	Button scheduled_days_button;
	Button calculate_button;
	private RadioGroup radio_pay_group;

	// gui layout elements
	LinearLayout simple_layout_container;
	LinearLayout advanced_layout_container;

	boolean premium_purchased = false; // set premium purchase to false on run

	// create handler objects to deal with preferences & calc engine values
	PreferencesHandler ph = new PreferencesHandler();
	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();

	// set specific decimal formats for different TextView fields
	DecimalFormat df_totals = new DecimalFormat("$##0.00");
	DecimalFormat df_rates = new DecimalFormat("##0.000");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// tag logcat for initial start
		Log.d(TAG, "Starting...");
		setContentView(R.layout.activity_main);

		// hide the keyboard until user requests it
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// set default values from preference file. necessary for first run
		PreferenceManager.setDefaultValues(this, R.xml.simple_prefs_layout, false);

		// set valueHandler values from preference handler
		ph.setValuesFromPreferences(this);

		// instantiate gui objects
		setupGuiInstances();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_about:
			Intent about = new Intent(this, AboutActivity.class);
			startActivity(about);
			return true;
		case R.id.action_settings:
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);
			return true;
		case R.id.action_deduction_list:
			DialogFragment newFragment = new DeductionListDialog();
			newFragment.show(getFragmentManager(), "dialog");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// check if premium version purchased
		if (!premium_purchased) {
			Log.d(TAG, "Checking premium_purchased...");

			premium_purchased = ph.getBoolPreference("premium_purchased", this);
			Log.d(TAG, "Is premium purchased: " + premium_purchased);
			if (premium_purchased) {
				Toast.makeText(this, "Premium version purchased", Toast.LENGTH_LONG).show();
				premium_purchased = true;
			}
		}

		ph.setValuesFromPreferences(this); // read values from preferences
		setupGuiLayout(); // setup simple or advanced layout
		setupButtons(); // setup buttons to receive click events
		refreshGui(); // set gui objects from values
	}

	@Override
	public void onPause() {
		super.onPause();
		ph.saveValuesToPreferences(this); // save values
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ph.saveValuesToPreferences(this); // save values
	}

	public void setupGuiInstances() {
		// gui display objects
		base_pay_total = (TextView) findViewById(R.id.base_pay_total);
		gross_pay_total = (TextView) findViewById(R.id.gross_pay_total);
		taxes_total = (TextView) findViewById(R.id.taxes_total);
		deposited_total = (TextView) findViewById(R.id.deposited_total);

		// gui entry objects
		base_pay_rate = (EditText) findViewById(R.id.base_pay_rate);
		overtime1_rate = (EditText) findViewById(R.id.overtime1_rate);
		overtime2_rate = (EditText) findViewById(R.id.overtime2_rate);
		years_worked = (EditText) findViewById(R.id.years_worked);

		// gui button objects
		holidays_button = (Button) findViewById(R.id.holidays_button);
		overtime_button = (Button) findViewById(R.id.overtime_button);
		scheduled_days_button = (Button) findViewById(R.id.scheduled_days_button);
		calculate_button = (Button) findViewById(R.id.calculate_button);
		radio_pay_group = (RadioGroup) findViewById(R.id.radioGroup1);

		// gui simple & advanced layout
		rank_label = (TextView) findViewById(R.id.rank_label);
		simple_layout_container = (LinearLayout) findViewById(R.id.simple_layout_container);
		advanced_layout_container = (LinearLayout) findViewById(R.id.advanced_layout_container);
	}

	public void setupGuiLayout() {
		// get preference settings
		boolean isAdvancedLayout = ph.preferenceSet("pref_advanced_layout", this);
		String current_rank_label = ph.getPreferences("pref_rank", this);

		// change layout based upon preferences
		if (!isAdvancedLayout) {
			// SIMPLE LAYOUT
			simple_layout_container.setVisibility(View.VISIBLE);
			rank_label.setText("Current rank: " + current_rank_label);
			advanced_layout_container.setVisibility(View.GONE);

			vh.setupSimpleValues(Integer.parseInt(ph.getPreferences("current_rank_int", this))); // setup gui objects
																									// from rank values
																									// in valuehandler
			vh.setYears_worked(ph.getIntPreference("pref_years_of_service", this)); // set years worked based upon value
																					// in settings

		} else {
			// ADVANCED LAYOUT
			simple_layout_container.setVisibility(View.GONE);
			advanced_layout_container.setVisibility(View.VISIBLE);
			vh.setupSimpleValues(0); // set values back to probationary firefighter
		}
	}

	public void readGuiIntoValues() {
		if (guiValuesFull()) {
			// reread the gui inputs into values right before
			// calculating to make sure that we accept any user changes made
			vh.setBase_pay_rate(vm.editToDouble(base_pay_rate));
			vh.setOvertime1_pay_rate(vm.editToDouble(overtime1_rate));
			vh.setOvertime2_pay_rate(vm.editToDouble(overtime2_rate));

			vh.setScheduled_days(vm.buttToInt(scheduled_days_button));
			vh.setHolidays_during_pay(vm.buttToInt(holidays_button));
			vh.setOvertime_hours(vm.buttToDouble(overtime_button));

			vh.setYears_worked(vm.editToInt(years_worked));
		}
	}

	public void refreshGui() {
		// refresh totals
		base_pay_total.setText(df_totals.format(vh.getBase_pay_total()));
		gross_pay_total.setText(df_totals.format(vh.getGross_pay_total()));
		taxes_total.setText(df_totals.format(vh.getTaxes_total()));
		deposited_total.setText(df_totals.format(vh.getDeposit_total()));

		// refresh specific values
		base_pay_rate.setText(df_rates.format(vh.getBase_pay_rate()));
		overtime1_rate.setText(df_rates.format(vh.getOvertime1_pay_rate()));
		overtime2_rate.setText(df_rates.format(vh.getOvertime2_pay_rate()));
		years_worked.setText(vm.intToString(vh.getYears_worked()));

		// refresh buttons
		holidays_button.setText("Holidays: " + vh.getHolidays_during_pay());
		overtime_button.setText("Overtime: " + vh.getCallback_hours() + " hrs");
		scheduled_days_button.setText("Scheduled Days: " + vh.getScheduled_days());
	}

	// check to make sure gui edittext objects have value. calc will FC if they do not
	public boolean guiValuesFull() {
		EditText[] gui_values = { base_pay_rate, overtime1_rate, overtime2_rate, years_worked }; // edittext values
		Button[] gui_button = { scheduled_days_button, holidays_button, overtime_button }; // button values
		for (EditText gvn : gui_values) {
			if (gvn.getText().toString().equals("")) {
				// create a toast, set the toast's textview object font to red & display
				Toast emptygvn = Toast.makeText(this, "Empty values. Resetting to previous", Toast.LENGTH_LONG);
				TextView thisToast = (TextView) emptygvn.getView().findViewById(android.R.id.message);
				thisToast.setTextColor(Color.RED);
				emptygvn.show();

				// also set the empty edittext font to red
				gvn.setTextColor(Color.RED);
				return false; // epic fail
			}
			// set value to white. useful if value was previously set to red
			gvn.setTextColor(Color.WHITE);
		}

		for (Button gbn : gui_button) {
			if (gbn.getText().toString().equals("")) {
				// create a toast, set the toast's textview object font to red & display
				Toast emptygbn = Toast.makeText(this, "Empty values. Resetting to previous", Toast.LENGTH_LONG);
				TextView thisToast = (TextView) emptygbn.getView().findViewById(android.R.id.message);
				thisToast.setTextColor(Color.RED);
				emptygbn.show();

				// also set the empty button font to red
				gbn.setTextColor(Color.RED);
				return false; // epic fail
			}
			// set the values to white. useful if thevalue was previously set to red
			gbn.setTextColor(Color.WHITE);
		}

		return true;
	}

	// setup buttons
	public void setupButtons() {
		holidayButtonClick();
		overtimeButtonClick();
		scheduledDaysButtonClick();
		calcButtonClick();
	}

	public void holidayButtonClick() {
		holidays_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				readGuiIntoValues();

				// create a new dialog handler
				DialogFragment newFragment = new DialogHandler();

				// create a bundle housing a key letting the dialog handler
				// know which button was pressed
				Bundle args = new Bundle();
				args.putInt("key", 0);
				newFragment.setArguments(args);

				newFragment.show(getFragmentManager(), "holidays");
			}
		});
	}

	public void overtimeButtonClick() {
		overtime_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				readGuiIntoValues();

				DialogFragment newFragment = new DialogHandler();

				// create a bundle housing a key letting the dialog handler
				// know which button was pressed
				Bundle args = new Bundle();
				args.putInt("key", 1);
				newFragment.setArguments(args);

				newFragment.show(getFragmentManager(), "overtime");
			}
		});
	}

	public void scheduledDaysButtonClick() {
		scheduled_days_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				readGuiIntoValues();

				DialogFragment newFragment = new DialogHandler();

				// create a bundle housing a key letting the dialog handler
				// know which button was pressed
				Bundle args = new Bundle();
				args.putInt("key", 2);
				newFragment.setArguments(args);

				newFragment.show(getFragmentManager(), "scheduled");
			}
		});
	}

	public void calcButtonClick() {
		calculate_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				readGuiIntoValues();

				// create a new calculator engine object
				CalcEngine cEngine = new CalcEngine();
				cEngine.calculateBase();

				// calculate gross based upon radio button index
				switch (radio_pay_group.indexOfChild(findViewById(radio_pay_group.getCheckedRadioButtonId()))) {
				case 0:
					cEngine.calculateGross(radio_pay_group.indexOfChild(findViewById(radio_pay_group
							.getCheckedRadioButtonId())));
					Toast.makeText(MainActivity.this, R.string.toast_1st_payday, Toast.LENGTH_SHORT).show();
					break;
				case 1:
					cEngine.calculateGross(radio_pay_group.indexOfChild(findViewById(radio_pay_group
							.getCheckedRadioButtonId())));
					Toast.makeText(MainActivity.this, R.string.toast_2nd_payday, Toast.LENGTH_SHORT).show();
					break;
				case 2:
					cEngine.calculateGross(radio_pay_group.indexOfChild(findViewById(radio_pay_group
							.getCheckedRadioButtonId())));
					Toast.makeText(MainActivity.this, R.string.toast_3rd_payday, Toast.LENGTH_SHORT).show();
					break;
				}

				// calculate taxes & deposit amount (with deductions)
				cEngine.calculateTaxes(vh.getGross_pay_total()); // simple taxes / not fully implemented
				cEngine.calculateDeposit(vh.getGross_pay_total() / 3);
				refreshGui();
			}
		});
	}
}
