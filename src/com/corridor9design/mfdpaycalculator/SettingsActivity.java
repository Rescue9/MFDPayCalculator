package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// display the preferences fragment
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
