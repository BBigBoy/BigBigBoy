package com.kksmartcontrol.activity;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.preference.MySharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class SplashActivity extends Activity {

	private Handler mHandler = new Handler();

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
		View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						goHome();
					}
				}, 500);
			}
		});
	}

	protected void onResume() {
		super.onResume();
	}

	private void goHome() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		super.finish();
	}

}
