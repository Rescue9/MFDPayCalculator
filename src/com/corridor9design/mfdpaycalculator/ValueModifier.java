package com.corridor9design.mfdpaycalculator;

import android.widget.Button;
import android.widget.EditText;

public class ValueModifier {
	
	// used to convert double, bool, int, String, and other values as necessary

	public ValueModifier() {
		// TODO Auto-generated constructor stub
	}
	
	public double editToDouble(EditText et){
		double value = Double.parseDouble(et.getText().toString());
		return value;
	}
	
	public int editToInt(EditText et){
		int value = Integer.parseInt(et.getText().toString());
		return value;
	}
	
	public double buttToDouble(Button bt){
		double value = Double.parseDouble(bt.getText().toString());
		return value;
	}
	
	public int buttToInt(Button bt){
		int value = Integer.parseInt(bt.getText().toString());
		return value;
	}

}
