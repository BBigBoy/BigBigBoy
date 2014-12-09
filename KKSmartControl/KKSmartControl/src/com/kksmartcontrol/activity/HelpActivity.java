package com.kksmartcontrol.activity;

import java.util.ArrayList;
import com.example.kksmartcontrol.R;
import com.kksmartcontrol.preference.MySharedPreferences;
import com.kksmartcontrol.util.ViewPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @ClassName: HelpActivity
 * @Description: TODO 引导页
 * 
 */
public class HelpActivity extends Activity implements OnClickListener,
	OnPageChangeListener {
    // private final String TAG = this.getClass().getName();
    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private ViewGroup main, group;
    private ImageView[] dots;
    private ImageButton imgbtn;
    private int currentIndex;
    private ViewPagerAdapter vpAdapter;
    private ImageView imageView;
    private static final int GO_BTN = 10;

    private int[] help_pics = { R.drawable.help1, R.drawable.help2,
	    R.drawable.help3, R.drawable.help4 };

    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	this.pageViews = new ArrayList<View>();
	LayoutInflater inflater = getLayoutInflater();
	this.main = (ViewGroup) inflater.inflate(R.layout.activity_help, null);
	this.dots = new ImageView[this.help_pics.length];

	LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.WRAP_CONTENT,
		LinearLayout.LayoutParams.WRAP_CONTENT);

	this.group = (ViewGroup) this.main.findViewById(R.id.viewGroup);
	this.viewPager = (ViewPager) this.main.findViewById(R.id.guidePages);
	for (int i = 0; i < this.help_pics.length; i++) {
	    RelativeLayout rl = new RelativeLayout(this);
	    rl.setLayoutParams(mParams);
	    rl.setBackgroundResource(this.help_pics[i]);

	    this.imageView = new ImageView(HelpActivity.this);
	    this.imageView.setLayoutParams(new LayoutParams(20, 20));
	    this.imageView.setPadding(20, 10, 20, 10);
	    this.imageView.setBackgroundResource(R.drawable.thumb_dot);
	    this.dots[i] = this.imageView;
	    this.dots[i].setEnabled(false);
	    this.dots[i].setOnClickListener(this);

	    group.addView(this.dots[i]);
	    this.currentIndex = 0;
	    this.dots[this.currentIndex].setEnabled(true);

	    if (i == (this.help_pics.length - 1)) {
		this.imgbtn = new ImageButton(this);
		this.imgbtn.setBackgroundResource(R.drawable.thumb_help_gobtn);
		this.imgbtn.setOnClickListener(this);
		this.imgbtn.setTag(GO_BTN);
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp1.bottomMargin = 200;
		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL,
			RelativeLayout.TRUE);
		rl.addView(imgbtn, lp1);
	    }
	    this.pageViews.add(rl);
	    // ExitApplication.getInstance().addActivity(this);
	}
	this.vpAdapter = new ViewPagerAdapter(this.pageViews);
	this.viewPager.setAdapter(this.vpAdapter);
	this.viewPager.setOnPageChangeListener(this);
	setContentView(this.main);
	// initDots();
    }

    private void setCurDot(int positon) {
	if (positon < 0 || positon > this.help_pics.length - 1
		|| this.currentIndex == positon) {
	    return;
	}
	this.dots[positon].setEnabled(true);
	this.dots[currentIndex].setEnabled(false);
	this.currentIndex = positon;
    }

    public void onPageScrollStateChanged(int arg0) {
	// TODO Auto-generated method stub
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
	// TODO Auto-generated method stub
    }

    public void onPageSelected(int arg0) {
	setCurDot(arg0);
    }

    public void onClick(View v) {

	ToHome();
    }

    public void ToHome() {
	SaveIsOpen(true);
	Intent intentHome = new Intent();
	intentHome.setClass(HelpActivity.this, MainActivity.class);
	startActivity(intentHome);
	finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    finish();
	}
	return super.onKeyDown(keyCode, event);
    }

    private void SaveIsOpen(boolean IsOpen) {
	MySharedPreferences.SaveIsOpen(IsOpen, getApplicationContext());
    }
}