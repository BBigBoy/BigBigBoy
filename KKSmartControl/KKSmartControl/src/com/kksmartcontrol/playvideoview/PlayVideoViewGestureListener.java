package com.kksmartcontrol.playvideoview;

import com.kksmartcontrol.util.FragmentUtil;
import com.kksmartcontrol.util.ToastUtil;

import tv.danmaku.ijk.media.widget.VideoView;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class PlayVideoViewGestureListener extends
		GestureDetector.SimpleOnGestureListener {
	VideoView videoView;

	public PlayVideoViewGestureListener(VideoView videoView) {
		this.videoView = videoView;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (Math.abs(e2.getX() - e1.getX()) > 360.0)
			FragmentUtil.showExistingFragmentByTag(((Activity) videoView.getContext()),
					"VideoPreFragment");

		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onLongPress(e);
		ToastUtil.showToast(videoView.getContext(), "双击或滑动以返回预览窗口",
				Toast.LENGTH_LONG);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		// Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
		// ToastUtil.showToast(videoView.getContext(), "双击或滑动以返回预览窗口",
		// Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		FragmentUtil.showExistingFragmentByTag(((Activity) videoView.getContext()),
				"VideoPreFragment");
		// ((Activity)
		// videoView.getContext()).getFragmentManager().popBackStack(
		// "VideoPlay", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		// ToastUtil.showToast(videoView.getContext(), "当前播放",
		// Toast.LENGTH_SHORT);
		return true;
	}
}