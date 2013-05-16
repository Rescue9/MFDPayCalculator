package com.corridor9design.mfdpaycalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DialogHandler extends DialogFragment {
	
	ValuesHandler vh = new ValuesHandler();
	ValueModifier vm = new ValueModifier();
	

	public DialogHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_fragment, null))
	    // Add action buttons
	           .setPositiveButton(R.string.dialog_button_accept, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText value = (EditText) ((AlertDialog) dialog). findViewById(R.id.dialog_edittext1);
	            	   vh.setBase_pay_total(Double.parseDouble(value.getText().toString()));
	               }
	           })
	           .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   DialogHandler.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
}
