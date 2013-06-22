/**
 * Program: DeductionContentProvider.java
 * Programmer: Andrew Buskov
 * Date: Jun 17, 2013
 * Purpose: To create a content provider that can be used to get information
 *  from the database throughout the application and from other applications.
 */

package com.corridor9design.mfdpaycalculator.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DeductionContentProvider extends ContentProvider {

	// declare database
	private MyDeductionDbHelper database;

	// create constants that can be used by the client
	private static final String AUTHORITY = "com.corridor9design.mfdpaycalculator.database.deductioncontentprovider";
	private static final String BASE_PATH = "deduction";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/deduction";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/deduction";

	// create UriMatcher and add Uris
	private static final int DEDUCTION_LIST = 1;
	private static final int DEDUCTION_ID = 2;
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, DEDUCTION_LIST);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DEDUCTION_ID);
	}

	public boolean onCreate() {
		database = new MyDeductionDbHelper(getContext());
		return false;
	}

	// create a cursor query that will return information to the content resolver
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// using SQLiteQueryBuilder instead of query
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check to make sure columns in projection exist in database
		checkColumns(projection);

		// tell the builder what table we want checked
		queryBuilder.setTables(Deduction.TABLE_DEDUCTION);

		// create a switch statement based upon directory or item uris
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case DEDUCTION_LIST:
			break;
		case DEDUCTION_ID:
			// add the id to the original query
			queryBuilder.appendWhere(Deduction.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// get the database object, query the database, return results to a cursor
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		// set notification uri for change
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	// used for inserting data into the database
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// find out if it's a dir or id uri
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		long id = 0;

		// if it is not a dir (list) uri type fail. insert cannot be done on id uri's
		switch (uriType) {
		case DEDUCTION_LIST:
			id = db.insert(Deduction.TABLE_DEDUCTION, null, values);
			break;
		default:
			// something went wrong
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// notify listeners of change and return item's uri
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// find out if it's a dir or id uri
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsDeleted = 0;

		// if it's a dir type, simply pass the selection & arguments
		switch (uriType) {
		case DEDUCTION_LIST:
			rowsDeleted = db.delete(Deduction.TABLE_DEDUCTION, selection, selectionArgs);
			break;

		// if it's an id, get the id from the uri...
		case DEDUCTION_ID:
			String id = uri.getLastPathSegment();
			// if the selection argument is empty, just delete the row
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(Deduction.TABLE_DEDUCTION, Deduction.TABLE_DEDUCTION + "=" + id, null);

				// if the selection argument has value, pass that to delete
			} else {
				rowsDeleted = db.delete(Deduction.TABLE_DEDUCTION, Deduction.TABLE_DEDUCTION + "=" + id, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// notify listeners of change and return item's uri
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// find out if it's a dir or id uri
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsUpdated = 0;

		// if it's a dir type, simply pass the selection & arguments
		switch (uriType) {
		case DEDUCTION_LIST:
			rowsUpdated = db.update(Deduction.TABLE_DEDUCTION, values, selection, selectionArgs);
			break;
		case DEDUCTION_ID:
			String id = uri.getLastPathSegment();
			// if the selection argument is empty, just delete the row
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(Deduction.TABLE_DEDUCTION, values, Deduction.TABLE_DEDUCTION + "=" + id
						+ " and " + selection, null);

				// if the selection argument has value, pass that to delete
			} else {
				rowsUpdated = db.update(Deduction.TABLE_DEDUCTION, values, Deduction.TABLE_DEDUCTION + "=" + id
						+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// notify listeners of change and return item's uri
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	// check the columns received from the calling method to make sure they exist in our table
	private void checkColumns(String[] projection) {
		String[] available = { Deduction.COLUMN_AMOUNT, Deduction.COLUMN_DESCRIPTION, Deduction.COLUMN_ID,
				Deduction.COLUMN_NAME, Deduction.COLUMN_PAYDAY1, Deduction.COLUMN_PAYDAY2, Deduction.COLUMN_PAYDAY3 };

		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

			// check to see if the columns requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
