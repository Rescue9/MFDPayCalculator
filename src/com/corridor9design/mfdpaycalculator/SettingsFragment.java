package com.corridor9design.mfdpaycalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String CURRENT_RANK_LISTPREFERENCE = "pref_rank";
	public static final String ADVANCED_LAYOUT = "pref_advanced_layout";
	public static final String CURRENT_RANK_INT = "current_rank_int";
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load the preferences fragment
		addPreferencesFromResource(R.xml.prefs_layout);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		ListPreference rank_listpreference = (ListPreference) findPreference(CURRENT_RANK_LISTPREFERENCE);
		String current_rank_label = sharedPreferences.getString(CURRENT_RANK_LISTPREFERENCE, "");

		
		if (key.equals(CURRENT_RANK_LISTPREFERENCE)) {
			Preference rank_list = findPreference(key);
			rank_list.setSummary(sharedPreferences.getString(key, ""));
			int rank_index = rank_listpreference.findIndexOfValue(current_rank_label);
			
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(CURRENT_RANK_INT, rank_index+"");
			editor.commit();
			
		}
		if (key.equals(ADVANCED_LAYOUT)){
			//get rank & layout preference
			Boolean advanced_layout_current = sharedPreferences.getBoolean(ADVANCED_LAYOUT, false);
			
			// print layout preference & rank
			Log.d("AdvancedLayout: " + advanced_layout_current, current_rank_label + "");
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
