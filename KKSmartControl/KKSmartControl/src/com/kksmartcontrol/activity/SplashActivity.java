package com.kksmartcontrol.activity;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.NetState;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.preference.MySharedPreferences;
import com.kksmartcontrol.preference.PreferencesUtils;
import com.roger.match.library.MatchTextView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private Handler mHandler = new Handler();

	private AnimatorSet animationSet = new AnimatorSet();

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
		setContentView(R.layout.activity_start);
		NetWorkObject.application = getApplication();

		KKSmartControlDataBean.setRowNum(PreferencesUtils.getInt(this,
				"rowNum", 2));
		KKSmartControlDataBean.setColumnNum(PreferencesUtils.getInt(this,
				"columnNum", 2));

		TextView textView = (TextView) findViewById(R.id.textView1);
		ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(textView,
				"alpha", 0.3f, 1.0f).setDuration(3000);
		ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(textView,
				"scaleX", 0.3f, 1.0f).setDuration(3000);
		ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(textView,
				"scaleY", 0.3f, 1.0f).setDuration(3000);
		ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(textView,
				"rotationX", 0, 90).setDuration(1500);
		objectAnimator4.setStartDelay(1000);
		animationSet.play(objectAnimator1).with(objectAnimator2)
				.with(objectAnimator3);
		animationSet.play(objectAnimator4).after(objectAnimator3);
		// animationSet.addListener(new AnimatorListenerAdapter() {
		// /**
		// * {@inheritDoc}
		// *
		// * @param animation
		// */
		// @Override
		// public void onAnimationEnd(Animator animation) {
		// super.onAnimationEnd(animation);
		// mHandler.post(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// goToMainActivity();
		// }
		// });
		// }
		// });

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		animationSet.start();
		if (NetWorkObject.getInstance().getNetStatus() != NetState.TCP_CONN_OPEN) {
			NetWorkObject.getInstance().connectToServer();
			Log.i("second", "onResume  != NetState.TCP_CONN_OPEN");
		}
		mHandler.postDelayed((new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				goToMainActivity();
			}
		}), 6000);
	}

	/**
	 * 转到MainActivity
	 */
	private void goToMainActivity() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		super.finish();
	}

}
