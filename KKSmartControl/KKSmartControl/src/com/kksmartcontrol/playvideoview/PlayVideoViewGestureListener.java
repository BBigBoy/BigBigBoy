package com.kksmartcontrol.playvideoview;

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
			((Activity) videoView.getContext()).getFragmentManager()
					.popBackStack();

		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onLongPress(e);
		Toast.makeText(videoView.getContext(), "双击或滑动以返回预览窗口",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		// Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
		// Toast.makeText(videoView.getContext(), "双击或滑动以返回预览窗口",
		// Toast.LENGTH_LONG).show();

		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		((Activity) videoView.getContext()).getFragmentManager().popBackStack();
		// Toast.makeText(videoView.getContext(), "当前播放", Toast.LENGTH_SHORT)
		// .show();
		return true;
	}
}