package com.kksmartcontrol.prevideoview;

import com.example.kksmartcontrol.R;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

public class VideoView_DragListener implements OnDragListener {

	String TAG = this.getClass().getName();
	MyPreVideoView videoView;

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		videoView = (MyPreVideoView) v;
		// 如果当前播放器正在播放就不更新背景
		Boolean isplaying = videoView.isPlaying();
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED");

			if (event.getLocalState().toString().equals("fromListViewItem")) {
				v.setSelected(true);
				return true;
			}

			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			if (!isplaying)
				v.setBackgroundResource(R.drawable.screen_bg_hoved);
			Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			if (!isplaying) {
				v.setBackgroundResource(R.drawable.thumb_videoview);
				v.setSelected(true);
			}
			Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
			break;
		// case DragEvent.ACTION_DRAG_LOCATION:
		// Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
		// break;
		case DragEvent.ACTION_DRAG_ENDED:
			v.setSelected(false);
			Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED");
			break;
		case DragEvent.ACTION_DROP:

			videoView.setContentDescription(event.getClipData().getItemAt(0)
					.getText().toString());
			Log.d(TAG, "Action is DragEvent.ACTION_DROP"
					+ event.getClipData().getItemAt(0).getText().toString());
			// 需要播放的视频名称
			videoViewToPlay(videoView);
			// VideoPreFragment.videoPreFragment.playingList
			// .add(videoView);
			break;
		default:
			break;
		}
		return false;

	}

	/**
	 * 播放视频
	 * 
	 * @param preVideoView
	 *            播放视频的preVideo
	 */
	private void videoViewToPlay(MyPreVideoView videoView) {
		// 设置播放地址
		// videoView.setVideoURI(Uri
		// .parse("android.resource://com.example.kksmartcontrol/"
		// + R.drawable.lit));

		videoView.setVideoURI(Uri.parse(Environment
				.getExternalStorageDirectory().getPath() + "/video/5.MP4"));

		// videoView.setVideoURI(Uri.parse("/sdcard/Video/5.MP4"));

		// String url = "rtsp://192.168.1.7:8554/1";
		// videoView.setVideoURI(Uri.parse(url));

		// 开始播放视频
		videoView.requestFocus();
		// 设置背景透明,以显示视频内容
		videoView.setBackgroundColor(0);
		videoView.start();

	}
}