package com.kksmartcontrol.dialogfragment;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.dialog.util.DialogUtil;

import android.app.AlertDialog;
import android.app.Dialog; 
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutDialog extends DialogFragment implements OnClickListener {

	Context context = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();

		
		View layoutView =DialogUtil.createDialogView(context, R.layout.dialog_about, 0.6f, 0.6f);
		InitView(layoutView);

		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitView(View layoutView) {
		Button aboutBtn = (Button) layoutView.findViewById(R.id.DialogBut_ok);
		aboutBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// switch (v.getId()) {
		// case R.id.exitDialogBut_ok:
		this.dismiss();
		// break;
		// default:
		// break;
		// }
	}

}
