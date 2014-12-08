package com.kksmartcontrol.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.example.kksmartcontrol.R;
import com.kksmartcontrol.dialog.util.DialogUtil; 

public class ConnectServerDialog extends DialogFragment {

	Context context = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		context = getActivity();

		View layoutView = DialogUtil.createDialogView(context,
				R.layout.conserverdialog, 0.6f, 0.6f);
		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

}
