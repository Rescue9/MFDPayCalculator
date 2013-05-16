package com.corridor9design.mfdpaycalculator;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

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

	PreferencesHandler ph = new PreferencesHandler();
	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();

	DecimalFormat df = new DecimalFormat("$##0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// hide the keyboard until user requests it
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// setup gui instances
		setupGuiInstances();

		// set valueHandler values from preferences
		ph.setValuesFromPreferences(this);

		testButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void setupGuiInstances() {
		// gui display elements
		base_pay_total = (TextView) findViewById(R.id.base_pay_total);
		gross_pay_total = (TextView) findViewById(R.id.gross_pay_total);
		taxes_total = (TextView) findViewById(R.id.taxes_total);
		deposited_total = (TextView) findViewById(R.id.deposited_total);
		rank_label = (TextView) findViewById(R.id.rank_label);

		// gui entry elements
		base_pay_rate = (EditText) findViewById(R.id.base_pay_rate);
		overtime1_rate = (EditText) findViewById(R.id.overtime1_rate);
		overtime2_rate = (EditText) findViewById(R.id.overtime2_rate);
		years_worked = (EditText) findViewById(R.id.years_worked);

		// gui button elements
		holidays_button = (Button) findViewById(R.id.holidays_button);
		overtime_button = (Button) findViewById(R.id.overtime_button);
		scheduled_days_button = (Button) findViewById(R.id.scheduled_days_button);
		calculate_button = (Button) findViewById(R.id.calculate_button);
	}

	public void readGuiIntoValues() {
		// we need to reread the gui inputs into values right before
		// calculating to make sure that we accept any user changes made
		vh.setBase_pay_rate(vm.editToDouble(base_pay_rate));
		vh.setOvertime1_pay_rate(vm.editToDouble(overtime1_rate));
		vh.setOvertime2_pay_rate(vm.editToDouble(overtime2_rate));

		vh.setScheduled_days(vm.buttToInt(scheduled_days_button));
		vh.setHolidays_during_pay(vm.buttToInt(holidays_button));
		vh.setCallback_hours(vm.buttToDouble(overtime_button));

		vh.setYears_worked(vm.editToInt(years_worked));
	}

	public void testButton() {
		holidays_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				base_pay_total.setText("TESTING");

			}
		});
	}
}
