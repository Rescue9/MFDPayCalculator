package com.corridor9design.mfdpaycalculator;

import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DeductionListDialog extends DialogFragment implements LoaderCallbacks<Cursor> {

	// set the projection as static
	private static final String[] PROJECTION = new String[] { "_id", "name", "amount" };

	// set the loader's unique id. loader id's are specific to the Activity or Fragment in which they reside
	private static final int LOADER_ID = 1;

	// the callback through which we will interact with the LoaderManager
	private LoaderCallbacks<Cursor> mCallbacks;

	// the adapter that binds data to our listview
	private SimpleCursorAdapter mAdapter;

	public DeductionListDialog() {
		// TODO Auto-generated constructor stub
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] DataColumns = new String[] { "name", "amount" };
		int[] viewIDs = new int[] { R.id.deduction_listing_deduction_name, R.id.deduction_listing_deduction_amount };

		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.deduction_listings, null, DataColumns, viewIDs, 0);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		View view = getActivity().getLayoutInflater().inflate(R.layout.activity_deduction_list, null);

		ListView listview = (ListView) view.findViewById(android.R.id.list);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "Item pressed was: " + id, Toast.LENGTH_SHORT).show();

			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "Item pressed was: " + id + " long.", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		listview.setAdapter(mAdapter);

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle("Testing");

		// The Activity (which implements the LoaderCallbacks<Cursor> interface) is the callbacks object through which
		// we will interact with the LoaderManager. The LoaderManager uses this object to instantiate the Loader and to
		// notify the client when data is made available/unavailable.
		mCallbacks = this;

		// Initialize the Loader with id '1' and callbacks 'mCallbacks'. If the loader doesn't already exist, one is
		// created. Otherwise, the already created Loader is reused. In either case, the LoaderManager will manage the
		// Loader across the Activity/Fragment lifecycle, will receive any new loads once they have completed, and will
		// report this new data back to the 'mCallbacks' object.
		LoaderManager lm = getLoaderManager();
		lm.initLoader(LOADER_ID, null, mCallbacks);

		return alertDialogBuilder.create();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// create a new CursorLoader with the following query parameters
		return new CursorLoader(getActivity(), DeductionContentProvider.CONTENT_URI, PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// a switch case is useful when dealing with multiple loaders/ids
		switch (loader.getId()) {
		case LOADER_ID:
			// the asyncronous load is complete and the data is now available for use. Only now can we associate the
			// query Cursor with the SimpleCursoeAdapter
			mAdapter.swapCursor(cursor);
			break;
		}
		// the listview now displays the queried data
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// for whatever reason, the loader's data is now unavailable. remove any references to the old data by replacing
		// it with a null Cursor
		mAdapter.swapCursor(null);
	}

}