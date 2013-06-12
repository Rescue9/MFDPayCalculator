package com.corridor9design.mfdpaycalculator.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDeductionDbHelper extends SQLiteOpenHelper {
	
	// static variables
	private static final int DATABASE_VERSION = 1; // database version
	private static final String DATABASE_NAME = "Deductions.db"; // database name
	private static final String TABLE_DEDUCTIONS = "deductions"; // table name
	
	// deductions table column names
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_PAYDAY1 = "payday1";
	private static final String KEY_PAYDAY2 = "payday2";
	private static final String KEY_PAYDAY3 = "payday3";
	
	// allColumns string array
	private String allColumns[] = {
			KEY_ID,
			KEY_NAME,
			KEY_AMOUNT,
			KEY_DESCRIPTION,
			KEY_PAYDAY1,
			KEY_PAYDAY2,
			KEY_PAYDAY3
	};
	
	// simpleColumns for simple list
	private String simpleColumns[] = {
			KEY_ID,
			KEY_NAME,
			KEY_AMOUNT
	};
	
	// create deduction table
	private static final String CREATE_DEDUCTIONS_TABLE = 
			"CREATE TABLE " + TABLE_DEDUCTIONS + " (" +
			KEY_ID + " INTEGER PRIMARY KEY," +
			KEY_NAME + " TEXT, " +
			KEY_AMOUNT + " TEXT, " +
			KEY_DESCRIPTION + " TEXT, " +
			KEY_PAYDAY1 + " TEXT, " +
			KEY_PAYDAY2 + " TEXT, " +
			KEY_PAYDAY3 + " TEXT" + ")";
	
	// constructor
	public MyDeductionDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// create table
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_DEDUCTIONS_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// FIXME FUTURE: implement a way to upgrade tables without dropping them
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEDUCTIONS);
		
		// create table again
		onCreate(db);
	}
	
	// add a new deduction
	public void addDeduction(Deduction deduction){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, deduction.get_deduction_name());
		values.put(KEY_AMOUNT, deduction.get_deduction_amount());
		values.put(KEY_DESCRIPTION, deduction.get_deduction_description());
		values.put(KEY_PAYDAY1, deduction.get_first_payday());
		values.put(KEY_PAYDAY2, deduction.get_second_payday());
		values.put(KEY_PAYDAY3, deduction.get_third_payday());
		
		// insert row
		db.insert(TABLE_DEDUCTIONS, null, values);
		db.close();
	}
	
	// get a single deduction
	public Deduction getDeduction(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor  = db.query(TABLE_DEDUCTIONS, new String[] {
				KEY_ID, KEY_NAME, KEY_AMOUNT, KEY_DESCRIPTION, KEY_PAYDAY1, KEY_PAYDAY2, KEY_PAYDAY3 },
				KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
		
		if(cursor != null)
			cursor.moveToFirst();
		
		Deduction deduction = new Deduction(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		
		// return deduction
		return deduction;
	}
	
	// get all deductions
	public List<Deduction> getAllDeductions(){
		List<Deduction> deduction_list = new ArrayList<Deduction>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_DEDUCTIONS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Deduction deduction = deductionFromCursor(cursor);
			deduction_list.add(deduction);
			cursor.moveToNext();
		}
		cursor.close();
		return deduction_list;
	}
	
	public List<Deduction> getDeductionList(){
		List<Deduction> deduction_list = new ArrayList<Deduction>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_DEDUCTIONS, simpleColumns, null, null, null, null, KEY_ID);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Deduction deduction = new Deduction(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
			deduction_list.add(deduction);
			cursor.moveToNext();
		}
		cursor.close();
		return deduction_list;
	}
	
	// get count of deductions
	public int getDeductionCount(){
		String countQuery = "SELECT * FROM " + TABLE_DEDUCTIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		// return count
		return cursor.getCount();
	}
	
	// update single record
	public int updateDeduction(Deduction deduction){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, deduction.get_deduction_name());
		values.put(KEY_AMOUNT, deduction.get_deduction_amount());
		values.put(KEY_DESCRIPTION, deduction.get_deduction_description());
		values.put(KEY_PAYDAY1, deduction.get_first_payday());
		values.put(KEY_PAYDAY2, deduction.get_second_payday());
		values.put(KEY_PAYDAY3, deduction.get_third_payday());
		
		// update the row
		return db.update(TABLE_DEDUCTIONS, values, KEY_ID + " =?", new String[] {String.valueOf(deduction.get_id()) });
	}
	
	// delete a single deduction
	public void deleteDeduction(Deduction deduction){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DEDUCTIONS, KEY_ID + " =?", new String[] {String.valueOf(deduction.get_id()) });
		db.close();
	}
	
	// build deduction from cursor
	private Deduction deductionFromCursor(Cursor cursor){
		Deduction deduction = new Deduction();
		deduction.set_id(Integer.parseInt(cursor.getString(0)));
		deduction.set_deduction_name(cursor.getString(1));
		deduction.set_deduction_amount(cursor.getString(2));
		deduction.set_deduction_description(cursor.getString(3));
		deduction.set_first_payday(cursor.getString(4));
		deduction.set_second_payday(cursor.getString(5));
		deduction.set_third_payday(cursor.getString(6));
		
		return deduction;
		
	}
}
