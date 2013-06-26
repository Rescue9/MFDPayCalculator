/**
 * Program: DeductionEngine.java
 * Programmer: Andrew Buskov
 * Date: Jun 26, 2013
 * Purpose: To handle all aspects relating to the calculation
 *  of deductions for use by the CalcEgnine.
 */

package com.corridor9design.mfdpaycalculator.engine;

import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class DeductionEngine {

	// set the projection (what we're wanting from the database) to pass to the method
	private static final String[] PROJECTION = new String[] { "_id", "amount" };

	// create an array to store out individual deductions
	double[] single_deduction;
	int row_count;

	public void fetchDeductions(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(DeductionContentProvider.CONTENT_URI, PROJECTION, null, null, null);

		row_count = cursor.getCount();

		single_deduction = new double[row_count];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				single_deduction[i] = Double.parseDouble(cursor.getString(1));
				i++;
			} while (cursor.moveToNext());
		}
	}

	public double returnDeductionTotal(Context context) {

		fetchDeductions(context);
		double deductions_total = 0.0;

		for (int i = 0; i < row_count; i++) {
			deductions_total += single_deduction[i];
		}
		
		System.out.println("Total deductions = " + deductions_total);
		
		return deductions_total;
	}
}
