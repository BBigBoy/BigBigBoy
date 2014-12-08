package com.kksmartcontrol.dialog;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.dialog.util.DialogUtil;
import com.kksmartcontrol.net.NetWorkObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExitDialog extends DialogFragment implements OnClickListener {

	
	Context context = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
	
		View layoutView = DialogUtil.createDialogView(context,
				R.layout.exitdialog, 0.6f, 0.6f);
		InitView(layoutView);

		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitView(View layoutView) {
		Button exit_BtnOk = (Button) layoutView
				.findViewById(R.id.DialogBut_ok);
		Button exit_BtnCancel = (Button) layoutView
				.findViewById(R.id.DialogBut_cancel);
		exit_BtnOk.setOnClickListener(this);
		exit_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DialogBut_ok:
			this.dismiss();
			((Activity) context).finish();
			// 网络模块反初始化应放在Activity结束以后
			NetWorkObject.getInstance().unInitNetClient();
			// android.os.Process.killProcess(android.os.Process.myPid()); //
			// 获取PID
			// System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			break;
		case R.id.DialogBut_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}

}