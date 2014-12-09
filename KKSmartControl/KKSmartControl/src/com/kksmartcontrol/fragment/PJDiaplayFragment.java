package com.kksmartcontrol.fragment;

import java.lang.ref.WeakReference;
import com.example.kksmartcontrol.R;
import com.kksmartcontrol.bean.Coordinate;
import com.kksmartcontrol.bean.KKSmartControlDataBean;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class PJDiaplayFragment extends Fragment {
	String TAG = this.getClass().getName();
	public static PJDiaplayFragment pjDiaplayFragment;
	private SoundPool soundpool;// 声明一个SoundPool
	private int music;// 定义一个整型用load（）；来设置suondID
	private Vibrator vibrator;// 振动
	LinearLayout pj_screen_layout = null;

	public static Handler controlHandler = null;

	private static class MyHandler extends Handler {
		WeakReference<PJDiaplayFragment> mfragment;

		public MyHandler(PJDiaplayFragment fragment) {
			mfragment = new WeakReference<PJDiaplayFragment>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PJDiaplayFragment fragment = mfragment.get();
			if (fragment != null) {
				switch (msg.what) {
				case 1:// 程序启动初始化拼接屏
					fragment.createPjScreen();
					break;
				case 2:// 点击提交后更新拼接屏
					fragment.pj_screen_layout.removeAllViews();
					fragment.createPjScreen();
					KKSmartControlDataBean.clearCoordinateList();// 拼接行列每变换一次都应清除
					break;

				default://
					break;
				}

			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 震动和声音
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		soundpool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		// soundpool = new
		// SoundPool.Builder().setMaxStreams(10).build();//使用builder创建soundpool是在Android5.0加入的
		music = soundpool.load(getActivity(), R.raw.ding_sel, 1); // 把声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		controlHandler = new MyHandler(this);
		pjDiaplayFragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		pj_screen_layout = new LinearLayout(getActivity());
		pj_screen_layout.setOrientation(LinearLayout.VERTICAL);
		controlHandler.sendEmptyMessage(1);
		return pj_screen_layout;
	}

	/**
	 * 根据给定行数与列数创建拼接屏layout，对应每个imageview的点击事件由对象PjImageViewClick确定
	 * 
	 * @param rows
	 *            保存的行数
	 * 
	 * @param columns
	 *            保存的列数
	 */
	private void createPjScreen() {

		int rows = KKSmartControlDataBean.getRowNum();
		int columns = KKSmartControlDataBean.getColumnNum();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		PjImageViewClick pjImageViewClick = new PjImageViewClick();

		for (int i = 1; i <= rows; i++) {
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 1; j <= columns; j++) {
				// 根据不同位置，控制不同margin
				ImageView iv = new ImageView(getActivity());
				iv.setBackgroundResource(R.drawable.thumb_pjimageview_bg);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setTag(j + "," + i);// 标签格式为“列值，行值”
				iv.setOnClickListener(pjImageViewClick);

				linearLayout.addView(iv, params);

			}
			pj_screen_layout.addView(linearLayout, params);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		controlHandler.removeCallbacksAndMessages(null);
		controlHandler = null;
		super.onDestroy();
	}

	/**
	 * 拼接屏每个imageview的点击对象事件
	 * 
	 * @author BigBigBoy
	 * 
	 */
	class PjImageViewClick implements OnClickListener {

		@Override
		public void onClick(View view) {
			String[] XYNUM = view.getTag().toString().split(",");
			Coordinate coordinate = new Coordinate();
			coordinate.X = Integer.parseInt(XYNUM[0]);
			coordinate.Y = Integer.parseInt(XYNUM[1]);

			if (!view.isSelected()) {
				view.setSelected(true);
				KKSmartControlDataBean.addToCoordinateList(coordinate);
				vibrator.vibrate(100);
				soundpool.play(music, 1, 1, 0, 0, 1);
			} else {
				view.setSelected(false);
				vibrator.vibrate(100);
				KKSmartControlDataBean.removeFromCoordinateList(coordinate);
				soundpool.play(music, 1, 1, 0, 0, 1);
			}
		}
	}

}
