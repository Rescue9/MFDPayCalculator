package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHandler extends Activity{
	
	ValuesHandler vhandler = new ValuesHandler();
	

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
		vhandler.setBase_pay_total(this.getDoublePreference("base_pay_final", context));
		vhandler.setGross_pay_total(this.getDoublePreference("gross_pay_final", context));
		vhandler.setTaxes_total(this.getDoublePreference("taxes_final", context));
		vhandler.setDeposit_total(this.getDoublePreference("deposit_final", context));
		
		// set values for hourly calculations
		
		vhandler.setBase_pay_rate(this.getDoublePreference("base_pay_rate", context));
		vhandler.setOvertime1_pay_rate(this.getDoublePreference("overtime1_pay_rate", context));
		vhandler.setOvertime2_pay_rate(this.getDoublePreference("overtime2_pay_rate", context));
		vhandler.setScheduled_days(this.getIntPreference("scheduled_days", context));
		
		// additional values needed for calculation
		vhandler.setOvertime_hours(this.getDoublePreference("callback_hours", context));
		vhandler.setYears_worked(this.getIntPreference("years_worked", context));
		vhandler.setHolidays_during_pay(this.getIntPreference("holidays_worked", context));
	}
}
