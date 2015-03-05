package com.kksmartcontrol.fragment;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.activity.MainActivity;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.net.ParameDataHandle.SystemFuntion;
import com.kksmartcontrol.netcmd.SetPJ_Infor;
import com.kksmartcontrol.preference.PreferencesUtils;
import com.kksmartcontrol.util.FragmentUtil;
import com.kksmartcontrol.util.PjScreenViewInterface;
import com.kksmartcontrol.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ControlSettingFragment extends Fragment implements OnClickListener {
	EditText setRow = null;
	EditText setColumn = null;
	Button submitBut, dviBtn, hdmiBtn, ypbprBtn, vgaBtn, cvbsBtn,
			picmode_standardBtn, picmode_dynamicBtn, picmode_manualBtn,
			color_coldBtn, color_warmBtn, color_manualBtn, backlight_adjBtn,
			set_sleepBtn, set_wake;
	Context context = null;
	SetPJ_Infor setPJ_Infor = null;// 设置拼接屏拼命令对象
	PjScreenViewInterface pjScreenView;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
		pjScreenView = ((MainActivity) getActivity());
		setPJ_Infor = SetPJ_Infor.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.controlsetting, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		InitView(getView());
		initButtonView(getView());
	}

	private void InitView(View view) {
		// 拼接控制控件初始化
		// setRow = (EditText) view.findViewById(R.id.edit_row);
		// setColumn = (EditText) view.findViewById(R.id.edit_column);
		// Edit_Text输入完成后判断字符是否合法，并作出相应处理
		// setRow.setOnKeyListener(new EditText_KeyListener(context));
		// setColumn.setOnKeyListener(new EditText_KeyListener(context));
		// setRow.setText(String.valueOf(KKSmartControlDataBean.getRowNum()));
		// setColumn
		// .setText(String.valueOf(KKSmartControlDataBean.getColumnNum()));
		// setRow.setSelection(setRow.getText().length());
		// setColumn.setSelection(setColumn.getText().length());
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		Log.d("onClick", "onClickonClickonClickonClickonClick");

		// // 改变拼接方式按钮不需要联网，不需要选中屏幕
		// if (view.getId() == R.id.set_submit) {
		// submitBtnClick(view);
		// return;
		// }
		// 每次点击按钮均判断网络是否连接正常
		if (NetWorkObject.getInstance().getNetStatus() != NetState.TCP_CONN_OPEN) {
			ToastUtil
					.showToast(context, "当前未与服务器正常连接，请连接！", Toast.LENGTH_SHORT);
			return;
		}
		if (pjScreenView.isSelectListEmpty()) {
			ToastUtil.showToast(context, "请选择需要设置的屏幕", Toast.LENGTH_SHORT);
			return;
		}
		switch (view.getId()) {
		case R.id.source_pj_dvi:
			setPjCmd(SystemFuntion.SET_PJ_DVI);
			pjScreenView.setInputSignl("DVI");
			break;
		case R.id.source_pj_hdmi:
			setPjCmd(SystemFuntion.SET_PJ_HDMI);
			pjScreenView.setInputSignl("HDMI");
			break;
		case R.id.source_pj_ypbpr:
			setPjCmd(SystemFuntion.SET_PJ_YPBPR);
			pjScreenView.setInputSignl("YPBPR");
			break;
		case R.id.source_pj_vga:
			setPjCmd(SystemFuntion.SET_PJ_VGA);
			pjScreenView.setInputSignl("VGA");
			break;
		case R.id.source_pj_cvbs:
			setPjCmd(SystemFuntion.SET_PJ_CVBS);
			pjScreenView.setInputSignl("CVBS");
			break;
		case R.id.picmode_standard:
			setPJ_Infor.setPjFunctionPacket(pjScreenView.getCoordinateList(),
					SystemFuntion.SET_PICMODE_STANDARD, (byte) 0x31,
					(byte) 0x00, (byte) 0x00);
			break;
		case R.id.picmode_dynamic:
			setPJ_Infor.setPjFunctionPacket(pjScreenView.getCoordinateList(),
					SystemFuntion.SET_PICMODE_DYNAMIC, (byte) 0x31,
					(byte) 0x00, (byte) 0x04);
			break;
		case R.id.picmode_manual:
			// new AdjustPicmodeDialog().show(getFragmentManager(),
			// "PicModeManualSet");
			FragmentUtil.addFragmentWithTag(context,
					AdjustPicModeFragment.class, R.id.listlayout,
					getResources().getString(R.string.manualsetting), 0); 
			break;
		case R.id.color_cold:
			setPJ_Infor.adjustColorMode(pjScreenView.getCoordinateList(),
					SystemFuntion.SET_COLORMODE_6500K, (byte) 0x31,
					(byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0xFF,
					(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
			break;
		case R.id.color_warm:
			setPJ_Infor.adjustColorMode(pjScreenView.getCoordinateList(),
					SystemFuntion.SET_COLORMODE_9300K, (byte) 0x31,
					(byte) 0x00, (byte) 0x03, (byte) 0xF0, (byte) 0xFF,
					(byte) 0xE6, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
			break;
		case R.id.color_manual:
			// new AdjustRGBDialog().show(getFragmentManager(), "RGBManualSet");
			FragmentUtil.addFragmentWithTag(context, AdjustRGBFragment.class,
					R.id.listlayout,
					getResources().getString(R.string.manualsetting), 0);
			break;
		case R.id.backlight_adj:
			// new BacklightDialog().show(getFragmentManager(), "BacklightSet");
			FragmentUtil.addFragmentWithTag(context,
					AdjustBacklightFragment.class, R.id.listlayout,
					getResources().getString(R.string.manualsetting), 0);
			break;
		case R.id.set_sleep:
			setPJ_Infor.setPjFunctionSleep((byte) 0xFC,
					SystemFuntion.SET_SYSTEM_SLEEP, (byte) 0x31, (byte) 0x00,
					(byte) 0x00);
			break;
		case R.id.set_wake:
			setPJ_Infor.setPjFunctionSleep((byte) 0xFC,
					SystemFuntion.SET_SYSTEM_SLEEP, (byte) 0x31, (byte) 0x00,
					(byte) 0x01);
			break;
		default:
			break;
		}
	}

	/**
	 * 拼接屏行数与列数提交按钮触发事件
	 */
	public void submitBtnClick(View view) {

		int rowNum = Integer.parseInt(setRow.getText().toString());
		int columnNum = Integer.parseInt(setColumn.getText().toString());

		KKSmartControlDataBean.setRowNum(rowNum);
		KKSmartControlDataBean.setColumnNum(columnNum);

		PreferencesUtils.putInt(context, "rowNum", rowNum);
		PreferencesUtils.putInt(context, "columnNum", columnNum);
		android.app.FragmentTransaction fragmentTransaction = getActivity()
				.getFragmentManager().beginTransaction();
		VideoPlayFragment videoPlayFragment = (VideoPlayFragment) getActivity()
				.getFragmentManager().findFragmentByTag("VideoPlayFragment");
		if (videoPlayFragment != null)
			getActivity().getFragmentManager().popBackStack();
		VideoPreFragment videoPreFragment = (VideoPreFragment) getActivity()
				.getFragmentManager().findFragmentByTag("VideoPreFragment");
		if (videoPreFragment != null) {
			fragmentTransaction.hide(videoPreFragment);
			fragmentTransaction.commit();
		}
		pjScreenView.setPJSplicesMode(rowNum, columnNum);
	}

	/**
	 * 设置拼接屏信号输入
	 * 
	 * @param function
	 *            标识具体设置的拼接信号输入 DVI、HDMI、YPBPR、VGA、CVBS
	 */
	private void setPjCmd(SystemFuntion function) {

		setPJ_Infor.setPjSource(pjScreenView.getCoordinateList(), function,
				(byte) 0x31);

	}

	private void initButtonView(View view) {
		// submitBut = (Button) view.findViewById(R.id.set_submit);
		dviBtn = (Button) view.findViewById(R.id.source_pj_dvi);
		hdmiBtn = (Button) view.findViewById(R.id.source_pj_hdmi);
		ypbprBtn = (Button) view.findViewById(R.id.source_pj_ypbpr);
		vgaBtn = (Button) view.findViewById(R.id.source_pj_vga);
		cvbsBtn = (Button) view.findViewById(R.id.source_pj_cvbs);
		picmode_standardBtn = (Button) view.findViewById(R.id.picmode_standard);
		picmode_dynamicBtn = (Button) view.findViewById(R.id.picmode_dynamic);
		picmode_manualBtn = (Button) view.findViewById(R.id.picmode_manual);
		color_coldBtn = (Button) view.findViewById(R.id.color_cold);
		color_warmBtn = (Button) view.findViewById(R.id.color_warm);
		color_manualBtn = (Button) view.findViewById(R.id.color_manual);
		backlight_adjBtn = (Button) view.findViewById(R.id.backlight_adj);
		set_sleepBtn = (Button) view.findViewById(R.id.set_sleep);
		set_wake = (Button) view.findViewById(R.id.set_wake);
		// submitBut.setOnClickListener(this);
		dviBtn.setOnClickListener(this);
		hdmiBtn.setOnClickListener(this);
		ypbprBtn.setOnClickListener(this);
		vgaBtn.setOnClickListener(this);
		cvbsBtn.setOnClickListener(this);
		picmode_standardBtn.setOnClickListener(this);
		picmode_dynamicBtn.setOnClickListener(this);
		picmode_manualBtn.setOnClickListener(this);
		color_coldBtn.setOnClickListener(this);
		color_warmBtn.setOnClickListener(this);
		color_manualBtn.setOnClickListener(this);
		backlight_adjBtn.setOnClickListener(this);
		set_sleepBtn.setOnClickListener(this);
		set_wake.setOnClickListener(this);
	}

}
