/**
 * Program: ValueModifier.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a modifier class for converting values
 *  from one type to another.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.widget.Button;
import android.widget.EditText;

public class ValueModifier {

	// used to convert double, bool, int, String, and other values as necessary

	public double editToDouble(EditText et) {
		double value = Double.parseDouble(et.getText().toString());
		return value;
	}

	public int editToInt(EditText et) {
		int value = Integer.parseInt(et.getText().toString());
		return value;
	}

	public double buttToDouble(Button bt) {
		// get the whole string, but replace everything that isn't a number
		String wholeString = bt.getText().toString();
		String numbOnly = wholeString.replaceAll("[^0-9.]+", "");
		double value = Double.parseDouble(numbOnly);
		return value;
	}

	public int buttToInt(Button bt) {
		// get the whole string, but replace everything that isn't a number
		String wholeString = bt.getText().toString();
		String numbOnly = wholeString.replaceAll("[^0-9]", "");
		int value = Integer.parseInt(numbOnly);
		return value;
	}

	public String doubleToString(double db) {
		String value = db + "";
		return value;
	}

	public String intToString(int nt) {
		String value = nt + "";
		return value;
	}

}
