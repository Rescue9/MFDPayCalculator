
package com.corridor9design.mfdpaycalculator.deductions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.corridor9design.mfdpaycalculator.BuildConfig;
import com.corridor9design.mfdpaycalculator.MainActivity;
import com.corridor9design.mfdpaycalculator.R;
import com.corridor9design.mfdpaycalculator.database.Deduction;
import com.corridor9design.mfdpaycalculator.database.DeductionContentProvider;
import com.corridor9design.mfdpaycalculator.database.MyDeductionDbHelper;

public class DeductionEditFragment extends DialogFragment {

    // declare variables
    String deduction_name;
    String deduction_amount;
    String deduction_number;
    String deduction_description;
    String first_payday;
    String second_payday;
    String third_payday;
    long database_id;

    // declare gui elements
    EditText deduction_name_edit;
    EditText deduction_amount_edit;
    EditText deduction_number_edit;
    EditText deduction_description_edit;
    CheckBox deduction_first_pay_checkbox;
    CheckBox deduction_second_pay_checkbox;
    CheckBox deduction_third_pay_checkbox;

    MyDeductionDbHelper db = new MyDeductionDbHelper(getActivity());

    ContentValues values = new ContentValues();
    View view;

    AlertDialog.Builder mBuilder;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBuilder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_deduction_editor, null);
        setupGuiInstances();
        // startListening();

        mBuilder.setTitle("Edit Dialog");
        mBuilder.setView(view);

        // get the database id
        long db_id = getArguments().getLong("database_id", -1);

        // if the id isn't negative, then we edit it
        if (db_id > -1) {
            // change the names of the buttons since we're not adding a new item
            mBuilder.setPositiveButton(R.string.deduction_button_delete,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDeductionItem();
                        }
                    });

            mBuilder.setNeutralButton(R.string.deduction_button_update,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (deduction_name_edit.getText().length() == 0
                                    && deduction_amount_edit.getText().length() == 0) {
                                deleteDeductionItem();
                                return;
                            }
                            updateDeductionItem();
                            return;
                        }

                    });

            mBuilder.setNegativeButton(R.string.deduction_button_cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }

                    });
        } else {

            mBuilder.setPositiveButton(R.string.deduction_button_cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

            mBuilder.setNegativeButton(R.string.deduction_button_accept,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (BuildConfig.DEBUG)
                                Log.d(MainActivity.TAG, "Added Deduction");
                            getValues();
                            if (deduction_name_edit.getText().length() != 0
                                    && deduction_amount_edit.getText().length() != 0) {
                                createDeductionItem();
                            } else {
                                Toast.makeText(getActivity(), "Deduction must have Name & Amount",
                                        Toast.LENGTH_LONG).show();
                            }
                            dismiss();
                        }

                    });
        }

        return mBuilder.create();
    }

    public void onResume() {
        super.onResume();

        // get the database id
        long db_id = getArguments().getLong("database_id", -1);

        // if the id isn't negative, then we edit it
        if (db_id > -1) {
            editDeduction(db_id);
        }

    }

    public void setupGuiInstances() {
        // gui display elements
        deduction_name_edit = (EditText) view.findViewById(R.id.deduction_name);
        deduction_amount_edit = (EditText) view.findViewById(R.id.deduction_amount);
        deduction_number_edit = (EditText) view.findViewById(R.id.deduction_number);
        deduction_description_edit = (EditText) view.findViewById(R.id.deduction_description);
        deduction_first_pay_checkbox = (CheckBox) view
                .findViewById(R.id.deduction_checkbox_first_payday);
        deduction_second_pay_checkbox = (CheckBox) view
                .findViewById(R.id.deduction_checkbox_second_payday);
        deduction_third_pay_checkbox = (CheckBox) view
                .findViewById(R.id.deduction_checkbox_third_payday);
    }

    private void createDeductionItem() {
        if(isCersDeduction(Integer.parseInt(deduction_number))){
            // cers deduction not allowed
            Toast.makeText(getActivity(), "CERS deduction not allowed", Toast.LENGTH_LONG).show();
            return;
        } else {
        // create a resolver to connect to the content provider
        ContentResolver resolver = getActivity().getContentResolver();

        // clear the ContentValues object; values
        values.clear();

        values.put(Deduction.COLUMN_AMOUNT, deduction_amount);
        values.put(Deduction.COLUMN_DESCRIPTION, deduction_description);
        values.put(Deduction.COLUMN_NAME, deduction_name);
        values.put(Deduction.COLUMN_NUMBER, deduction_number);
        values.put(Deduction.COLUMN_PAYDAY1, first_payday);
        values.put(Deduction.COLUMN_PAYDAY2, second_payday);
        values.put(Deduction.COLUMN_PAYDAY3, third_payday);

        resolver.insert(DeductionContentProvider.CONTENT_URI, values);
        }
    }

    private Dialog editDeduction(long id) {
        // setup resolver to get from content provider
        database_id = id;
        ContentResolver resolver = getActivity().getContentResolver();

        // set arrays for querying database
        String[] projection = new String[] {
                "_id", "name", "amount", "number", "description", "payday1", "payday2", "payday3"
        };
        String[] selectionArgs = new String[] {
            id + ""
        };

        // clear previous values
        values.clear();

        // get these columns into the content values object
        values.get(Deduction.COLUMN_AMOUNT);
        values.get(Deduction.COLUMN_DESCRIPTION);
        values.get(Deduction.COLUMN_ID);
        values.get(Deduction.COLUMN_NAME);
        values.get(Deduction.COLUMN_NUMBER);
        values.get(Deduction.COLUMN_PAYDAY1);
        values.get(Deduction.COLUMN_PAYDAY2);
        values.get(Deduction.COLUMN_PAYDAY3);

        // we want a singular row, so we'll create a new URI with the row id
        Uri singleUri = ContentUris.withAppendedId(DeductionContentProvider.CONTENT_URI, id);

        // get the row from the content provider into a cursor
        Cursor cursor = resolver.query(singleUri, projection, Deduction.COLUMN_ID + "=?",
                selectionArgs, null);
        cursor.moveToFirst();

        // set the edittext areas to the cursor strings from the database
        deduction_name_edit.setText(cursor.getString(1));
        deduction_amount_edit.setText(cursor.getString(2));
        deduction_number_edit.setText(cursor.getString(3));
        deduction_description_edit.setText(cursor.getString(4));

        // use these if statements to check boxes as return results are strings
        if (cursor.getString(5).equals("true")) {
            deduction_first_pay_checkbox.setChecked(true);
        }
        if (cursor.getString(6).equals("true")) {
            deduction_second_pay_checkbox.setChecked(true);
        }
        if (cursor.getString(7).equals("true")) {
            deduction_third_pay_checkbox.setChecked(true);
        }

        Dialog editDialog = mBuilder.create();
        return editDialog;
    }

    private void updateDeductionItem() {
        // get values from gui
        getValues();
        // create a resolver to connect to the content provider
        ContentResolver resolver = getActivity().getContentResolver();
        String[] selectionArgs = new String[] {
            database_id + ""
        };

        // clear values
        values.clear();

        values.put(Deduction.COLUMN_AMOUNT, deduction_amount);
        values.put(Deduction.COLUMN_DESCRIPTION, deduction_description);
        values.put(Deduction.COLUMN_NAME, deduction_name);
        values.put(Deduction.COLUMN_NUMBER, deduction_number);
        values.put(Deduction.COLUMN_PAYDAY1, first_payday);
        values.put(Deduction.COLUMN_PAYDAY2, second_payday);
        values.put(Deduction.COLUMN_PAYDAY3, third_payday);

        resolver.update(DeductionContentProvider.CONTENT_URI, values, Deduction.COLUMN_ID + "=?",
                selectionArgs);
    }

    // delete the deduction item from the database
    private void deleteDeductionItem() {
        // create a resolver to connect to the content provider
        ContentResolver resolver = getActivity().getContentResolver();
        String[] selectionArgs = new String[] {
            database_id + ""
        };

        resolver.delete(DeductionContentProvider.CONTENT_URI, Deduction.COLUMN_ID + "=?",
                selectionArgs);
    }

    private void getValues() {
        // get values from current input
        deduction_name = deduction_name_edit.getText().toString();
        deduction_amount = deduction_amount_edit.getText().toString();
        deduction_number = deduction_number_edit.getText().toString();
        deduction_description = deduction_description_edit.getText().toString();

        if (deduction_first_pay_checkbox.isChecked()) {
            first_payday = "true";
        } else
            first_payday = "false";

        if (deduction_second_pay_checkbox.isChecked()) {
            second_payday = "true";
        } else
            second_payday = "false";

        if (deduction_third_pay_checkbox.isChecked()) {
            third_payday = "true";
        } else
            third_payday = "false";
    }
    
    private boolean isCersDeduction(int id) {
        if (id == 12) {
            return true;
        }
        return false;
    }
}
