package com.corridor9design.mfdpaycalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";

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
