/**
 * Program: SettingsFragment.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a fragment holding application settings.
 */

package com.corridor9design.mfdpaycalculator.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.corridor9design.mfdpaycalculator.R;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";
	public static final String ADVANCED_LAYOUT = "pref_advanced_layout";
	public static final String CURRENT_RANK_INT = "current_rank_int";
	public static final String YEARS_OF_SERVICE = "pref_years_of_service";
	public static final String MARITAL_STATUS = "pref_marital_status";
	public static final String EXEMPTIONS = "pref_exemptions";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// add the preferences to the fragment from the xml layout
		addPreferencesFromResource(R.xml.simple_prefs_layout);

		// create preference object so we can set the summary
		Preference pref_years_of_service = findPreference(YEARS_OF_SERVICE);
		pref_years_of_service.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getString(YEARS_OF_SERVICE, "0"));

		// update the exemptions summary on the settings page
		Preference exemptions = findPreference(EXEMPTIONS);
		exemptions.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
				EXEMPTIONS, "0"));

		// setup checkbox summary
		CheckBoxPreference advanced_layout = (CheckBoxPreference) findPreference(ADVANCED_LAYOUT);
		advanced_layout
				.setSummary(sharedPreferences.getBoolean(ADVANCED_LAYOUT, false) ? "Disable to use standard pay rates"
						: "Enable to use your specific pay rates");
	}

	// create a preference change listener to update information on change.
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// create a shared preferences editor
		SharedPreferences.Editor editor = sharedPreferences.edit();

		String current_rank_label = sharedPreferences.getString(CURRENT_RANK_LISTPREFERENCE, "");

		// set advanced layout checkbox summary
		CheckBoxPreference advanced_layout = (CheckBoxPreference) findPreference(ADVANCED_LAYOUT);

		// get the current years of service string
		String years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0");

		// add
		if (key.equals(ADVANCED_LAYOUT)) {
			advanced_layout.setSummary(sharedPreferences.getBoolean(key, false) ? "Disable to use standard pay rates"
					: "Enable to use your specific pay rates");
		}
		// if the preference changed is the current rank...
		if (key.equals(CURRENT_RANK_LISTPREFERENCE)) {
			// get the new rank value
			ListPreference current_rank = (ListPreference) findPreference(key);
			// set the summary to the new rank value
			current_rank.setSummary(current_rank.getEntry());
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

		// if the key changed is the marital status...
		if (key.equals(MARITAL_STATUS)) {
			// get the new value & set the status from that
			ListPreference marital_status = (ListPreference) findPreference(key);
			marital_status.setSummary(marital_status.getEntry());

		}

		// if the key changed is the exemptions...
		if (key.equals(EXEMPTIONS)) {
			// check to see if it is null
			if (EXEMPTIONS.matches("")) {
				// if null, set to 0 & commit back to preferences
				editor.putString(key, "0");
				editor.apply();
			}

			// update the exemptions summary on the settings page
			Preference exemptions = findPreference(key);
			exemptions.setSummary(sharedPreferences.getString(EXEMPTIONS, "0"));
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
