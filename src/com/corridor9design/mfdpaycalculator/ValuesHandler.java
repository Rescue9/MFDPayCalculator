package com.corridor9design.mfdpaycalculator;

public class ValuesHandler {

	private static final double INCENTIVE = 258.33;

	// totals after calculation
	private static double base_pay_final;
	private static double gross_pay_final;
	private static double taxes_final;
	private static double deposit_final;

	// variables for hourly calculations
	private static double base_pay_rate;
	private static double overtime1_pay_rate;
	private static double overtime2_pay_rate;
	private static int scheduled_days;

	// additional variables
	private static double callback_hours;
	private static int years_worked;
	private static int holidays_worked;

	public ValuesHandler() {
		// TODO Auto-generated constructor stub
	}

	public static double getIncentive() {
		return INCENTIVE;
	}

	public double getBase_pay_final() {
		return base_pay_final;
	}

	public void setBase_pay_final(double base_pay_final) {
		ValuesHandler.base_pay_final = base_pay_final;
	}

	public double getGross_pay_final() {
		return gross_pay_final;
	}

	public void setGross_pay_final(double gross_pay_final) {
		ValuesHandler.gross_pay_final = gross_pay_final;
	}

	public double getTaxes_final() {
		return taxes_final;
	}

	public void setTaxes_final(double taxes_final) {
		ValuesHandler.taxes_final = taxes_final;
	}

	public double getDeposit_final() {
		return deposit_final;
	}

	public void setDeposit_final(double deposit_final) {
		ValuesHandler.deposit_final = deposit_final;
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

	public void setCallback_hours(double callback_hours) {
		ValuesHandler.callback_hours = callback_hours;
	}

	public int getYears_worked() {
		return years_worked;
	}

	public void setYears_worked(int years_worked) {
		ValuesHandler.years_worked = years_worked;
	}

	public int getHolidays_worked() {
		return holidays_worked;
	}

	public void setHolidays_worked(int holidays_worked) {
		ValuesHandler.holidays_worked = holidays_worked;
	}

}
