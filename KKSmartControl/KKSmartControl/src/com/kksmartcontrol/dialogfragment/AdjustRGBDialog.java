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

public class AdjustRGBDialog extends DialogFragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context = null;

	int colorRValue;
	int colorGValue;
	int colorBValue;
	TextView RGainText = null;
	TextView GGainText = null;
	TextView BGainText = null;

	PjScreenViewInterface coordinateList;
	private Handler SeekbarHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		WeakReference<AdjustRGBDialog> mDialog;

		public MyHandler(AdjustRGBDialog dialog) {
			mDialog = new WeakReference<AdjustRGBDialog>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			AdjustRGBDialog dialog = mDialog.get();
			if (dialog != null) {
				switch (msg.what) {
				case 1:
					dialog.refresh(dialog.colorRValue, SeekType.RGIN_BAR);
					break;
				case 2:
					dialog.refresh(dialog.colorGValue, SeekType.GGAIN_BAR);
					break;
				case 3:
					dialog.refresh(dialog.colorBValue, SeekType.BGAIN_BAR);
					break;
				default:
					break;
				}

			}
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		coordinateList = ((MainActivity) getActivity());
		View layoutView = DialogUtil.createDialogView(context,
				R.layout.dialog_adjustrgb);
		InitViewControls(layoutView);
		AlertDialog alertdialog = new AlertDialog.Builder(context).create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitViewControls(View layoutView) {
		SeekBar RGainBar = (SeekBar) layoutView
				.findViewById(R.id.colorRseekbar);
		SeekBar GGainBar = (SeekBar) layoutView
				.findViewById(R.id.colorGseekbar);
		SeekBar BGainBar = (SeekBar) layoutView
				.findViewById(R.id.colorBseekbar);
		Button RGB_BtnOk = (Button) layoutView.findViewById(R.id.DialogBut_ok);
		Button RGB_BtnCancel = (Button) layoutView
				.findViewById(R.id.DialogBut_cancel);
		colorRValue = PreferencesUtils.getInt(context, "colorRValue", 128);
		colorGValue = PreferencesUtils.getInt(context, "colorGValue", 128);
		colorBValue = PreferencesUtils.getInt(context, "colorBValue", 128);
		RGainBar.setMax(255);
		RGainBar.setProgress(colorRValue);
		GGainBar.setMax(255);
		GGainBar.setProgress(colorGValue);
		BGainBar.setMax(255);
		BGainBar.setProgress(colorBValue);
		RGainText = (TextView) layoutView.findViewById(R.id.colorR_value);
		GGainText = (TextView) layoutView.findViewById(R.id.colorG_value);
		BGainText = (TextView) layoutView.findViewById(R.id.colorB_value);
		RGainText.setText(Integer.toString(colorRValue));
		GGainText.setText(Integer.toString(colorGValue));
		BGainText.setText(Integer.toString(colorBValue));
		RGainBar.setOnSeekBarChangeListener(this);
		GGainBar.setOnSeekBarChangeListener(this);
		BGainBar.setOnSeekBarChangeListener(this);
		RGB_BtnOk.setOnClickListener(this);
		RGB_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DialogBut_ok:
			PreferencesUtils.putInt(context, "colorRValue", colorRValue);
			PreferencesUtils.putInt(context, "colorGValue", colorGValue);
			PreferencesUtils.putInt(context, "colorBValue", colorBValue);
			this.dismiss();
			break;
		case R.id.DialogBut_cancel:
			colorRValue = PreferencesUtils.getInt(context, "colorRValue", 128);
			colorGValue = PreferencesUtils.getInt(context, "colorGValue", 128);
			colorBValue = PreferencesUtils.getInt(context, "colorBValue", 128);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setPj_Infor.adjustColorMode(
							coordinateList.getCoordinateList(),
							SystemFuntion.SET_COLORMODE_R, (byte) 0x31,
							(byte) 0x00, (byte) 0x06, (byte) colorRValue,
							(byte) colorGValue, (byte) colorBValue,
							(byte) 0x80, (byte) 0x80, (byte) 0x80);
				}
			}).start();
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
		switch (seekBar.getId()) {

		case R.id.colorRseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			colorRValue = progress;
			SeekbarHandler.sendEmptyMessage(1);
			break;
		case R.id.colorGseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			colorGValue = progress;
			SeekbarHandler.sendEmptyMessage(2);
			break;
		case R.id.colorBseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			colorBValue = progress;
			SeekbarHandler.sendEmptyMessage(3);
			break;

		default:

			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.colorRseekbar:
			setPj_Infor.adjustColorMode(coordinateList.getCoordinateList(),
					SystemFuntion.SET_COLORMODE_R, (byte) 0x31, (byte) 0x00,
					(byte) 0x06, (byte) colorRValue, (byte) colorGValue,
					(byte) colorBValue, (byte) 0x80, (byte) 0x80, (byte) 0x80);
			Log.i("DialogViewInit", "Rseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.colorGseekbar:
			setPj_Infor.adjustColorMode(coordinateList.getCoordinateList(),
					SystemFuntion.SET_COLORMODE_G, (byte) 0x31, (byte) 0x00,
					(byte) 0x06, (byte) colorRValue, (byte) colorGValue,
					(byte) colorBValue, (byte) 0x80, (byte) 0x80, (byte) 0x80);
			Log.i("DialogViewInit", "Gseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.colorBseekbar:
			setPj_Infor.adjustColorMode(coordinateList.getCoordinateList(),
					SystemFuntion.SET_COLORMODE_B, (byte) 0x31, (byte) 0x00,
					(byte) 0x06, (byte) colorRValue, (byte) colorGValue,
					(byte) colorBValue, (byte) 0x80, (byte) 0x80, (byte) 0x80);
			Log.i("DialogViewInit", "Bseekbar停止后执行的操作，有待进一步归类");
			break;

		default:

			break;
		}
	}

	public void refresh(int current, SeekType seekbar) {
		switch (seekbar) {

		case RGIN_BAR:
			RGainText.setText(Integer.toString(current));
			break;
		case GGAIN_BAR:
			GGainText.setText(Integer.toString(current));
			break;
		case BGAIN_BAR:
			BGainText.setText(Integer.toString(current));
			break;
		default:
			break;
		}
	}
}
