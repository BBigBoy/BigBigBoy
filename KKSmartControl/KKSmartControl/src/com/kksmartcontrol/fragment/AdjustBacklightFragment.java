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

public class AdjustBacklightFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	SetPJ_Infor setPj_Infor = SetPJ_Infor.getInstance();// 设置拼接屏拼命令对象
	Context context = null;
	int backlightValue;
	SeekBar backlight_GainBar;
	PjScreenViewInterface coordinateList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		coordinateList = ((MainActivity) getActivity());
		View layoutView = inflater.inflate(R.layout.adjustbacklight, container,
				false);
		InitView(layoutView);
		((TextView) layoutView.findViewById(R.id.fragmenttitletext))
				.setText(getResources().getString(R.string.backlight));
		setProgress();
		return layoutView;
	}

	private void InitView(View layoutView) {
		backlight_GainBar = (SeekBar) layoutView
				.findViewById(R.id.backlightseekbar);
		Button backlight_BtnCancel = (Button) layoutView
				.findViewById(R.id.revocation_setting);
		backlightValue = PreferencesUtils
				.getInt(context, "backlightValue", 100);
		backlight_GainBar.setProgress(backlightValue);
		backlight_GainBar.setOnSeekBarChangeListener(this);
		backlight_BtnCancel.setOnClickListener(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (NetWorkObject.getInstance().getNetStatus() == NetState.TCP_CONN_OPEN) {
			PreferencesUtils.putInt(context, "backlightValue", backlightValue);
		}
		super.onPause();
	}

	public void setProgress() {
		backlight_GainBar.setProgress(backlightValue);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.revocation_setting:
			backlightValue = PreferencesUtils.getInt(context, "backlightValue",
					100);
			// 撤销后还原进度
			setProgress();
			setPj_Infor.setPjFunctionPacket(coordinateList.getCoordinateList(),
					SystemFuntion.SET_ADJUST_BACKLIGHT, (byte) 0x31,
					(byte) 0x00, (byte) backlightValue);
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
		// switch (seekBar.getId()) {
		// case R.id.backlightseekbar:
		Log.i("onProgressChanged", "onProgressChanged =========" + progress);
		backlightValue = progress;
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

}
