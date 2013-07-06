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

import java.util.Arrays;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class DeductionEngine {
	
	ValuesHandler vh = new ValuesHandler();

	// set the projection (what we're wanting from the database) to pass to the method
	private static final String[] PROJECTION = new String[] { "_id", "amount", "number", "payday1", "payday2",
			"payday3" };
	
	// set the standard CERS deduction for Hazardous Duty
	private static final double CERS_MULTIPLIER = 0.08;

	// create an array to store out individual deductions
	double[] single_deduction;

	public double returnDeductionTotal(Context context, int payday) {

		double deductions_total = returnPostTaxDeductions(context, payday) + returnPreTaxDeductions(context, payday);

		System.out.println("Calculating for #" + (payday + 1) + " payday of month."); // payday + 1 because payday
																						// starts at 0
		System.out.println("Total deductions = " + deductions_total);

		return deductions_total;
	}

	public double returnPostTaxDeductions(Context context, int payday) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(DeductionContentProvider.CONTENT_URI, PROJECTION, null, null, null);

		String[] selection_args = new String[] { "1", "2", "12", "13", "14", "17", "28", "30" };

		double post_tax_deductions = 0.0;

		if (cursor.moveToFirst()) {
			do {
				// set payday+3 due to payday radio button being 0,1,2 where
				// cursor columns for payday are 3,4,5
				if (cursor.getString(payday + 3).equals("true")
						&& !(Arrays.asList(selection_args).contains(cursor.getString(2)))) {

					post_tax_deductions += cursor.getDouble(1);
				}
			} while (cursor.moveToNext());
		}
		System.out.println("PostTax deductions = " + post_tax_deductions);
		return post_tax_deductions;
	}

	public double returnPreTaxDeductions(Context context, int payday) {
		// from what column do we want to match and what arguments do we want to match
		String selection = Deduction.COLUMN_NUMBER + " in (?,?,?,?,?,?,?,?,?)";
		String[] selection_args = new String[] { "1", "2", "12", "13", "14", "17", "27", "28", "30" };

		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(DeductionContentProvider.CONTENT_URI, PROJECTION, selection, selection_args,
				null);

		// fetch our deductions
		double pre_tax_deduction_total = 0.0;

		if (cursor.moveToFirst()) {
			do {
				// set payday+3 due to payday radio button being 0,1,2 where
				// cursor columns for payday are 3,4,5
				if (cursor.getString(payday + 3).equals("true")) {
					pre_tax_deduction_total += cursor.getDouble(1);
				}
			} while (cursor.moveToNext());
		}
		
		System.out.println("PreTax deductions = " + pre_tax_deduction_total);
		
		// calculate CERS as 8% of gross
		double cers = vh.getGross_pay_total() * CERS_MULTIPLIER;
		System.out.println("CERS = " + cers);

		pre_tax_deduction_total += cers;
		System.out.println("PreTaxCers deductions = " + pre_tax_deduction_total);

		return pre_tax_deduction_total;
	}
}
