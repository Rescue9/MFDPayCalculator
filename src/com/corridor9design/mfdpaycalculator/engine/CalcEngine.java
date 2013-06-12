package com.corridor9design.mfdpaycalculator.engine;


public class CalcEngine {

	ValuesHandler vh = new ValuesHandler();

	// variable values
	double base_pay_hours = vh.getScheduled_days() * 24;
	double overtime1_hours = (vh.getScheduled_days() * 24) - 80;
	double overtime2_hours = vh.getCallback_hours();
	int holiday_hours = vh.getHolidays_during_pay() * 8;

	// calculation values
	double base_pay_subtotal = vh.getBase_pay_rate() * 80;
	double overtime1_pay = vh.getOvertime1_pay_rate() * overtime1_hours;
	double overtime2_pay = vh.getOvertime2_pay_rate() * overtime2_hours;
	double holiday_pay = vh.getBase_pay_rate() * holiday_hours;
	double longevity = vh.getYears_worked() * 7;

	// calculation totals
	double base_pay_total = base_pay_subtotal + overtime1_pay + holiday_pay;
	double gross_pay_1st_total = base_pay_total + longevity + overtime2_pay;
	double gross_pay_2nd_total = base_pay_total + vh.getIncentive() + overtime2_pay;
	double gross_pay_3rd_total = base_pay_total + overtime2_pay;
	double gross_pay_total = vh.getGross_pay_total();

	public CalcEngine() {
	}

	public void calculateBase() {
		vh.setBase_pay_total(base_pay_total);
	}

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

	public void calculateTaxes(double taxable) {
		vh.setTaxes_total(taxable / 3);
	}

	public void calculateDeposti(double deposit) {
		vh.setDeposit_total(deposit * 2);
	}
}
