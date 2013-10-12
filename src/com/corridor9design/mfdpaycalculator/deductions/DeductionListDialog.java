
package com.corridor9design.mfdpaycalculator.deductions;

import com.corridor9design.mfdpaycalculator.R;
import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DeductionListDialog extends DialogFragment implements LoaderCallbacks<Cursor>,
        OnClickListener {

    // set the projection (what we're wanting from the database) to pass to the
    // method
    private static final String[] PROJECTION = new String[] {
            "_id", "name", "amount"
    };

    // set the loader's unique id. loader id's are specific to the Activity or
    // Fragment in which they reside
    private static final int LOADER_ID = 1;

    // the callback through which we will interact with the LoaderManager
    private LoaderCallbacks<Cursor> mCallbacks;

    // the adapter that binds data to our listview
    private SimpleCursorAdapter mAdapter;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // the names of the columns we want to pass to the views
        String[] DataColumns = new String[] {
                "name", "amount"
        };
        // the views we want to pass the names to
        int[] viewIDs = new int[] {
                R.id.deduction_listing_deduction_name, R.id.deduction_listing_deduction_amount
        };

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.deduction_listings, null,
                DataColumns, viewIDs, 0);

        // set a binder so we can add a dollar sign in front of the amount
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                // if the column index is the amount column
                if (columnIndex == 2) {
                    // assign view to a new TextView
                    TextView amount = (TextView) view;
                    // create a number string from the cursor string in the
                    // amount column
                    String number_string = cursor.getString(cursor.getColumnIndex("amount"));
                    // setup decimal format
                    DecimalFormat df = new DecimalFormat("$##.00");
                    // create a new formatted string
                    String my_new_amount = df.format(Double.parseDouble(number_string));
                    // pass that new formatted string to the textview
                    amount.setText(my_new_amount);
                    return true;
                }
                return false;
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // the main view that houses our layout
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_deduction_list, null);

        // the listview that holds the deduction list
        ListView listview = (ListView) view.findViewById(android.R.id.list);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            // set a short click listener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create a new dialog fragment
                DialogFragment deduction_specifics = new DeductionSpecificsDialog();

                // / bundle database row so we can get the correct info for our
                // specific listing
                Bundle arguments = new Bundle();
                arguments.putLong("database_row", id);
                deduction_specifics.setArguments(arguments);
                deduction_specifics.show(getFragmentManager(), "dialog");
            }
        });
        // set the long click listener
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            // on long click we want to open the edit fragment
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment deduction_edit = new DeductionEditFragment();
                Bundle arguments = new Bundle();
                arguments.putLong("database_id", id);
                deduction_edit.setArguments(arguments);
                deduction_edit.show(getFragmentManager(), "dialog");
                /*
                 * Intent deduction_edit_intent = new Intent(getActivity(),
                 * DeductionEditActivity.class);
                 * deduction_edit_intent.putExtra("database_id", id);
                 * startActivity(deduction_edit_intent);
                 */
                return true;
            }
        });

        listview.setAdapter(mAdapter);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Deductions: ");
        alertDialogBuilder.setMessage("Long press to update or delete");
        alertDialogBuilder.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Add", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add button
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }

                ft.addToBackStack(null);
                DialogFragment deduction_edit = new DeductionEditFragment();
                Bundle arguments = new Bundle();
                deduction_edit.setArguments(arguments);
                deduction_edit.show(ft, "dialog");
            }
        });

        // The Activity (which implements the LoaderCallbacks<Cursor> interface)
        // is the callbacks object through which we will interact with the
        // LoaderManager. The LoaderManager uses this object to instantiate the
        // Loader and to notify the client when data is made
        // available/unavailable.
        mCallbacks = this;

        // Initialize the Loader with id '1' and callbacks 'mCallbacks'. If the
        // loader doesn't already exist, one is created. Otherwise, the already
        // created Loader is reused. In either case, the LoaderManager will
        // manage the Loader across the Activity/Fragment lifecycle, will
        // receive any new loads once they have completed, and will report this
        // new data back to the 'mCallbacks' object.
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        AlertDialog dialog = alertDialogBuilder.create();
        return dialog;

    }

    public void onResume() {
        super.onResume();
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create a new CursorLoader with the following query parameters
        return new CursorLoader(getActivity(), DeductionContentProvider.CONTENT_URI, PROJECTION,
                null, null,
                Deduction.COLUMN_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // a switch case is useful when dealing with multiple loaders/ids
        switch (loader.getId()) {
            case LOADER_ID:
                // the asyncronous load is complete and the data is now
                // available for use. Only now can we associate the query Cursor
                // with the SimpleCursoeAdapter
                mAdapter.swapCursor(cursor);
                break;
        }
        // the listview now displays the queried data
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // for whatever reason, the loader's data is now unavailable. remove any
        // references to the old data by replacing it with a null Cursor
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}
