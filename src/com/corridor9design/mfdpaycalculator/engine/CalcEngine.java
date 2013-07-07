/**
 * Program: CalcEngine.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create the calculation engine for our application.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.content.Context;

public class CalcEngine {

	// create a new values handler & deduction engine objects
	ValuesHandler vh = new ValuesHandler();
	DeductionEngine de = new DeductionEngine();
	TaxEngine te = new TaxEngine();

	// create variables for use in calcengine class
	// from calculation done on supplied values
	double base_pay_hours = vh.getScheduled_days() * 24;
	double overtime1_hours = (vh.getScheduled_days() * 24) - 80;
	double overtime2_hours = vh.getCallback_hours();
	int holiday_hours = vh.getHolidays_during_pay() * 8;

	// calculate pay, subtotals, and longevity from above variables
	double longevity = vh.getYears_worked() * 7;
	double base_pay_subtotal = vh.getBase_pay_rate() * 80;
	double overtime1_pay = vh.getOvertime1_pay_rate() * overtime1_hours; // calculateScheduledOvertime();
	double overtime2_pay = vh.getOvertime2_pay_rate() * overtime2_hours; //calculateUnscheduledOvertime();
	double holiday_pay = vh.getBase_pay_rate() * holiday_hours;

	// calculate totals from subtotals and variables above
	double base_pay_total = base_pay_subtotal + overtime1_pay + holiday_pay;
	double gross_pay_1st_total = base_pay_total + longevity + overtime2_pay;
	double gross_pay_2nd_total = base_pay_total + vh.getIncentive() + overtime2_pay;
	double gross_pay_3rd_total = base_pay_total + overtime2_pay;
	double gross_pay_total = vh.getGross_pay_total();

	// set base pay value from above total
	public void calculateBase() {
		vh.setBase_pay_total(base_pay_total);
		System.out.println("calculateBase: " + vh.getBase_pay_total());

	}
	
	public double calculateScheduledOvertime(){
		// calculate longevity for the scheduled overtime
		double long_sched_ot = (longevity * 12) / 2912;
		
		// scheduled overtime calculation
		double sched_ot = (vh.getBase_pay_rate() + long_sched_ot) * 1.5 * overtime1_hours;
		System.out.println("calculateScheduledOvertime " + sched_ot);
		return sched_ot;
	}
	
	public double calculateUnscheduledOvertime(){
		// calculate longevity for the scheduled overtime
		double long_sched_ot = (longevity * 12) / 2912;
				
		// scheduled overtime calculation
		double unsched_ot = (vh.getBase_pay_rate() + long_sched_ot + (3100.00/2080.00)) * 1.5 * overtime2_hours;
		System.out.println("calculateUnscheduledOvertime " + unsched_ot);
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
		vh.setDeposit_total(deposit - calculateTaxes(context, payday) - de.returnDeductionTotal(context, payday));
		//de.returnPreTaxDeductions(context, payday);
		//de.returnPostTaxDeductions(context, payday);
	}
}
