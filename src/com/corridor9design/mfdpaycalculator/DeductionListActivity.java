package com.corridor9design.mfdpaycalculator;

import com.corridor9design.mfdpaycalculator.database.MyDeductionDbHelper;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DeductionListActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deduction_list);

		MyDeductionDbHelper db = new MyDeductionDbHelper(this);
		
		String[] columns = new String[] { "name", "amount" };
		int[] to = new int[] { R.id.deduction_listing_deduction_name, R.id.deduction_listing_deduction_amount };
		
		Cursor cursor = db.getDeductionList();

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.deduction_listings,
				cursor, columns, to, 0);

		ListView listview = (ListView) findViewById(android.R.id.list);
		listview.setAdapter(adapter);
		
	}

}
