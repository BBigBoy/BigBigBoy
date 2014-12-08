package com.kksmartcontrol.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyWindowManager {
	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;

	private static PlusFloatWindow plusFloatWindow;
	private static LayoutParams plusFloatWindowParams;
	static int plusFloatWindowWidth;

	/**
	 * 创建plusFloatWindow。初始位置为屏幕的右部中间位置。
	 */
	public static void createPlusFloatWindow(Context activityContext) {
		// WindowManager使用Application，这样才能弹出其他Dialog
		Context applicationContext = activityContext.getApplicationContext();
		WindowManager windowManager = getWindowManager(applicationContext);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		plusFloatWindowWidth = outMetrics.widthPixels;
		int screenHeight = outMetrics.heightPixels;

		if (plusFloatWindow == null) {
			// 使用activityContext，弹出Dialog
			plusFloatWindow = new PlusFloatWindow(activityContext);
			if (plusFloatWindowParams == null) {
				plusFloatWindowParams = new LayoutParams();
				plusFloatWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
				plusFloatWindowParams.format = PixelFormat.RGBA_8888;
				plusFloatWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				plusFloatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				plusFloatWindowParams.width = PlusFloatWindow.windowViewWidth;
				plusFloatWindowParams.height = PlusFloatWindow.windowViewHeight;
				plusFloatWindowParams.x = plusFloatWindowWidth;
				plusFloatWindowParams.y = screenHeight / 2;
			}
			plusFloatWindow.setParams(plusFloatWindowParams);
			windowManager.addView(plusFloatWindow, plusFloatWindowParams);
		}
	}

	/**
	 * 通过代理的方式，改变plusFloatWindow的显示状态
	 * 
	 * @param netstate
	 */
	public static void setNetState(Boolean netstate) {
		plusFloatWindow.setNetState(netstate);
	}

	/**
	 * 隐藏悬浮窗
	 */
	public static void hidePlusFloatWindow() {
		plusFloatWindow.setVisibility(View.GONE);
	}

	/**
	 * 显示悬浮窗
	 */
	public static void displayPlusFloatWindow() {
		plusFloatWindow.setVisibility(View.VISIBLE);
	}

	/**
	 * 移除悬浮窗
	 */
	public static void removePlusFloatWindow() {
		if (plusFloatWindow != null) {
			mWindowManager.removeView(plusFloatWindow);
			plusFloatWindow = null;
			plusFloatWindowParams = null;
		}
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

}
