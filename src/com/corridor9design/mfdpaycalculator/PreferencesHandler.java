package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHandler extends Activity{
	

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

}
