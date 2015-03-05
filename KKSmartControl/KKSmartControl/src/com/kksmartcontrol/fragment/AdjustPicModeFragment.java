package com.kksmartcontrol.fragment;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.activity.MainActivity;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.net.ParameDataHandle.SystemFuntion;
import com.kksmartcontrol.netcmd.SetPJ_Infor;
import com.kksmartcontrol.preference.PreferencesUtils;
import com.kksmartcontrol.util.PjScreenViewInterface;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AdjustPicModeFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context;
	int brightnessValue;
	int contrastValue;
	int toneValue;
	int shapnessValue;
	SeekBar brightnessBar;
	SeekBar contrastBar;
	SeekBar toneBar;
	SeekBar sharpnessBar;
	PjScreenViewInterface coordinateList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		coordinateList = ((MainActivity) getActivity());
		View view = inflater.inflate(R.layout.adjustpicmode, container, false);
		((TextView) view.findViewById(R.id.fragmenttitletext))
				.setText(getResources().getString(R.string.pic_mode));
		InitView(view);
		setProgress();
		return view;
	}

	private void InitView(View layoutView) {
		brightnessBar = (SeekBar) layoutView
				.findViewById(R.id.brightnessseekbar);
		contrastBar = (SeekBar) layoutView.findViewById(R.id.contrastseekbar);
		toneBar = (SeekBar) layoutView.findViewById(R.id.toneseekbar);
		sharpnessBar = (SeekBar) layoutView.findViewById(R.id.sharpnessseekbar);
		Button PIC_BtnCancel = (Button) layoutView
				.findViewById(R.id.revocation_setting);
		brightnessValue = PreferencesUtils.getInt(context, "brightnessValue",
				40);
		contrastValue = PreferencesUtils.getInt(context, "contrastValue", 40);
		toneValue = PreferencesUtils.getInt(context, "toneValue", 50);
		shapnessValue = PreferencesUtils.getInt(context, "shapnessValue", 12);
		sharpnessBar.setMax(25);
		brightnessBar.setOnSeekBarChangeListener(this);
		contrastBar.setOnSeekBarChangeListener(this);
		toneBar.setOnSeekBarChangeListener(this);
		sharpnessBar.setOnSeekBarChangeListener(this);
		PIC_BtnCancel.setOnClickListener(this);
	}

	public void setProgress() {
		brightnessBar.setProgress(brightnessValue);
		contrastBar.setProgress(contrastValue);
		toneBar.setProgress(toneValue);
		sharpnessBar.setProgress(shapnessValue);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {

		case R.id.brightnessseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			brightnessValue = progress;
			break;
		case R.id.contrastseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			contrastValue = progress;
			break;
		case R.id.toneseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			toneValue = progress;
			break;
		case R.id.sharpnessseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			shapnessValue = progress;
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (NetWorkObject.getInstance().getNetStatus() == NetState.TCP_CONN_OPEN) {
			PreferencesUtils
					.putInt(context, "brightnessValue", brightnessValue);
			PreferencesUtils.putInt(context, "contrastValue", contrastValue);
			PreferencesUtils.putInt(context, "toneValue", toneValue);
			PreferencesUtils.putInt(context, "shapnessValue", shapnessValue);
		}
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.revocation_setting:
			brightnessValue = PreferencesUtils.getInt(context,
					"brightnessValue", 40);
			contrastValue = PreferencesUtils.getInt(context, "contrastValue",
					40);
			toneValue = PreferencesUtils.getInt(context, "toneValue", 12);

			shapnessValue = PreferencesUtils.getInt(context, "shapnessValue",
					50);
			// 撤销后还原进度
			setProgress();
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
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_BRIGHTNESS, (byte) 0x31,
					(byte) 0x00, (byte) brightnessValue);
			Log.i("DialogViewInit", "brightnessseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.contrastseekbar:
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_CONTRAST, (byte) 0x31,
					(byte) 0x00, (byte) contrastValue);
			Log.i("DialogViewInit", "contrastseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.toneseekbar:
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_TON, (byte) 0x31, (byte) 0x00,
					(byte) toneValue);
			Log.i("DialogViewInit", "toneseekbar停止后执行的操作，有待进一步归类");
			break;
		case R.id.sharpnessseekbar:
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_PICMODE_SHAPNESS, (byte) 0x31,
					(byte) 0x00, (byte) shapnessValue);
			Log.i("DialogViewInit", "sharpnessseekbar停止后执行的操作，有待进一步归类");
			break;
		default:
			break;
		}
	}

}
