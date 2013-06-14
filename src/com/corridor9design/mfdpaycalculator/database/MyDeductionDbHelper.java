package com.corridor9design.mfdpaycalculator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDeductionDbHelper extends SQLiteOpenHelper {
	
	// static variables
	private static final int DATABASE_VERSION = 1; // database version
	private static final String DATABASE_NAME = "Deductions.db"; // database name
	
	// constructor
	public MyDeductionDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// create table
	@Override
	public void onCreate(SQLiteDatabase database){
		Deduction.onCreate(database);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// FIXME FUTURE: implement a way to upgrade tables without dropping them
		Deduction.onUpgrade(database, oldVersion, newVersion);
	}
}