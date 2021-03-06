/**
 * Program: CalcEngine.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create the calculation engine for our application.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.content.Context;
import android.util.Log;

import com.corridor9design.mfdpaycalculator.BuildConfig;
import com.corridor9design.mfdpaycalculator.MainActivity;
import com.corridor9design.mfdpaycalculator.preferences.PreferencesHandler;

public class CalcEngine {
    // create context

    public CalcEngine(Context context){
        dte = new DateEngine(context);
    }


	// create a new values handler & deduction engine objects
	ValuesHandler vh = new ValuesHandler();
	DeductionEngine de = new DeductionEngine();
	TaxEngine te = new TaxEngine();
	PreferencesHandler ph = new PreferencesHandler();
	DateEngine dte;

	// create variables for use in calcengine class
	// from calculation done on supplied values
	double base_pay_hours = vh.getScheduled_days() * 24;
	double sched_overtime_hours = (vh.getScheduled_days() * 24) - 80;
	double unsched_overtime_hours = vh.getCallback_hours();
	int holiday_hours = vh.getHolidays_during_pay() * 8;

	// calculate pay, subtotals, and longevity from above variables
	double base_pay_subtotal = vh.getBase_pay_rate() * 80;
	double holiday_pay = vh.getBase_pay_rate() * holiday_hours;
	
	// if advanced layout use edittext inputs for overtime calculation
	double longevity_days;
	double longevity_pay;
	double long_hourly_rate;
	double overtime1_pay;
	double overtime2_pay;

	// calculate totals from subtotals and variables above
	double base_pay_total;
	double gross_pay_1st_total;
	double gross_pay_2nd_total;
	double gross_pay_3rd_total;
	

	// set base pay value from above total
	public void calculateBase(Context context) {
		// if simple layout use methods for overtime calculation
		if(!ph.preferenceSet("pref_advanced_layout", context)){
			longevity_days = (7 * dte.getElapsedDays(context));
			longevity_pay = (7 * dte.getElapsedYears(context));
			long_hourly_rate = (longevity_days / 2912 / 30.437);
			if (BuildConfig.DEBUG) Log.d(MainActivity.TAG, "Longevity Hourly Rate: " + long_hourly_rate);

			overtime1_pay = calculateScheduledOvertime();
			overtime2_pay = calculateUnscheduledOvertime();
		
			base_pay_total = base_pay_subtotal + overtime1_pay + holiday_pay;
			gross_pay_1st_total = base_pay_total + longevity_pay + overtime2_pay;
			gross_pay_2nd_total = base_pay_total + vh.getIncentive() + overtime2_pay;
			gross_pay_3rd_total = base_pay_total + overtime2_pay;

		} else {
			overtime1_pay = vh.getOvertime1_pay_rate() * sched_overtime_hours;
			overtime2_pay = vh.getOvertime2_pay_rate() * unsched_overtime_hours;
			longevity_pay = vh.getYears_worked() * 7;
			
			base_pay_total = base_pay_subtotal + overtime1_pay + holiday_pay;
			gross_pay_1st_total = base_pay_total + longevity_pay + overtime2_pay;
			gross_pay_2nd_total = base_pay_total + vh.getIncentive() + overtime2_pay;
			gross_pay_3rd_total = base_pay_total + overtime2_pay;
	
		}
		vh.setBase_pay_total(base_pay_total);
		if(BuildConfig.DEBUG) Log.d(MainActivity.TAG, "calculateBase: " + vh.getBase_pay_total());

	}
	
	public double calculateScheduledOvertime(){
		// scheduled overtime calculation
		double sched_ot = (vh.getBase_pay_rate() + long_hourly_rate) * 1.5 * sched_overtime_hours;
		if(BuildConfig.DEBUG) Log.d(MainActivity.TAG, "calculateScheduledOvertime " + sched_ot);
		return sched_ot;
	}
	
	public double calculateUnscheduledOvertime(){
		// scheduled overtime calculation
		double unsched_ot = (vh.getBase_pay_rate() + long_hourly_rate + (3100.00/2080.00)) * 1.5 * unsched_overtime_hours;
		if(BuildConfig.DEBUG) Log.d(MainActivity.TAG, "calculateUnscheduledOvertime " + unsched_ot);
		return unsched_ot;
	}
	

	// set gross pay value from above total based upon payday
	public void calculateGross(int payday) {
		switch (payday) {
		case 0:
			vh.setGross_pay_total(gross_pay_1st_total);
			break;
		case 1:
			vh.setGross_pay_total(gross_pay_2nd_total);
			break;
		case 2:
			vh.setGross_pay_total(gross_pay_3rd_total);
			break;
		}
	}

	// set tax deduction total based upon above calculation
	public double calculateTaxes(Context context, int payday) {
		//vh.setTaxes_total(taxable / 3); // FIXME this calculation is too simple.
		double taxes = te.returnTaxWitholding(context, payday);
		vh.setTaxes_total(taxes);
		return taxes;
	}

	// set deposit total based upon above calculation
	public void calculateDeposit(Context context, double deposit, int payday) {
		//vh.setDeposit_total(((deposit - de.returnDeductionTotal(context, payday)) /3) * 2); // FIXME this calculation is too simple.
		vh.setDeposit_total(deposit - vh.getTaxes_total() - de.returnDeductionTotal(context, payday));
	}
}
