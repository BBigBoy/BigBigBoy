package com.kksmartcontrol.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.widget.Toast;

/**
 * 不够好，可以进一步修改
 * 
 * @author BBBoy
 * 
 */
public class ToastUtil {
	private static Toast mToast;
	private static Timer timer = new Timer();
	private static long time_length;

	public static void showToast(Context context, CharSequence text,
			int duration) {
		if (mToast != null) {
			mToast.cancel();
			mToast.setText(text);
			mToast.setDuration(duration);
		} else {
			mToast = Toast.makeText(context, text, duration);
		}
		if (duration == Toast.LENGTH_LONG)
			time_length = 3000;
		else
			time_length = 2500;
		mToast.show();
		timer.schedule(new TimerTask() {
			public void run() {
				mToast = null;
			}
		}, time_length);
	}

	public static void showToast(Context context, int resId, int duration) {
		showToast(context, context.getResources().getText(resId), duration);
	}

}
