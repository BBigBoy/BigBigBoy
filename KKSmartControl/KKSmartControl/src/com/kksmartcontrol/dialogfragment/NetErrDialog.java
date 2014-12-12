package com.kksmartcontrol.dialogfragment;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.dialog.util.DialogUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NetErrDialog extends DialogFragment implements OnClickListener {
	
	Context context = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		
		View layoutView = DialogUtil.createDialogView(context,
				R.layout.dialog_neterr);
		InitView(layoutView);

		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitView(View layoutView) {
		Button net_BtnOk = (Button) layoutView.findViewById(R.id.DialogBut_ok);
		Button net_BtnCancel = (Button) layoutView
				.findViewById(R.id.DialogBut_cancel);
		net_BtnOk.setOnClickListener(this);
		net_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DialogBut_ok:
			Intent localIntent = new Intent();
			localIntent.setComponent(new ComponentName("com.android.settings",
					"com.android.settings.wifi.WifiPickerActivity"));
			context.startActivity(localIntent);
			this.dismiss();
			break;
		case R.id.DialogBut_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}

}
