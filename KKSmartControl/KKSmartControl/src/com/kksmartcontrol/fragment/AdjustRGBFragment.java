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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AdjustRGBFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context = null;

	int colorRValue;
	int colorGValue;
	int colorBValue;
	SeekBar RGainBar, GGainBar, BGainBar;
	PjScreenViewInterface coordinateList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		coordinateList = ((MainActivity) getActivity());
		View layoutView = inflater
				.inflate(R.layout.adjustrgb, container, false);
		((TextView) layoutView.findViewById(R.id.fragmenttitletext))
				.setText(getResources().getString(R.string.rgbsetting));
		InitViewControls(layoutView);
		setProgress();

		return layoutView;
	}

	private void InitViewControls(View layoutView) {
		RGainBar = (SeekBar) layoutView.findViewById(R.id.colorRseekbar);
		GGainBar = (SeekBar) layoutView.findViewById(R.id.colorGseekbar);
		BGainBar = (SeekBar) layoutView.findViewById(R.id.colorBseekbar);
		Button RGB_BtnRevocation = (Button) layoutView
				.findViewById(R.id.revocation_setting);
		colorRValue = PreferencesUtils.getInt(context, "colorRValue", 128);
		colorGValue = PreferencesUtils.getInt(context, "colorGValue", 128);
		colorBValue = PreferencesUtils.getInt(context, "colorBValue", 128);
		RGainBar.setOnSeekBarChangeListener(this);
		GGainBar.setOnSeekBarChangeListener(this);
		BGainBar.setOnSeekBarChangeListener(this);
		RGB_BtnRevocation.setOnClickListener(this);
	}

	public void setProgress() {
		RGainBar.setProgress(colorRValue);
		GGainBar.setProgress(colorGValue);
		BGainBar.setProgress(colorBValue);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (NetWorkObject.getInstance().getNetStatus() == NetState.TCP_CONN_OPEN) {
			PreferencesUtils.putInt(context, "colorRValue", colorRValue);
			PreferencesUtils.putInt(context, "colorGValue", colorGValue);
			PreferencesUtils.putInt(context, "colorBValue", colorBValue);
		}
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.revocation_setting:
			colorRValue = PreferencesUtils.getInt(context, "colorRValue", 128);
			colorGValue = PreferencesUtils.getInt(context, "colorGValue", 128);
			colorBValue = PreferencesUtils.getInt(context, "colorBValue", 128);
			setProgress();
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
			break;
		case R.id.colorGseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			colorGValue = progress;
			break;
		case R.id.colorBseekbar:
			Log.i("onProgressChanged", "onProgressChanged =========" + progress);
			colorBValue = progress;
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

}
