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
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AdjustPicmodeDialog extends DialogFragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context = null;
	int brightnessValue;
	int contrastValue;
	int toneValue;
	int shapnessValue;
	TextView brightnessText = null;
	TextView contrastText = null;
	TextView toneText = null;
	TextView shapnessText = null;
	PjScreenViewInterface coordinateList;
	private Handler SeekbarHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		WeakReference<AdjustPicmodeDialog> mDialog;

		public MyHandler(AdjustPicmodeDialog dialog) {
			mDialog = new WeakReference<AdjustPicmodeDialog>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			AdjustPicmodeDialog dialog = mDialog.get();
			if (dialog != null) {
				switch (msg.what) {
				case 1:// tcp connect
					dialog.refresh(dialog.brightnessValue,
							SeekType.BRIGHTNESS_BAR);
					break;
				case 2:// tcp connect err
					dialog.refresh(dialog.contrastValue,
							SeekType.CONTRASTNESS_BAR);
					break;
				case 3:// tcp connect close
					dialog.refresh(dialog.toneValue, SeekType.TONE_BAR);
					break;
				case 4:// tcp err
					dialog.refresh(dialog.shapnessValue, SeekType.SHAPNESS_BAR);
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
				R.layout.dialog_adjustpicmode);
		InitView(layoutView);

		AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
				.create();
		alertdialog.setView(layoutView, 0, 0, 0, 0);
		return alertdialog;
	}

	private void InitView(View layoutView) {
		SeekBar brightnessBar = (SeekBar) layoutView
				.findViewById(R.id.brightnessseekbar);
		SeekBar contrastBar = (SeekBar) layoutView
				.findViewById(R.id.contrastseekbar);
		SeekBar toneBar = (SeekBar) layoutView.findViewById(R.id.toneseekbar);
		SeekBar sharpnessBar = (SeekBar) layoutView
				.findViewById(R.id.sharpnessseekbar);
		Button PIC_BtnOk = (Button) layoutView.findViewById(R.id.DialogBut_ok);
		Button PIC_BtnCancel = (Button) layoutView
				.findViewById(R.id.DialogBut_cancel);
		brightnessValue = PreferencesUtils.getInt(context, "brightnessValue",
				40);
		contrastValue = PreferencesUtils.getInt(context, "contrastValue", 40);
		toneValue = PreferencesUtils.getInt(context, "toneValue", 50);
		shapnessValue = PreferencesUtils.getInt(context, "shapnessValue", 12);

		brightnessBar.setProgress(brightnessValue);

		contrastBar.setProgress(contrastValue);

		toneBar.setProgress(toneValue);

		sharpnessBar.setMax(25);
		sharpnessBar.setProgress(shapnessValue);
		brightnessText = (TextView) layoutView
				.findViewById(R.id.brightness_value);
		contrastText = (TextView) layoutView.findViewById(R.id.contrast_value);
		toneText = (TextView) layoutView.findViewById(R.id.tone_value);
		shapnessText = (TextView) layoutView.findViewById(R.id.sharpness_value);
		brightnessText.setText(Integer.toString(brightnessValue));
		contrastText.setText(Integer.toString(contrastValue));
		toneText.setText(Integer.toString(toneValue));
		shapnessText.setText(Integer.toString(shapnessValue));
		brightnessBar.setOnSeekBarChangeListener(this);
		contrastBar.setOnSeekBarChangeListener(this);
		toneBar.setOnSeekBarChangeListener(this);
		sharpnessBar.setOnSeekBarChangeListener(this);
		PIC_BtnOk.setOnClickListener(this);
		PIC_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {

		case R.id.brightnessseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			brightnessValue = progress;
			SeekbarHandler.sendEmptyMessage(1);
			break;
		case R.id.contrastseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			contrastValue = progress;
			SeekbarHandler.sendEmptyMessage(2);
			break;
		case R.id.toneseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			toneValue = progress;
			SeekbarHandler.sendEmptyMessage(3);
			break;
		case R.id.sharpnessseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			shapnessValue = progress;
			SeekbarHandler.sendEmptyMessage(4);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.DialogBut_ok:
			PreferencesUtils
					.putInt(context, "brightnessValue", brightnessValue);
			PreferencesUtils.putInt(context, "contrastValue", contrastValue);
			PreferencesUtils.putInt(context, "toneValue", toneValue);
			PreferencesUtils.putInt(context, "shapnessValue", shapnessValue);
			this.dismiss();
			break;
		case R.id.DialogBut_cancel:
			brightnessValue = PreferencesUtils.getInt(context,
					"brightnessValue", 40);
			contrastValue = PreferencesUtils.getInt(context, "contrastValue",
					40);
			toneValue = PreferencesUtils.getInt(context, "toneValue", 12);

			shapnessValue = PreferencesUtils.getInt(context, "shapnessValue",
					50);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setPj_Infor.setPjFunctionPacket(
							coordinateList.getCoordinateList(),
							SystemFuntion.SET_PICMODE_BRIGHTNESS, (byte) 0x31,
							(byte) 0x00, (byte) brightnessValue);
					SystemClock.sleep(300);
					setPj_Infor.setPjFunctionPacket(
							coordinateList.getCoordinateList(),
							SystemFuntion.SET_PICMODE_CONTRAST, (byte) 0x31,
							(byte) 0x00, (byte) contrastValue);
					SystemClock.sleep(300);
					setPj_Infor.setPjFunctionPacket(
							coordinateList.getCoordinateList(),
							SystemFuntion.SET_PICMODE_TON, (byte) 0x31,
							(byte) 0x00, (byte) toneValue);
					SystemClock.sleep(300);
					setPj_Infor.setPjFunctionPacket(
							coordinateList.getCoordinateList(),
							SystemFuntion.SET_PICMODE_SHAPNESS, (byte) 0x31,
							(byte) 0x00, (byte) shapnessValue);
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
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.brightnessseekbar:
			setPj_Infor.setPjFunctionPacket(
					coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_BRIGHTNESS, (byte) 0x31,
					(byte) 0x00, (byte) brightnessValue);
			Log.i("DialogViewInit", "brightnessseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.contrastseekbar:
			setPj_Infor.setPjFunctionPacket(
					coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_CONTRAST, (byte) 0x31,
					(byte) 0x00, (byte) contrastValue);
			Log.i("DialogViewInit", "contrastseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.toneseekbar:
			setPj_Infor.setPjFunctionPacket(
					coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_TON, (byte) 0x31, (byte) 0x00,
					(byte) toneValue);
			Log.i("DialogViewInit", "toneseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.sharpnessseekbar:
			setPj_Infor.setPjFunctionPacket(
					coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_SHAPNESS, (byte) 0x31,
					(byte) 0x00, (byte) shapnessValue);
			Log.i("DialogViewInit", "sharpnessseekbar停止后执行的操作，有待进一步归类");
			break;

		default:
			break;
		}
	}

	public void refresh(int current, SeekType seekbar) {
		switch (seekbar) {

		case BRIGHTNESS_BAR:
			brightnessText.setText(Integer.toString(current));
			break;
		case CONTRASTNESS_BAR:
			contrastText.setText(Integer.toString(current));
			break;
		case TONE_BAR:
			toneText.setText(Integer.toString(current));
			break;
		case SHAPNESS_BAR:
			shapnessText.setText(Integer.toString(current));
			break;

		default:
			break;
		}
	}
}
