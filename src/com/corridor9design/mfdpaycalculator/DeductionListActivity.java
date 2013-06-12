package com.corridor9design.mfdpaycalculator;

import java.util.List;

import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.MyDeductionDbHelper;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DeductionListActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deduction_list);
		
		MyDeductionDbHelper db = new MyDeductionDbHelper(this);
		List<Deduction> deductions = db.getAllDeductions();
		System.out.println(deductions);
		
		// using simpleCursorAdapter to show all elements in a listView
		ArrayAdapter<Deduction> adapter = new ArrayAdapter<Deduction>(this, android.R.layout.simple_list_item_1, deductions);
		setListAdapter(adapter);		
	}
	
	
}
