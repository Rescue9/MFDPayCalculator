package com.corridor9design.mfdpaycalculator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DeductionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deduction_editor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
		// Inflate the menu; this adds items to the action bar if it is present.
	}

}
