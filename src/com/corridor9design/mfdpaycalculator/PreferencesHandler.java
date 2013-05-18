package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHandler extends Activity{
	
	ValuesHandler vh = new ValuesHandler();
	

	public PreferencesHandler() {
	}
	
	public void setPreferences(String key, String value, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getPreferences(String key, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, "0");
	}
	
	public double getDoublePreference(String key, Context context){
		double pref = Double.parseDouble(getPreferences(key, context));
		return pref;
		
	}
	
	public int getIntPreference(String key, Context context){
		int pref = Integer.parseInt(getPreferences(key, context));
		return pref;
	}
	
	public boolean getBoolPreference(String key, Context context){
		boolean pref = Boolean.parseBoolean(getPreferences(key, context));
		return pref;
	}
	
	public void setValuesFromPreferences(Context context){
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
	
	public void saveValuesToPreferences(Context context){
		setPreferences("base_pay_total", vh.getBase_pay_total()+"", context);
	}
}
