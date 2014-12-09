package com.kksmartcontrol.fragment;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.widget.VideoView;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.playvideoview.PlayVideoViewGestureListener;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class VideoPlayFragment extends Fragment {
	VideoView playVideoView;
	private View mBufferingIndicator;

	GestureDetector gestureDetector;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.videoview_play, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		VideoView_Init(getView());
		videoViewToPlay(playVideoView);
	}

	/**
	 * 初始化所有videoview控件，并注册相应时间
	 */
	private void VideoView_Init(View view) {

		mBufferingIndicator = view.findViewById(R.id.buffering_indicator);
		playVideoView = (VideoView) view.findViewById(R.id.videoView_real);
		gestureDetector = new GestureDetector(getActivity(),
				new PlayVideoViewGestureListener(playVideoView));
		// playVideoView.setMediaController(new MediaController(getActivity()));
		playVideoView.setMediaBufferingIndicator(mBufferingIndicator);
		// playVideoView.setOnDragListener(new PlayVideoView_DragListener());
		playVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(IMediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				// 这里写播放错误时的操作
				return false;
			}
		});
		playVideoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	/**
	 * 播放视频
	 * 
	 * @param preVideoView
	 *            播放视频的preVideo
	 */
	private void videoViewToPlay(VideoView videoView) {
		// 设置播放地址
		// videoView.setVideoURI(Uri
		// .parse("android.resource://com.example.kksmartcontrol/"
		// + R.drawable.big));
		videoView.setVideoURI(Uri.parse(Environment
				.getExternalStorageDirectory().getPath() + "/15.MP4"));

		// videoView.setVideoURI(Uri.parse("/sdcard/Video/5.MP4"));

		// String url = "rtsp://192.168.1.100:8554/test";
		// videoView.setVideoURI(Uri.parse(url));

		// 开始播放视频
		videoView.requestFocus();
		// 设置背景透明,以显示视频内容
		videoView.setBackgroundColor(0);
		videoView.start();

	}
}
