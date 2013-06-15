package com.corridor9design.mfdpaycalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

public class DeductionListDialogFragment extends DialogFragment {

	public DeductionListDialogFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_deduction_list, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Testing");
        
        return alertDialogBuilder.create();

	}

}
