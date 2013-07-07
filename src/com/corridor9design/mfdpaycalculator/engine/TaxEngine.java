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
	
	public static final double STATE_STANDARD_DEDUCTION = 2360.00;
	
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
		exemption_allowance = exemptions * 150;
		
		fed_taxable_amount = gross_pay - exemption_allowance - pre_tax_deductions;
		//System.out.println("Fed_Tax_amount = " + fed_taxable_amount);
		
		if(marital_status == 1){
			federal_taxes = singleTaxRate(fed_taxable_amount);
		} else {
			federal_taxes = marriedTaxRate(fed_taxable_amount);
		}
		
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
						
		return state_taxes;
	}
	
	public double calculateCityTaxes(Context context, int payday){
		
		double city_taxes = vh.getGross_pay_total() * .015; // city tax is 1.5%
		
		return city_taxes;
	}
	
	public double calculateMedicare(Context context, int payday){
		
		double medicare_taxes = vh.getGross_pay_total() * .0145; // medicare is 1.45%
		
		return medicare_taxes;
	}
	
	public double singleTaxRate(double taxable_amount){
		double single_tax_rate = 0;
		
		if(taxable_amount < 85.00){
			single_tax_rate = 0;
		}
		
		if(taxable_amount >= 85.00 && taxable_amount < 428.00){
			single_tax_rate = ((taxable_amount - 85) * .10);
		}

		if(taxable_amount >= 428.00 && taxable_amount < 1479.00){
			single_tax_rate = ((taxable_amount - 428) * .15) + 34.30;
		}
		
		if(taxable_amount >= 1479.00 && taxable_amount < 3463.00){
			single_tax_rate = ((taxable_amount - 1479) * .25) + 191.95;
		}
		
		if(taxable_amount >= 3463.00 && taxable_amount < 7133.00){
			single_tax_rate = ((taxable_amount - 3463) * .28) + 687.95;
		}
		
		//System.out.println("Single tax rate = " + single_tax_rate);
		return single_tax_rate;
	}
	
	public double marriedTaxRate(double taxable_amount){
		double married_tax_rate = 0;
		
		if(taxable_amount < 319.00){
			married_tax_rate = 0;
		}
		
		if(taxable_amount >= 319.00 && taxable_amount < 1006.00){
			married_tax_rate = ((taxable_amount - 319) * .10);
		}

		if(taxable_amount >= 1006.00 && taxable_amount < 3108.00){
			married_tax_rate = ((taxable_amount - 1006) * .15) + 68.70;
		}
		
		if(taxable_amount >= 3108.00 && taxable_amount < 5950.00){
			married_tax_rate = ((taxable_amount - 3108) * .25) + 384.00;
		}
		
		if(taxable_amount >= 5950.00 && taxable_amount < 8898.00){
			married_tax_rate = ((taxable_amount - 5950) * .28) + 1094.50;
		}
		
		System.out.println("Married tax rate = " + married_tax_rate);
		return married_tax_rate;
	}
}
