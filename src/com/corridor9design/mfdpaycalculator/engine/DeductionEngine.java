/**
 * Program: DeductionEngine.java
 * Programmer: Andrew Buskov
 * Date: Jun 26, 2013
 * Purpose: To handle all aspects relating to the calculation
 *  of deductions for use by the CalcEgnine.
 */

package com.corridor9design.mfdpaycalculator.engine;

import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class DeductionEngine {

	// set the projection (what we're wanting from the database) to pass to the method
	private static final String[] PROJECTION = new String[] { "_id", "amount", "number", "payday1", "payday2",
			"payday3" };

	// create an array to store out individual deductions
	double[] single_deduction;

	public double returnDeductionTotal(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(DeductionContentProvider.CONTENT_URI, PROJECTION, null, null, null);

		double deductions_total = 0.0;

		if (cursor.moveToFirst()) {
			do {
				deductions_total += Double.parseDouble(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		System.out.println("Total deductions = " + deductions_total);

		return deductions_total;
	}

	public double returnPreTaxDeductions(Context context) {
		// from what column do we want to match and what arguments do we want to match
		String selection = Deduction.COLUMN_NUMBER + " in (?,?,?,?,?,?,?,?)";
		String[] selection_args = new String[] { "1", "2", "12", "13", "14", "17", "28", "30" };

		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(DeductionContentProvider.CONTENT_URI, PROJECTION, selection, selection_args,
				null);

		// fetch our deductions
		double pre_tax_deduction_total = 0.0;

		if (cursor.moveToFirst()) {
			do {
				pre_tax_deduction_total += Double.parseDouble(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		System.out.println("PreTax deductions = " + pre_tax_deduction_total);
		return pre_tax_deduction_total;
	}
}
