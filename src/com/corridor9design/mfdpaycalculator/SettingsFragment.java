/**
 * Program: SettingsFragment.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a fragment holding application settings.
 */

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

		// add the preferences to the fragment from the xml layout
		addPreferencesFromResource(R.xml.simple_prefs_layout);

		// create preference object so we can set the summary
		Preference pref_years_of_service = findPreference(YEARS_OF_SERVICE);
		pref_years_of_service.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getString(YEARS_OF_SERVICE, "0"));
	}

	// create a preference change listener to update information on change.
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		SharedPreferences.Editor editor = sharedPreferences.edit(); // create a shared preferences editor

		ListPreference rank_listpreference = (ListPreference) findPreference(CURRENT_RANK_LISTPREFERENCE);
		String current_rank_label = sharedPreferences.getString(CURRENT_RANK_LISTPREFERENCE, ""); // get the current
																									// rank string
		String years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0"); // get the current years of
																						// service string

		// if the preference changed is the current rank...
		if (key.equals(CURRENT_RANK_LISTPREFERENCE)) {
			// get the new rank value
			Preference rank_list = findPreference(key);
			// set the summary to the new rank value
			rank_list.setSummary(sharedPreferences.getString(key, ""));
			// set the rank index to the new rank integer
			int rank_index = rank_listpreference.findIndexOfValue(current_rank_label);

			// write this back into shared preferences
			editor.putString(CURRENT_RANK_INT, rank_index + "");
			editor.commit();

		}

		// if the key changed is the layout...
		if (key.equals(ADVANCED_LAYOUT)) {
			// get new rank & layout preference
			Boolean advanced_layout_current = sharedPreferences.getBoolean(ADVANCED_LAYOUT, false);

			// log layout preference & rank
			Log.d("AdvancedLayout: " + advanced_layout_current, current_rank_label + "");
		}

		// if the key changed is the years of service...
		if (key.equals(YEARS_OF_SERVICE)) {
			// check to see if years of service is null
			if (years_of_service.matches("")) {
				// if null, set years of service to 0 & commit back to preferences
				editor.putString(key, "0");
				editor.apply();
			}

			// update years of service summary on settings page
			Preference pref_years_of_service = findPreference(key);
			pref_years_of_service.setSummary(sharedPreferences.getString(YEARS_OF_SERVICE, "0"));

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
