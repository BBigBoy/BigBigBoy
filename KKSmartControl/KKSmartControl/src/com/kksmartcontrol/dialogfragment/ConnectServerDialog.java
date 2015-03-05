package com.kksmartcontrol.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.dialog.util.DialogUtil; 
import com.kksmartcontrol.net.NetWorkObject;

public class ConnectServerDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Context context = getActivity();

		View layoutView = DialogUtil.createDialogView(context,
				R.layout.dialog_conserver, 0.6f, 0.6f);
		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 在networkobject中，可能这个网络连接dialog对象还没完全创建就已经连上网，
		// 此时通过finviewbytag是找不到这个对象的，因此在此处判断，如果已经连上网络，则不显示
		if (NetWorkObject.getInstance().getNetStatus() == NetState.TCP_CONN_OPEN) {
			this.dismiss();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i("ConnectServerDialogonPause", "ConnectServerDialogonPause");
		super.onPause();
	}
}
