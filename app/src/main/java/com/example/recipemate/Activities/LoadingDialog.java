package com.example.recipemate.Activities;
import com.example.recipemate.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

	private Activity activity;
	private AlertDialog dialog;

	public LoadingDialog(Activity myActivity) {
	    activity = myActivity;
	}

    public void startAlertDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    LayoutInflater inflater = activity.getLayoutInflater();
	    builder.setView(inflater.inflate(R.layout.loading_dialog, null));//maybe change the root?
	    builder.setCancelable(false);

	    dialog = builder.create();
	    dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}