package com.kksmartcontrol.dialogfragment;

import java.lang.ref.WeakReference;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.activity.MainActivity;
import com.kksmartcontrol.dialog.util.DialogUtil;
import com.kksmartcontrol.net.ParameDataHandle.SystemFuntion;
import com.kksmartcontrol.netcmd.SetPJ_Infor;
import com.kksmartcontrol.preference.PreferencesUtils;
import com.kksmartcontrol.util.PjScreenViewInterface;
import com.kksmartcontrol.util.SeekType;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BacklightDialog extends DialogFragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context = null;
	int backlightValue;
	TextView backlightText = null;
	PjScreenViewInterface coordinateList;
	private Handler SeekbarHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		WeakReference<BacklightDialog> mDialog;

		public MyHandler(BacklightDialog dialog) {
			mDialog = new WeakReference<BacklightDialog>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			BacklightDialog dialog = mDialog.get();
			if (dialog != null) {
				switch (msg.what) {
				case 1:// tcp connect
					dialog.refresh(dialog.backlightValue,
							SeekType.BRIGHTNESS_BAR);
					break;

				default:
					break;
				}
			}
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		context = getActivity();
		coordinateList = ((MainActivity) getActivity());
		View layoutView = DialogUtil.createDialogView(context,
				R.layout.dialog_backlight);
		InitView(layoutView);

		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitView(View layoutView) {
		SeekBar backlight_GainBar = (SeekBar) layoutView
				.findViewById(R.id.backlightseekbar);
		backlightText = (TextView) layoutView
				.findViewById(R.id.backlight_value);
		Button backlight_BtnOk = (Button) layoutView
				.findViewById(R.id.DialogBut_ok);
		Button backlight_BtnCancel = (Button) layoutView
				.findViewById(R.id.DialogBut_cancel);
		backlightValue = PreferencesUtils
				.getInt(context, "backlightValue", 100);
		backlight_GainBar.setProgress(backlightValue);
		backlightText.setText(Integer.toString(backlightValue));
		backlight_GainBar.setOnSeekBarChangeListener(this);
		backlight_BtnOk.setOnClickListener(this);
		backlight_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DialogBut_ok:
			PreferencesUtils.putInt(context, "backlightValue", backlightValue);
			this.dismiss();
			break;
		case R.id.DialogBut_cancel:
			backlightValue = PreferencesUtils.getInt(context, "backlightValue",
					100);
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_ADJUST_BACKLIGHT, (byte) 0x31,
					(byte) 0x00, (byte) backlightValue);
			Log.i("DialogViewInit", "点击取消后执行的操作，有待进一步归类");
			this.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		// switch (seekBar.getId()) {
		// case R.id.backlightseekbar:
		Log.i("onProgressChanged", "onProgressChanged =========" + progress);
		backlightValue = progress;
		SeekbarHandler.sendEmptyMessage(1);
		// break;
		// default:
		// break;
		// }
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// switch (seekBar.getId()) {
		//
		// case R.id.backlightseekbar:
		setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
				SystemFuntion.SET_ADJUST_BACKLIGHT, (byte) 0x31, (byte) 0x00,
				(byte) backlightValue);
		Log.i("DialogViewInit", "backlightseekbar停止后执行的操作，有待进一步归类");
		// break;
		//
		// default:
		// break;
		// }
	}

	public void refresh(int current, SeekType seekbar) {
		// switch (seekbar) {
		// case BACKLIGHT_BAR:
		backlightText.setText(Integer.toString(current));
		// break;
		//
		// default:
		// break;
		// }
	}

}
