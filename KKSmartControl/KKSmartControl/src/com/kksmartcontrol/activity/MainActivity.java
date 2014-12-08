package com.kksmartcontrol.activity;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.dialog.ExitDialog;
import com.kksmartcontrol.floatwindow.MyWindowManager;
import com.kksmartcontrol.fragment.ControlSettingFragment;
import com.kksmartcontrol.fragment.MediaPlayListFragment;
import com.kksmartcontrol.fragment.PJDiaplayFragment;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.pagersliding.PagerSlidingTabStrip;
import com.kksmartcontrol.util.MySharedPreferences;
import com.kksmartcontrol.util.PreferencesUtils;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;

public class MainActivity extends FragmentActivity {

	public String TAG = this.getClass().getName();

	private ControlSettingFragment controlFragment;

	private MediaPlayListFragment mediaplayListFragment;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 判断程序若是首次启动，则进入引导页
		if (!MySharedPreferences.GetIsOpen(getApplicationContext())) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), HelpActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_main);
		NetWorkObject.context = this;

		KKSmartControlDataBean.setRowNum(PreferencesUtils.getInt(this,
				"rowNum", 2));
		KKSmartControlDataBean.setColumnNum(PreferencesUtils.getInt(this,
				"columnNum", 2));

		fragmentTransaction = getFragmentManager().beginTransaction();

		PJDiaplayFragment pjDiaplayFragment = new PJDiaplayFragment();
		fragmentTransaction.replace(R.id.videodisplay, pjDiaplayFragment);
		fragmentTransaction.commit();

		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();
		// 创建悬浮窗
		MyWindowManager.createPlusFloatWindow(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 显示悬浮窗
		MyWindowManager.displayPlusFloatWindow();
		if (NetWorkObject.getInstance().getNetStatus() == NetState.TCP_CONN_OPEN) {
			// device_con.setSelected(true);
			// MyWindowManager.setNetState(true);
		} else {
			// device_con.setSelected(false);
			MyWindowManager.setNetState(false);
			NetWorkObject.getInstance().connectToServer();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		PreferencesUtils.putInt(this, "rowNum",
				KKSmartControlDataBean.getRowNum());
		PreferencesUtils.putInt(this, "columnNum",
				KKSmartControlDataBean.getColumnNum());
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 隐藏悬浮窗
		MyWindowManager.hidePlusFloatWindow();
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 移除悬浮窗
		MyWindowManager.removePlusFloatWindow();
		super.onDestroy();
	}

	// public void addPreVideoViewFragment() {
	// fragmentTransaction.add(R.id.videodisplay, new Fragment(), "preview");
	//
	// }

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) (TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, dm) / 1.5));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// Tab的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "设    置", "播    放" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (controlFragment == null) {
					controlFragment = new ControlSettingFragment();
				}
				return controlFragment;
			case 1:
				if (mediaplayListFragment == null) {
					mediaplayListFragment = new MediaPlayListFragment();
				}
				return mediaplayListFragment;

			default:
				return null;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// displayDialog(context, R.layout.exitpopdialog);
			new ExitDialog().show(getFragmentManager(), "Exit");
		}
		return false;
	}
}