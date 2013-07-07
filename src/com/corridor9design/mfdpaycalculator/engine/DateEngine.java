/**
 * Program: DateEngine.java
 * Programmer: Andrew Buskov
 * Date: Jul 7, 2013
 * Purpose: To calculate the amount of time
 *  between the hire date and the pay date.
 */

package com.corridor9design.mfdpaycalculator.engine;

import android.content.Context;

import com.corridor9design.mfdpaycalculator.preferences.PreferencesHandler;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateEngine {
	
	PreferencesHandler ph = new PreferencesHandler();

	LocalDate current_date = new LocalDate();
	LocalDate hire_date;
	
	
	public int getElapsedDays(Context context){
		String hire_date_pref = ph.getPreference("pref_hire_date", context);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy.MM.dd");
		hire_date = LocalDate.parse(hire_date_pref, dtf	);		
		
		int days_elapsed = Days.daysBetween(hire_date, current_date).getDays() - 1;
		
		return days_elapsed;
	}
	
	public int getElapsedYears(Context context){
		String hire_date_pref = ph.getPreference("pref_hire_date", context);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy.MM.dd");
		hire_date = LocalDate.parse(hire_date_pref, dtf	);
		
		int years_elapsed = Years.yearsBetween(hire_date, current_date).getYears();
		
		return years_elapsed;
	}
}
