package com.kksmartcontrol.prevideoview;

import tv.danmaku.ijk.media.widget.VideoView;
import android.content.ClipData;
import android.support.v4.app.FragmentActivity;
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
import com.kksmartcontrol.util.FragmentUtil;
import com.kksmartcontrol.util.MyDragShadowBuilder;
import com.kksmartcontrol.util.ToastUtil;

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
			ToastUtil.showToast(videoView.getContext(), "当前无预览内容,无需移除 ！",
					Toast.LENGTH_SHORT);
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
		FragmentUtil.addFragmentWithTag(videoView.getContext(),
				DestoryVideoFragment.class, R.id.listlayout, "destoryfragment",
				0);
		// Starts the drag
		videoView.startDrag(dragData, myShadow, localState, 0);

	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
		if (videoView.getContentDescription() != null) {
			ToastUtil.showToast(videoView.getContext(), " 双击以确定播放  "
					+ videoView.getContentDescription(), Toast.LENGTH_SHORT);
		} else {
			ToastUtil.showToast(videoView.getContext(), "当前窗口无播放预览",
					Toast.LENGTH_SHORT);
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
			// VideoPreFragment videoPreFragment = (VideoPreFragment)
			// ((FragmentActivity) videoView
			// .getContext()).getFragmentManager().findFragmentByTag(
			// "VideoPreFragment");
			// android.app.FragmentTransaction fragmentTransaction =
			// ((FragmentActivity) videoView
			// .getContext()).getFragmentManager().beginTransaction();
			// VideoPlayFragment videoPlayFragment = (VideoPlayFragment)
			// ((FragmentActivity) videoView
			// .getContext()).getFragmentManager().findFragmentByTag(
			// "VideoPlayFragment");
			// if (videoPlayFragment == null)
			// videoPlayFragment = new VideoPlayFragment();
			// // fragmentTransaction.hide(videoPreFragment);
			// fragmentTransaction.add(R.id.displaylayout, videoPlayFragment,
			// "VideoPlayFragment");
			// fragmentTransaction.addToBackStack("VideoPlay");
			// fragmentTransaction.commit();
			if (!FragmentUtil.showExistingFragmentByTag(
					((FragmentActivity) videoView.getContext()),
					"VideoPlayFragment")) {
				FragmentUtil.addFragmentWithTag(
						((FragmentActivity) videoView.getContext()),
						VideoPlayFragment.class, R.id.displaylayout,
						"VideoPlayFragment", 0);
			}

		} else {
			ToastUtil.showToast(videoView.getContext(), "当前窗口无播放预览,请先添加预览视频",
					Toast.LENGTH_SHORT);
		}
		return true;
	}
}