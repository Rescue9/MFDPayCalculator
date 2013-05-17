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
		String wholeString = bt.getText().toString();
		String numbOnly = wholeString.replaceAll("[^0-9]", "");
		double value = Double.parseDouble(numbOnly);
		return value;
	}
	
	public int buttToInt(Button bt){
		String wholeString = bt.getText().toString();
		String numbOnly = wholeString.replaceAll("[^0-9]", "");
		int value = Integer.parseInt(numbOnly);
		return value;
	}
	
	public String doubleToString(double db){
		String value = db +"";
		return value;
	}
	
	public String intToString(int nt){
		String value = nt +"";
		return value;
	}

}
