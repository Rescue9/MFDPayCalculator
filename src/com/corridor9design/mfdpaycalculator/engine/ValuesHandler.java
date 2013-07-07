/**
 * Program: ValuesHandler.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a handler class for manipulating values
 *  throughout the entire application.
 */

package com.corridor9design.mfdpaycalculator.engine;

public class ValuesHandler {

	private static final double INCENTIVE = 258.33;

	// totals after calculation
	private static double base_pay_total;
	private static double gross_pay_total;
	private static double taxes_total;
	private static double deposit_total;

	// variables for hourly calculations
	private static double base_pay_rate;
	private static double overtime1_pay_rate;
	private static double overtime2_pay_rate;
	private static int scheduled_days;

	// additional variables
	private static double callback_hours;
	private static int years_worked;
	private static int holidays_during_pay;

	public double getIncentive() {
		return INCENTIVE;
	}

	public double getBase_pay_total() {
		return base_pay_total;
	}

	public void setBase_pay_total(double base_pay_total) {
		ValuesHandler.base_pay_total = base_pay_total;
	}

	public double getGross_pay_total() {
		return gross_pay_total;
	}

	public void setGross_pay_total(double gross_pay_total) {
		ValuesHandler.gross_pay_total = gross_pay_total;
	}

	public double getTaxes_total() {
		return taxes_total;
	}

	public void setTaxes_total(double taxes_total) {
		ValuesHandler.taxes_total = taxes_total;
	}

	public double getDeposit_total() {
		return deposit_total;
	}

	public void setDeposit_total(double deposit_total) {
		ValuesHandler.deposit_total = deposit_total;
	}

	public double getBase_pay_rate() {
		return base_pay_rate;
	}

	public void setBase_pay_rate(double base_pay_rate) {
		ValuesHandler.base_pay_rate = base_pay_rate;
	}

	public double getOvertime1_pay_rate() {
		return overtime1_pay_rate;
	}

	public void setOvertime1_pay_rate(double overtime1_pay_rate) {
		ValuesHandler.overtime1_pay_rate = overtime1_pay_rate;
	}

	public double getOvertime2_pay_rate() {
		return overtime2_pay_rate;
	}

	public void setOvertime2_pay_rate(double overtime2_pay_rate) {
		ValuesHandler.overtime2_pay_rate = overtime2_pay_rate;
	}

	public int getScheduled_days() {
		return scheduled_days;
	}

	public void setScheduled_days(int scheduled_days) {
		ValuesHandler.scheduled_days = scheduled_days;
	}

	public double getCallback_hours() {
		return callback_hours;
	}

	public void setOvertime_hours(double callback_hours) {
		ValuesHandler.callback_hours = callback_hours;
	}

	public int getYears_worked() {
		return years_worked;
	}

	public void setYears_worked(int years_worked) {
		ValuesHandler.years_worked = years_worked;
	}

	public int getHolidays_during_pay() {
		return holidays_during_pay;
	}

	public void setHolidays_during_pay(int holidays_worked) {
		ValuesHandler.holidays_during_pay = holidays_worked;
	}

	// use these values when simple layout is selected
	// values updated 03/2013
	public void setupSimpleValues(int rank) {
		switch (rank) {
		case 0:
			this.setBase_pay_rate(8.773);
			this.setOvertime1_pay_rate(13.160);
			this.setOvertime2_pay_rate(15.395);
			break;

		case 1:
			this.setBase_pay_rate(11.737);
			this.setOvertime1_pay_rate(17.606);
			this.setOvertime2_pay_rate(19.841);
			break;

		case 2:
			this.setBase_pay_rate(12.060);
			this.setOvertime1_pay_rate(18.098);
			this.setOvertime2_pay_rate(20.333);
			break;

		case 3:
			this.setBase_pay_rate(12.556);
			this.setOvertime1_pay_rate(18.834);
			this.setOvertime2_pay_rate(21.069);
			break;

		case 4:
			this.setBase_pay_rate(13.138);
			this.setOvertime1_pay_rate(19.707);
			this.setOvertime2_pay_rate(21.942);
			break;

		case 5:
			this.setBase_pay_rate(14.208);
			this.setOvertime1_pay_rate(21.896);
			this.setOvertime2_pay_rate(24.131);
			break;
		}
	}
}
