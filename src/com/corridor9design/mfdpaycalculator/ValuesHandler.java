package com.corridor9design.mfdpaycalculator;

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

	public ValuesHandler() {
	}

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

	public void setValuesFromProbationaryFirefighterPay() {
		this.setBase_pay_rate(8.773);
		this.setOvertime1_pay_rate(13.160);
		this.setOvertime2_pay_rate(15.395);
	}

	public void setValuesFromFirefighterPay() {
		this.setBase_pay_rate(11.737);
		this.setOvertime1_pay_rate(17.606);
		this.setOvertime2_pay_rate(19.841);
	}

	public void setValuesFromEngineerPay() {
		this.setBase_pay_rate(12.060);
		this.setOvertime1_pay_rate(18.098);
		this.setOvertime2_pay_rate(20.333);
	}

	public void setValuesFromLieutenantPay() {
		this.setBase_pay_rate(12.556);
		this.setOvertime1_pay_rate(18.834);
		this.setOvertime2_pay_rate(21.069);
	}

	public void setValuesFromCaptainPay() {
		this.setBase_pay_rate(13.138);
		this.setOvertime1_pay_rate(19.707);
		this.setOvertime2_pay_rate(21.942);
	}

	public void setValuesFromMajorPay() {
		this.setBase_pay_rate(13.929);
		this.setOvertime1_pay_rate(20.894);
		this.setOvertime2_pay_rate(23.129);
	}
}
