/**
 * Program: TaxEngine.java
 * Programmer: Andrew Buskov
 * Date: Jul 5, 2013
 * Purpose: To create a class that holds all formulas relating
 *  to the calculation of taxes.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.content.Context;
import android.util.Log;

import com.corridor9design.mfdpaycalculator.BuildConfig;
import com.corridor9design.mfdpaycalculator.MainActivity;
import com.corridor9design.mfdpaycalculator.preferences.PreferencesHandler;

public class TaxEngine {

    public static final double STATE_STANDARD_DEDUCTION = 2310.00;
    public static final double FEDERAL_STANDARD_DEDUCTION = 3900.00; 

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
    double incentive;

    public double returnTaxWitholding(Context context, int payday) {
        double total_tax =
               // calculateAdditionalWithholding(context) +
                calculateFederalTaxes(context, payday) +
                calculateStateTaxes(context, payday) +
                calculateCityTaxes(context, payday) +
                calculateMedicare(context, payday);
        
        if (BuildConfig.DEBUG) Log.d(MainActivity.TAG, "Total Taxes: " + total_tax);

        return total_tax;
    }

    public double calculateAdditionalWithholding(Context context) {
        double totalAdditionalWithholding = ph.getDoublePreference(
                "pref_additional_withholding_federal", context)
                + ph.getDoublePreference("pref_additional_withholding_state", context);
        return totalAdditionalWithholding;
    }

    public double calculateFederalTaxes(Context context, int payday) {
        
        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "Payday Switch #: " + payday);

        marital_status = ph.getIntPreference("pref_marital_status", context);
        exemptions = ph.getIntPreference("pref_exemptions", context);
        gross_pay = vh.getGross_pay_total();
        pre_tax_deductions = de.returnPreTaxDeductions(context, payday);

        double federal_taxes = 0;
        double federal_taxes_total = 0;
        double fed_taxable_gross = 0;
        double fed_taxable_gross_annual = 0;

        // allowances claimed on w4
        exemption_allowance = exemptions * FEDERAL_STANDARD_DEDUCTION;

        
        // use switch statement to calculate for payday calculations
        switch (payday){
                        
            case 0:
                fed_taxable_gross = gross_pay - pre_tax_deductions;
                fed_taxable_gross_annual = (fed_taxable_gross * 26) - FEDERAL_STANDARD_DEDUCTION;
                //if (BuildConfig.DEBUG)
                //    Log.d(MainActivity.TAG, "Fed_Tax_amount = " + fed_taxable_gross_annual);
                
                if (marital_status == 1) {
                    federal_taxes = singleTaxRate(fed_taxable_gross_annual) / 26;
                } else {
                    federal_taxes = marriedTaxRate(fed_taxable_gross_annual) / 26;
                }
                
                break;
                
            case 1:
                // calculate multipliers for second payday due to incentive
                incentive = ValuesHandler.INCENTIVE;
                double incentive_multiplier = incentive / gross_pay;
                double base_multiplier = 1 - incentive_multiplier;
                
                double fed_base_taxable_gross = (gross_pay - pre_tax_deductions) * base_multiplier;
                double fed_base_taxable_gross_annual = (fed_base_taxable_gross * 26) - FEDERAL_STANDARD_DEDUCTION;
                
                double fed_incentive_taxable_gross = (gross_pay - pre_tax_deductions) * incentive_multiplier;
                double fed_incentive_taxable_gross_annual = (fed_incentive_taxable_gross * 12) - FEDERAL_STANDARD_DEDUCTION;
                
                
                if (marital_status == 1) {
                    federal_taxes = (singleTaxRate(fed_base_taxable_gross_annual) / 26) + (singleTaxRate(fed_incentive_taxable_gross_annual) / 26);
                } else {
                    federal_taxes = (marriedTaxRate(fed_base_taxable_gross_annual) / 26) + (marriedTaxRate(fed_incentive_taxable_gross_annual) / 26);
                }
                
                break;
                
            case 2:
                fed_taxable_gross = gross_pay - pre_tax_deductions;
                fed_taxable_gross_annual = (fed_taxable_gross * 26) - FEDERAL_STANDARD_DEDUCTION;
                
                if (marital_status == 1) {
                    federal_taxes = singleTaxRate(fed_taxable_gross_annual) / 26;
                } else {
                    federal_taxes = marriedTaxRate(fed_taxable_gross_annual) / 26;
                }
                
                break;
                
        }
        federal_taxes_total = federal_taxes + ph.getDoublePreference(
                "pref_additional_withholding_federal", context);
        
        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "Total Federal Taxes: " + federal_taxes_total);
        
        return federal_taxes_total;

    }

    public double calculateStateTaxes(Context context, int payday) {
        pre_tax_deductions = de.returnPreTaxDeductions(context, payday);

        double state_taxes = 0;
        double state_taxable_amount = 0;
        double tax_rate_amount = 0;
        double tax_first_8000 = 8000;
        double tax_rate_on_8000 = 280;

        
        // switch for each payday
        switch(payday) {
            case 0:
                // calculate on 26 pays per year
                state_taxable_amount = ((gross_pay - pre_tax_deductions) * 26) - STATE_STANDARD_DEDUCTION; 
                tax_rate_amount = state_taxable_amount - tax_first_8000;

                state_taxes = ((tax_rate_amount * .058) + tax_rate_on_8000) / 26;

                if (BuildConfig.DEBUG)
                    Log.d(MainActivity.TAG, "State: " + state_taxes);
                break;
                
            case 1:
             // calculate multipliers for second payday due to incentive
                incentive = ValuesHandler.INCENTIVE;
                double incentive_multiplier = incentive / gross_pay;
                double base_multiplier = 1 - incentive_multiplier;
                
                double state_base_taxable_gross = (gross_pay - pre_tax_deductions) * base_multiplier;
                double state_base_taxable_gross_annual = (state_base_taxable_gross * 26) - STATE_STANDARD_DEDUCTION;
                double base_tax_rate_amount = state_base_taxable_gross_annual - tax_first_8000;
                double base_state_taxes = ((base_tax_rate_amount * .058) + (tax_rate_on_8000 * base_multiplier)) / 26;
                
                double state_incentive_taxable_gross = (gross_pay - pre_tax_deductions) * incentive_multiplier;
                double state_incentive_taxable_gross_annual = (state_incentive_taxable_gross * 26) - STATE_STANDARD_DEDUCTION;
                double incentive_tax_rate_amount = state_incentive_taxable_gross_annual - tax_first_8000;
                double incentive_state_taxes = ((incentive_tax_rate_amount * .058) + (tax_rate_on_8000 * incentive_multiplier)) / 26;
                
                // check for negative incentive taxes
                if (incentive_state_taxes < 0) {
                    incentive_state_taxes = 0;
                }
                
                state_taxes = base_state_taxes + incentive_state_taxes;
                if (BuildConfig.DEBUG)
                    Log.d(MainActivity.TAG, "State: " + state_taxes);
                
                break;
                
            case 2:
                // calculate on 26 pays per year
                state_taxable_amount = ((gross_pay - pre_tax_deductions) * 26) - STATE_STANDARD_DEDUCTION; 
                tax_rate_amount = state_taxable_amount - tax_first_8000;

                state_taxes = ((tax_rate_amount * .058) + tax_rate_on_8000) / 26;

                if (BuildConfig.DEBUG)
                    Log.d(MainActivity.TAG, "State: " + state_taxes);
                break;
        
        }
        
        
        return state_taxes;
    }

    public double calculateCityTaxes(Context context, int payday) {

        double city_taxes = vh.getGross_pay_total() * .015; // city tax is 1.5%

        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "City: " + city_taxes);
        return city_taxes;
    }

    public double calculateMedicare(Context context, int payday) {
        pre_tax_deductions = de.returnMedicareDeductions(context, payday);
     // medicare is 1.45%
        double medicare_taxes = (vh.getGross_pay_total() - pre_tax_deductions) * .0145; 

        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "Medicare: " + medicare_taxes);
        return medicare_taxes;
    }
    
    // 2012 tables!
    public double singleTaxRate(double taxable_amount) {
        double single_tax_rate = 0;

        if (taxable_amount < 2200.00) {
            single_tax_rate = 0;
        }

        if (taxable_amount >= 2200.00 && taxable_amount < 11125.00) {
            single_tax_rate = ((taxable_amount - 2200) * .10);
        }

        if (taxable_amount >= 11125.00 && taxable_amount < 38450.00) {
            single_tax_rate = ((taxable_amount - 11125) * .15) + 893.00;
        }

        if (taxable_amount >= 38450.00 && taxable_amount < 90050.00) {
            single_tax_rate = ((taxable_amount - 38450) * .25) + 4991.00;
        }

        if (taxable_amount >= 90050.00 && taxable_amount < 185450.00) {
            single_tax_rate = ((taxable_amount - 90050) * .28) + 17891.00;
        }

        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "Single tax rate = " + single_tax_rate);
        return single_tax_rate;
    }

    // 2012 tables!
    public double marriedTaxRate(double taxable_amount) {
        double married_tax_rate = 0;

        if (taxable_amount < 312.00) {
            married_tax_rate = 0;
        }

        if (taxable_amount >= 312.00 && taxable_amount < 981.00) {
            married_tax_rate = ((taxable_amount - 312) * .10);
        }

        if (taxable_amount >= 981.00 && taxable_amount < 3013.00) {
            married_tax_rate = ((taxable_amount - 981) * .15) + 66.90;
        }

        if (taxable_amount >= 3013.00 && taxable_amount < 5800.00) {
            married_tax_rate = ((taxable_amount - 3013) * .25) + 374.40;
        }

        if (taxable_amount >= 5800.00 && taxable_amount < 8675.00) {
            married_tax_rate = ((taxable_amount - 5800) * .28) + 1066.65;
        }

        if (BuildConfig.DEBUG)
            Log.d(MainActivity.TAG, "Married tax rate = " + married_tax_rate);
        return married_tax_rate;
    }
}
