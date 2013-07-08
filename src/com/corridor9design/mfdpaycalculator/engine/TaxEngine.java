/**
 * Program: TaxEngine.java
 * Programmer: Andrew Buskov
 * Date: Jul 5, 2013
 * Purpose: To create a class that holds all formulas relating
 *  to the calculation of taxes.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.content.Context;

import com.corridor9design.mfdpaycalculator.preferences.PreferencesHandler;

public class TaxEngine {
	
	public static final double STATE_STANDARD_DEDUCTION = 2310.00;
	public static final double FEDERAL_STANDARD_DEDUCTION = 146.15; // 2012 rate!
	
	// create handlers
	PreferencesHandler ph = new PreferencesHandler();
	ValuesHandler vh = new ValuesHandler();
	DeductionEngine de = new DeductionEngine();
	
	// declare variable
	int exemptions;
	int marital_status;
	double gross_pay;
	double exemption_allowance;
	double pre_tax_deductions;
	
	public double returnTaxWitholding(Context context, int payday){
		double total_tax = 
				calculateFederalTaxes(context, payday) +
				calculateStateTaxes(context, payday) +
				calculateCityTaxes(context, payday) +
				calculateMedicare(context, payday);
		
		return total_tax;
	}
	
	public double calculateFederalTaxes(Context context, int payday){
		marital_status = ph.getIntPreference("pref_marital_status", context);
		exemptions = ph.getIntPreference("pref_exemptions", context);
		gross_pay = vh.getGross_pay_total();
		pre_tax_deductions = de.returnPreTaxDeductions(context, payday);
		
		
		double federal_taxes = 0;
		double fed_taxable_amount = 0;

		// allowances claimed on w4
		exemption_allowance = exemptions * FEDERAL_STANDARD_DEDUCTION;
		
		fed_taxable_amount = gross_pay - exemption_allowance - pre_tax_deductions;
		//System.out.println("Fed_Tax_amount = " + fed_taxable_amount);
		
		if(marital_status == 1){
			federal_taxes = singleTaxRate(fed_taxable_amount);
		} else {
			federal_taxes = marriedTaxRate(fed_taxable_amount);
		}
		//System.out.println("Fed: " + federal_taxes);
		return federal_taxes;
	}
	
	public double calculateStateTaxes(Context context, int payday){
		pre_tax_deductions = de.returnPreTaxDeductions(context, payday);

		double state_taxes = 0;
		double state_taxable_amount = 0;
		double tax_rate_amount = 0;
		
		state_taxable_amount = ((gross_pay - pre_tax_deductions) * 26) - STATE_STANDARD_DEDUCTION; // 26 pays per year
		double tax_first_8000 = 8000;
		double tax_rate_on_8000 = 280;
		
		tax_rate_amount = state_taxable_amount - tax_first_8000;
		
		state_taxes = ((tax_rate_amount * .058) + tax_rate_on_8000) / 26;
		
		//System.out.println("State: " + state_taxes);
		return state_taxes;
	}
	
	public double calculateCityTaxes(Context context, int payday){
		
		double city_taxes = vh.getGross_pay_total() * .015; // city tax is 1.5%
		
		//System.out.println("City: " + city_taxes);
		return city_taxes;
	}
	
	public double calculateMedicare(Context context, int payday){
		pre_tax_deductions = de.returnMedicareDeductions(context, payday);
		double medicare_taxes = (vh.getGross_pay_total() - pre_tax_deductions) * .0145; // medicare is 1.45%
		
		//System.out.println("Medicare: " + medicare_taxes);
		return medicare_taxes;
	}
	
	// 2012 tables!
	public double singleTaxRate(double taxable_amount){
		double single_tax_rate = 0;
		
		if(taxable_amount < 83.00){
			single_tax_rate = 0;
		}
		
		if(taxable_amount >= 83.00 && taxable_amount < 417.00){
			single_tax_rate = ((taxable_amount - 83) * .10);
		}

		if(taxable_amount >= 417.00 && taxable_amount < 1442.00){
			single_tax_rate = ((taxable_amount - 417) * .15) + 33.40;
		}
		
		if(taxable_amount >= 1442.00 && taxable_amount < 3377.00){
			single_tax_rate = ((taxable_amount - 1442) * .25) + 187.15;
		}
		
		if(taxable_amount >= 3377.00 && taxable_amount < 6954.00){
			single_tax_rate = ((taxable_amount - 3377) * .28) + 670.90;
		}
		
		//System.out.println("Single tax rate = " + single_tax_rate);
		return single_tax_rate;
	}
	
	// 2012 tables!
	public double marriedTaxRate(double taxable_amount){
		double married_tax_rate = 0;
		
		if(taxable_amount < 312.00){
			married_tax_rate = 0;
		}
		
		if(taxable_amount >= 312.00 && taxable_amount < 981.00){
			married_tax_rate = ((taxable_amount - 312) * .10);
		}

		if(taxable_amount >= 981.00 && taxable_amount < 3013.00){
			married_tax_rate = ((taxable_amount - 981) * .15) + 66.90;
		}
		
		if(taxable_amount >= 3013.00 && taxable_amount < 5800.00){
			married_tax_rate = ((taxable_amount - 3013) * .25) + 374.40;
		}
		
		if(taxable_amount >= 5800.00 && taxable_amount < 8675.00){
			married_tax_rate = ((taxable_amount - 5800) * .28) + 1066.65;
		}
		
		System.out.println("Married tax rate = " + married_tax_rate);
		return married_tax_rate;
	}
}
