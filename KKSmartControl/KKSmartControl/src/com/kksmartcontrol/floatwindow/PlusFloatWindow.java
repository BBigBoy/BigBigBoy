package com.kksmartcontrol.floatwindow;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.dialogfragment.AboutDialog;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PlusFloatWindow extends LinearLayout {

	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int windowViewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int windowViewHeight;
	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的布局
	 */
	private LinearLayout floatView;

	ImageButton imageButton1, imageButton2;
	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;
	Boolean ismoved = true;
	LinearLayout expandFloatView;

	public PlusFloatWindow(final Context context) {
		super(context);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window, this);
		floatView = (LinearLayout) findViewById(R.id.small_window_layout);
		expandFloatView = (LinearLayout) findViewById(R.id.control);
		windowViewWidth = floatView.getLayoutParams().width;
		windowViewHeight = floatView.getLayoutParams().height;
		imageButton1 = (ImageButton) findViewById(R.id.ImageButton1);
		imageButton2 = (ImageButton) findViewById(R.id.ImageButton2);
		imageButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutDialog aboutDialog = (AboutDialog) ((Activity) context)
						.getFragmentManager().findFragmentByTag("about");
				if (aboutDialog == null)
					aboutDialog = new AboutDialog();
				if (!aboutDialog.isVisible())
					aboutDialog.show(((Activity) context).getFragmentManager(),
							"about");

				floatView.setVisibility(View.VISIBLE);
				floatView.setSelected(false);
				mParams.width = expandFloatView.getWidth() / 3;
				mParams.x = MyWindowManager.plusFloatWindowWidth;
				windowManager.updateViewLayout(PlusFloatWindow.this, mParams);
				expandFloatView.setVisibility(View.GONE);
			}
		});
		imageButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtil.showToast(context, "用来进行应用基础设置....",
						Toast.LENGTH_LONG);
				floatView.setVisibility(View.VISIBLE);
				floatView.setSelected(false);
				mParams.width = expandFloatView.getWidth() / 3;
				windowManager.updateViewLayout(PlusFloatWindow.this, mParams);
				expandFloatView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (NetWorkObject.getInstance().getNetStatus() != NetState.TCP_CONN_OPEN) {
				NetWorkObject.getInstance().connectToServer();
				return true;
			}
			if (floatView.getVisibility() == View.VISIBLE) {
				floatView.setSelected(true);
			}
			ismoved = false;
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY();
			if (floatView.getVisibility() == View.VISIBLE)
				updateViewPosition(xInScreen - floatView.getWidth() / 2,
						yInScreen - floatView.getHeight());
			else
				updateViewPosition(xInScreen - expandFloatView.getWidth() - 20,
						yInScreen - expandFloatView.getHeight() / 2);
			ismoved = true;

			break;
		case MotionEvent.ACTION_UP:
			// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
			if (ismoved == false
					&& expandFloatView.getVisibility() == View.GONE) {
				ismoved = true;
				mParams.width = 3 * floatView.getWidth();
				windowManager.updateViewLayout(this, mParams);
				expandFloatView.setVisibility(View.VISIBLE);
				floatView.setVisibility(View.GONE);
			} else if (ismoved == false
					&& expandFloatView.getVisibility() == View.VISIBLE) {
				expandFloatView.setVisibility(View.GONE);
				floatView.setVisibility(View.VISIBLE);
				mParams.width = expandFloatView.getWidth() / 3;
				windowManager.updateViewLayout(this, mParams);
			}
			if (floatView.getVisibility() == View.VISIBLE) {
				floatView.setSelected(false);
			}

			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition(float x, float y) {
		mParams.x = (int) x;
		mParams.y = (int) y;
		windowManager.updateViewLayout(this, mParams);
	}

	public void setNetState(Boolean netstate) {
		if (netstate == false) {
			floatView.setBackgroundResource(R.drawable.neterror);

			LinearLayout backLayout = (LinearLayout) findViewById(R.id.backtofloating);
			backLayout.setBackgroundResource(R.drawable.neterror);

		} else {
			floatView.setBackgroundResource(R.drawable.thumb_floatingbg);
			floatView.setSelected(false);

			LinearLayout backLayout = (LinearLayout) findViewById(R.id.backtofloating);
			backLayout.setBackgroundResource(R.drawable.back);

		}
	}

}
