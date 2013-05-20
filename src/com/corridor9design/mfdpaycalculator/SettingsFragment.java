package com.corridor9design.mfdpaycalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";
	public static final String ADVANCED_LAYOUT = "pref_advanced_layout";
	public static final String CURRENT_RANK_INT = "current_rank_int";
	public static final String YEARS_OF_SERVICE = "pref_years_of_service";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load the preferences fragment
		addPreferencesFromResource(R.xml.prefs_layout);

		Preference pref_years_of_service = findPreference(YEARS_OF_SERVICE);
		pref_years_of_service.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getString("pref_years_of_service", "0"));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		SharedPreferences.Editor editor = sharedPreferences.edit();

		ListPreference rank_listpreference = (ListPreference) findPreference(CURRENT_RANK_LISTPREFERENCE);
		String current_rank_label = sharedPreferences.getString(CURRENT_RANK_LISTPREFERENCE, "");
		String years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0");

		if (key.equals(CURRENT_RANK_LISTPREFERENCE)) {
			Preference rank_list = findPreference(key);
			rank_list.setSummary(sharedPreferences.getString(key, ""));
			int rank_index = rank_listpreference.findIndexOfValue(current_rank_label);

			editor.putString(CURRENT_RANK_INT, rank_index + "");
			editor.commit();

		}

		if (key.equals(ADVANCED_LAYOUT)) {
			// get rank & layout preference
			Boolean advanced_layout_current = sharedPreferences.getBoolean(ADVANCED_LAYOUT, false);

			// print layout preference & rank
			Log.d("AdvancedLayout: " + advanced_layout_current, current_rank_label + "");
		}

		if (key.equals(YEARS_OF_SERVICE)) {
			// get original value of years of service
			String new_years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0");

			// check to see if years of service is null
			if (years_of_service.matches("")) {
				// if null, set years of service to 0 & commit back to preferences
				editor.putString(key, "0");
				editor.apply();

				// set years of service string from preferences again
				new_years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0");
			}

			// update years of service summary on settings page
			Preference pref_years_of_service = findPreference(key);
			pref_years_of_service.setSummary(new_years_of_service);

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
