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
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.corridor9design.mfdpaycalculator.BuildConfig;
import com.corridor9design.mfdpaycalculator.R;

import java.text.DecimalFormat;

public class SettingsFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {

    public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";
    public static final String ADVANCED_LAYOUT = "pref_advanced_layout";
    public static final String CURRENT_RANK_INT = "current_rank_int";
    public static final String YEARS_OF_SERVICE = "pref_years_of_service";
    public static final String MARITAL_STATUS = "pref_marital_status";
    public static final String EXEMPTIONS = "pref_exemptions";
    public static final String ADDITIONAL_FEDERAL = "pref_additional_withholding_federal";
    public static final String ADDITIONAL_STATE = "pref_additional_withholding_state";

    DecimalFormat df = new DecimalFormat("$##0.00");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add the preferences to the fragment from the xml layout
        addPreferencesFromResource(R.xml.simple_prefs_layout);
        addPreferencesFromResource(R.xml.simple_prefs_tax_info);
        addPreferencesFromResource(R.xml.simple_prefs_personal_info);

        // set current rank summary to prob ff/1 on initial
        ListPreference rank_listpreference = (ListPreference) findPreference(CURRENT_RANK_LISTPREFERENCE);
        rank_listpreference.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(CURRENT_RANK_LISTPREFERENCE, "Probationary Firefighter 1"));

        // create preference object so we can set the summary
        Preference pref_years_of_service = findPreference(YEARS_OF_SERVICE);
        pref_years_of_service.setSummary((String) PreferenceManager.getDefaultSharedPreferences(
                getActivity())
                .getString(YEARS_OF_SERVICE, "0"));

        // update the exemptions summary on the settings page
        Preference exemptions = findPreference(EXEMPTIONS);
        exemptions.setSummary((String) PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(EXEMPTIONS, "0"));

        // update the additional federal summary on the settings page
        Preference additional_federal = findPreference(ADDITIONAL_FEDERAL);
        additional_federal.setSummary(df.format(Double.parseDouble((String) PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(ADDITIONAL_FEDERAL, "0"))));

        // update the additional state summary on the settings page
        Preference additional_state = findPreference(ADDITIONAL_STATE);
        additional_state.setSummary(df.format(Double.parseDouble((String) PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(ADDITIONAL_STATE, "0"))));

    }

    // create a preference change listener to update information on change.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // create a shared preferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ListPreference rank_listpreference = (ListPreference) findPreference(CURRENT_RANK_LISTPREFERENCE);

        // get the current rank string
        String current_rank_label = sharedPreferences.getString(CURRENT_RANK_LISTPREFERENCE, "");

        // get the current years of service string
        String years_of_service = sharedPreferences.getString(YEARS_OF_SERVICE, "0");

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
            if (BuildConfig.DEBUG)
                Log.d("AdvancedLayout: " + advanced_layout_current, current_rank_label + "");
            
            //TODO Toast this as broken currently!
            Toast.makeText(getActivity(), "THIS IS CURRENTLY BROKEN", Toast.LENGTH_LONG).show();
        }

        // if the key changed is the years of service...
        if (key.equals(YEARS_OF_SERVICE)) {
            // check to see if years of service is null
            if (years_of_service.matches("")) {
                // if null, set years of service to 0 & commit back to
                // preferences
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

        // if the key changed is the additional federal withholding...
        if (key.equals(ADDITIONAL_FEDERAL)) {
            // check to see if it is null
            if (ADDITIONAL_FEDERAL.matches("")) {
                // if null, set to 0 & commit back to preferences
                editor.putString(key, "0");
                editor.apply();
            }

            // update the additional federal summary on the settings page
            Preference additional_federal = findPreference(key);
            additional_federal.setSummary(df.format(Double.parseDouble(sharedPreferences.getString(ADDITIONAL_FEDERAL, "0"))));
        }

        // if the key changed is the exemptions...
        if (key.equals(ADDITIONAL_STATE)) {
            // check to see if it is null
            if (ADDITIONAL_STATE.matches("")) {
                // if null, set to 0 & commit back to preferences
                editor.putString(key, "0");
                editor.apply();
            }

            // update the additional state summary on the settings page
            Preference additional_state = findPreference(key);
            additional_state.setSummary(df.format(Double.parseDouble(sharedPreferences.getString(ADDITIONAL_STATE, "0"))));
        }

    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
    }
}
