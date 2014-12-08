package com.kksmartcontrol.prevideoview;

import com.example.kksmartcontrol.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.widget.VideoView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author BigBigBoy 自定义预览videoview
 */
public class MyPreVideoView extends VideoView {
	GestureDetector gestureDetector;

	public MyPreVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// // TODO Auto-generated constructor stub
		// setTag("preVideoView");
		// setZOrderOnTop(true);
		setBackgroundResource(R.drawable.thumb_videoview);
		gestureDetector = new GestureDetector(context,
				new PreVideoViewGestureListener(this));
		setOnDragListener(new VideoView_DragListener());

		setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(IMediaPlayer mp) {
				// TODO Auto-generated method stub
				MyPreVideoView.this
						.setBackgroundResource(R.drawable.thumb_videoview);
				MyPreVideoView.this.setContentDescription(null);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gestureDetector.onTouchEvent(e);

	}

}