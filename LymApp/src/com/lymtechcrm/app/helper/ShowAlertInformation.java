package com.lymtechcrm.app.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lymtechcrm.app.R;
import com.lymtechcrm.app.main.MainActivity;

public class ShowAlertInformation {

	public static void showDialog(Context context, String Title, String Message) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.getMainScreenActivity());

		alertDialog.setTitle(Title);

		alertDialog.setMessage(Message);

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		alertDialog.show();
	}

}
