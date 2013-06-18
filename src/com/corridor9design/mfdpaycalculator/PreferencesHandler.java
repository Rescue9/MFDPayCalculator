/**
 * Program: PreferencesHandler.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a handler class for getting & setting values.
 */

package com.corridor9design.mfdpaycalculator;

import com.corridor9design.mfdpaycalculator.engine.ValuesHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHandler {

	ValuesHandler vh = new ValuesHandler();

	public PreferencesHandler() {
	}

	// set some preference as passed to method
	public void setPreferences(String key, String value, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString(key, value);
		editor.commit();
	}

	// get preference of type String
	public String getPreferences(String key, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, "0");
	}

	// get preference of type Double
	public double getDoublePreference(String key, Context context) {
		double pref = Double.parseDouble(getPreferences(key, context));
		return pref;

	}

	// get preference of type Integer
	public int getIntPreference(String key, Context context) {
		int pref = Integer.parseInt(getPreferences(key, context));
		return pref;
	}

	// get preference of type Boolean
	public boolean getBoolPreference(String key, Context context) {
		boolean pref = Boolean.parseBoolean(getPreferences(key, context));
		return pref;
	}

	// check to see if a preference is set
	public boolean preferenceSet(String key, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(key, false);
	}

	// set values from their respective shared preference saves
	public void setValuesFromPreferences(Context context) {
		// set values for totals from previous run
		vh.setBase_pay_total(this.getDoublePreference("base_pay_total", context));
		vh.setGross_pay_total(this.getDoublePreference("gross_pay_total", context));
		vh.setTaxes_total(this.getDoublePreference("taxes_total", context));
		vh.setDeposit_total(this.getDoublePreference("deposit_total", context));

		// set values for hourly calculations
		vh.setBase_pay_rate(this.getDoublePreference("base_pay_rate", context));
		vh.setOvertime1_pay_rate(this.getDoublePreference("overtime1_pay_rate", context));
		vh.setOvertime2_pay_rate(this.getDoublePreference("overtime2_pay_rate", context));
		vh.setScheduled_days(this.getIntPreference("scheduled_days", context));

		// additional values needed for calculation
		vh.setOvertime_hours(this.getDoublePreference("callback_hours", context));
		vh.setYears_worked(this.getIntPreference("years_worked", context));
		vh.setHolidays_during_pay(this.getIntPreference("holidays_worked", context));
	}

	// save values to their respective shared preference saves
	public void saveValuesToPreferences(Context context) {
		// save values in total fields
		setPreferences("base_pay_total", vh.getBase_pay_total() + "", context);
		setPreferences("gross_pay_total", vh.getGross_pay_total() + "", context);
		setPreferences("taxes_total", vh.getTaxes_total() + "", context);
		setPreferences("deposit_total", vh.getDeposit_total() + "", context);

		// save values for hourly calculations
		setPreferences("base_pay_rate", vh.getBase_pay_rate() + "", context);
		setPreferences("overtime1_pay_rate", vh.getOvertime1_pay_rate() + "", context);
		setPreferences("overtime2_pay_rate", vh.getOvertime2_pay_rate() + "", context);
		setPreferences("scheduled_days", vh.getScheduled_days() + "", context);

		// save additional values
		setPreferences("callback_hours", vh.getCallback_hours() + "", context);
		setPreferences("years_worked", vh.getYears_worked() + "", context);
		setPreferences("holidays_worked", vh.getHolidays_during_pay() + "", context);
	}
}
