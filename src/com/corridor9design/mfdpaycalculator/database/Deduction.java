/**
 * Program: Deduction.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a dbschema to be used for the deductions table.
 */

package com.corridor9design.mfdpaycalculator.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Deduction implements BaseColumns {

	public static final String TABLE_DEDUCTION = "deduction"; // table name

	// deductions table column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PAYDAY1 = "payday1";
	public static final String COLUMN_PAYDAY2 = "payday2";
	public static final String COLUMN_PAYDAY3 = "payday3";

	// create deduction table String
	private static final String CREATE_DEDUCTIONS_TABLE = "CREATE TABLE " + TABLE_DEDUCTION + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT, " + COLUMN_AMOUNT + " TEXT, " + COLUMN_NUMBER + " TEXT, "
			+ COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PAYDAY1 + " TEXT, " + COLUMN_PAYDAY2 + " TEXT, " + COLUMN_PAYDAY3
			+ " TEXT" + ")";

	public static void onCreate(SQLiteDatabase database) {
		// create the actual table
		database.execSQL(CREATE_DEDUCTIONS_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// FIXME FUTURE: implement a way to upgrade tables without dropping them
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_DEDUCTION);

		// create table again
		onCreate(database);
	}
}
