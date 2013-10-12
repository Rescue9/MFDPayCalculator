/**
 * Program: SettingsActivity.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create an activity for launching the settings menus.
 *  This will be more helpful as we get more into fragments on tablets.
 */

package com.corridor9design.mfdpaycalculator.preferences;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// display the preferences fragment
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
