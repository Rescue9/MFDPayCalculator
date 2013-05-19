package com.corridor9design.mfdpaycalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";
	public static final String ADVANCED_LAYOUT = "pref_advanced_layout";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load the preferences fragment
		addPreferencesFromResource(R.xml.prefs_layout);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(CURRENT_RANK_LISTPREFERENCE)) {
			Preference rank_list = findPreference(key);
			rank_list.setSummary(sharedPreferences.getString(key, ""));
		}
		if (key.equals(ADVANCED_LAYOUT)){
			// find layouts
			int advanced_layout = R.id.advanced_layout_container;
			int simple_layout = R.id.simple_layout_container;
			
			//get rank preference
			String current_rank_title = sharedPreferences.getString("pref_rank", "");
			
			System.out.println("ADVANCED_RANK");
		}
		
	}

	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}		
}
