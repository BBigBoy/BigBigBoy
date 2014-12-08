package com.kksmartcontrol.prevideoview;

import tv.danmaku.ijk.media.widget.VideoView;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.fragment.DestoryVideoFragment;
import com.kksmartcontrol.fragment.VideoPlayFragment;
import com.kksmartcontrol.fragment.VideoPreFragment;
import com.kksmartcontrol.util.MyDragShadowBuilder;

public class PreVideoViewGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	MyPreVideoView videoView;

	public PreVideoViewGestureListener(VideoView videoView) {
		this.videoView = (MyPreVideoView) videoView;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onLongPress(e);

		if (videoView.getContentDescription() == null) {
			Toast.makeText(videoView.getContext(), "当前无预览内容,无需移除 ！",
					Toast.LENGTH_SHORT).show();
			return;
		}

		ClipData dragData = ClipData.newPlainText("sourceName",
				videoView.getContentDescription());

		View dragView = View.inflate(videoView.getContext(),
				R.layout.dragshadow_prevideoview, null);
		// LayoutInflater.from(context).inflate(R.layout.dragshadow_prevideoview,
		// null);
		TextView dragviewText = (TextView) dragView
				.findViewById(R.id.dragviewtext);
		dragviewText.setText(videoView.getContentDescription());
		int width = (int) (videoView.getWidth() * 1.2);
		int height = (int) (videoView.getHeight() * 0.8);
		// 测量好大小
		dragView.measure(width, height);
		// 分派布局参数，在此之后才可以调用draw方法
		dragView.layout(0, 0, width, height);

		DragShadowBuilder myShadow = new MyDragShadowBuilder(dragView);

		MyPreVideoView localState = videoView;

		FragmentTransaction fragmentTransaction = ((Activity) videoView
				.getContext()).getFragmentManager().beginTransaction();
		if (DestoryVideoFragment.destoryVideoViewFragment == null) {
			DestoryVideoFragment.destoryVideoViewFragment = new DestoryVideoFragment();
			fragmentTransaction.add(R.id.listlayout,
					DestoryVideoFragment.destoryVideoViewFragment,
					"destoryfragment");
		}
		fragmentTransaction.show(DestoryVideoFragment.destoryVideoViewFragment);
		fragmentTransaction.commit();

		// Starts the drag
		videoView.startDrag(dragData, myShadow, localState, 0);

	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
		if (videoView.getContentDescription() != null) {
			Toast.makeText(videoView.getContext(),
					" 双击以确定播放  " + videoView.getContentDescription(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(videoView.getContext(), "当前窗口无播放预览",
					Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (videoView.isPlaying()) {
			FragmentTransaction fragmentTransaction = ((Activity) videoView
					.getContext()).getFragmentManager().beginTransaction();
			VideoPlayFragment videoPlayFragment = (VideoPlayFragment) ((Activity) videoView
					.getContext()).getFragmentManager().findFragmentByTag(
					"VideoPlayFragment");
			if (videoPlayFragment == null)
				videoPlayFragment = new VideoPlayFragment();
			fragmentTransaction.hide(VideoPreFragment.videoPreFragment);
			fragmentTransaction.add(R.id.videodisplay, videoPlayFragment,
					"VideoPlayFragment");
			// fragmentTransaction
			// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			Toast.makeText(videoView.getContext(), "当前窗口无播放预览,请先添加预览视频",
					Toast.LENGTH_SHORT).show();
		}
		return true;
	}
}