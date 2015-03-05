package com.kksmartcontrol.activity;

import java.util.ArrayList;
import java.util.List;
import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.bean.Coordinate;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.dialogfragment.ExitDialog;
import com.kksmartcontrol.fragment.ControlSettingFragment;
import com.kksmartcontrol.fragment.MediaPlayListFragment;
import com.kksmartcontrol.fragment.NetErrIndicateFragment;
import com.kksmartcontrol.fragment.PlusFragment;
import com.kksmartcontrol.fragment.VideoPreFragment;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.pagersliding.PagerSlidingTabStrip;
import com.kksmartcontrol.pjscreenview.PJCell;
import com.kksmartcontrol.pjscreenview.PJScreenView;
import com.kksmartcontrol.preference.PreferencesUtils;
import com.kksmartcontrol.util.FragmentUtil;
import com.kksmartcontrol.util.PjScreenViewInterface;
import com.kksmartcontrol.util.ToastUtil;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		PagerSlidingTabStrip.RefreshActivity, PjScreenViewInterface {

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
	private DisplayMetrics displayMetrics;

	android.app.FragmentTransaction fragmentTransaction;

	private TextView titleText;
	ObjectAnimator titleTextAnimator;
	private Handler mHandler = new Handler();

	PJScreenView pjScreenView;

	ImageView plusImage;
	ObjectAnimator titleAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		titleText = (TextView) findViewById(R.id.titletext);
		titleTextAnimator = ObjectAnimator.ofFloat(titleText, "alpha", 0.3f,
				1.0f).setDuration(1300);
		NetWorkObject.context = this;
		Log.i("second", "onCreate");
		plusImage = (ImageView) findViewById(R.id.plus);
		titleAnimator = ObjectAnimator.ofFloat(plusImage, "rotation", 0, -45)
				.setDuration(600);
		pjScreenView = (PJScreenView) findViewById(R.id.pjscreenview);

		pjScreenView.setSplicesMode(KKSmartControlDataBean.getRowNum(),
				KKSmartControlDataBean.getColumnNum());
		displayMetrics = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (NetWorkObject.getInstance().getNetStatus() != NetState.TCP_CONN_OPEN) {
			showNetErrIndicate();
		} else {
			ToastUtil.showToast(this, "已连接到服务器 ！", Toast.LENGTH_LONG);
		}
	}

	/**
	 * 隐藏网络未连接提示
	 */
	public void hideNetErrIndicate() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FragmentUtil.hideFragmentByTag(MainActivity.this,
						"NetErrIndicate");
			}
		});

	}
  
	/**
	 * 显示网络未连接提示
	 */
	public void showNetErrIndicate() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (FragmentUtil.hasFragmentSpecifiedByTag(MainActivity.this,
						"NetErrIndicate")) {
					FragmentUtil.showExistingFragmentByTag(MainActivity.this,
							"NetErrIndicate");
				} else {
					FragmentUtil.addFragmentWithTag(MainActivity.this,
							NetErrIndicateFragment.class, R.id.displaylayout,
							"NetErrIndicate", R.animator.neterrfragmententer);
				}
			}
		});

	}

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// if (NetWorkObject.getInstance().getNetStatus() != NetState.TCP_CONN_OPEN)
	// {
	// MyWindowManager.setNetState(false);
	// NetWorkObject.getInstance().connectToServer();
	// Log.i("second", "onResume  != NetState.TCP_CONN_OPEN");
	// }
	// Log.i("second", "onResume");
	// // 显示悬浮窗
	// MyWindowManager.displayPlusFloatWindow();
	// }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		PreferencesUtils.putInt(this, "rowNum",
				KKSmartControlDataBean.getRowNum());
		PreferencesUtils.putInt(this, "columnNum",
				KKSmartControlDataBean.getColumnNum());
		Log.i("second", "onPause");
		super.onPause();
	}

	public void plusClick(View v) {
		if (!v.isSelected()) {
			v.setSelected(true);
			titleAnimator.start();
			FragmentUtil.addFragmentWithTag(this, PlusFragment.class,
					R.id.listlayout, "plusFragment",
					R.animator.plusfragmententer);
		} else {
			v.setSelected(false);
			titleAnimator.reverse();
			FragmentUtil.removeFragmentByTag(this, "plusFragment",
					R.animator.plusfragmentexit);
		}
	}

	/**
	 * 图像模式设置、背光、色温控制的fragment中的关闭按钮事件
	 * 
	 * @param v
	 */
	public void onCloseBtnClick(View v) {
		FragmentUtil.removeVisibleFragmentByTag(this,
				getResources().getString(R.string.manualsetting),
				R.animator.fragmentexit);

	}

	@Override
	public void refreshDisplayLayout(final int position) {
		// TODO Auto-generated method stub
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (position == 0) {
					FragmentUtil.removeFragmentByTag(MainActivity.this,
							"VideoPreFragment", R.animator.fragmentexit);
					FragmentUtil.removeFragmentByTag(MainActivity.this,
							"VideoPlayFragment", R.animator.fragmentexit);
					// MainActivity.this.getFragmentManager().popBackStack(
					// "VideoPlay",
					// FragmentManager.POP_BACK_STACK_INCLUSIVE);

				} else {
					FragmentUtil.addFragmentWithTag(MainActivity.this,
							VideoPreFragment.class, R.id.displaylayout,
							"VideoPreFragment", R.animator.plusfragmententer);
				}
			}
		});
	}

	@Override
	public void refreshTextAlpha(final float alpha) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				titleText.setAlpha(alpha);
			}
		});
	}

	@Override
	public void refreshTitle(final int position) {
		// TODO Auto-generated method stub
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (position == 1) {
					titleText.setText("视频播放");
					plusImage.setVisibility(View.GONE);
				} else {
					titleText.setText("拼接控制");
					plusImage.setVisibility(View.VISIBLE);
				}
				titleTextAnimator.start();
			}
		});
	}

	@Override
	public void setPJSplicesMode(int ROW, int COLUMN) {
		pjScreenView.setSplicesMode(ROW, COLUMN);
	}

	@Override
	public void setInputSignl(String inputSignl) {
		pjScreenView.setInputSignl(inputSignl);
	}

	@Override
	public List<Coordinate> getCoordinateList() {
		// TODO Auto-generated method stub

		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		List<PJCell> selectList = pjScreenView.getSelectList();
		Log.d("getCoordinateListlllll", "\n\nselectList.size()---->"
				+ selectList.size());
		for (PJCell cell : selectList) {
			coordinateList.add(new Coordinate(cell.rowNum, cell.columnNum));
			Log.d("getCoordinateList", "\n\n---->" + cell);
		}
		Log.d("getCoordinateListlllll", "\n\ncoordinateList.size()---->"
				+ coordinateList.size());
		return coordinateList;

	}

	@Override
	public boolean isSelectListEmpty() {
		// TODO Auto-generated method stub
		return pjScreenView.getSelectList().isEmpty();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		NetWorkObject.getInstance().setNetStatus(NetState.NET_STATUS_ERR);
		NetWorkObject.getInstance().unInitNetClient();
		NetWorkObject.context = null;
		Log.i("second", "onDestroy");
		super.onDestroy();
	}

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
				TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) (TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics) / 1.5));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics));
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
			if (FragmentUtil
					.removeVisibleFragmentByTag(this,
							getResources().getString(R.string.manualsetting),
							R.animator.fragmentexit))
				return false;
			if (plusImage.isSelected()) {
				plusImage.setSelected(false);
				titleAnimator.reverse();
				FragmentUtil.removeFragmentByTag(this, "plusFragment",
						R.animator.plusfragmentexit);
				return false;
			}
			// displayDialog(context, R.layout.exitpopdialog);
			new ExitDialog().show(getFragmentManager(), "Exit");
		}
		return false;
	}

}