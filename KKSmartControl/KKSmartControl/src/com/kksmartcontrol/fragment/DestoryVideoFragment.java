package com.kksmartcontrol.fragment;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.prevideoview.MyPreVideoView;
 
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle; 
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DestoryVideoFragment extends Fragment { 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(getActivity());
		imageView.setBackgroundResource(R.drawable.thumb_destorypreview);

		imageView.setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {
				// TODO Auto-generated method stub
				Log.d("setOnDragListener", "setOnDragListener");
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					Log.d("ACTION_DRAG_STARTED", "ACTION_DRAG_STARTED");
					return true;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setSelected(true);
					Log.d("ACTION_DRAG_ENTERED", "ACTION_DRAG_ENTERED");
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setSelected(false);
					Log.d("ACTION_DRAG_EXITED", "ACTION_DRAG_EXITED");
					break;
				// case DragEvent.ACTION_DRAG_LOCATION:
				// Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
				// break;
				case DragEvent.ACTION_DRAG_ENDED:
					FragmentTransaction fragmentTransaction = getActivity()
							.getFragmentManager().beginTransaction();
					fragmentTransaction.hide(DestoryVideoFragment.this);
					fragmentTransaction.commit();
					Log.d("ACTION_DRAG_ENDED", "ACTION_DRAG_ENDED");
					break;
				case DragEvent.ACTION_DROP:
					MyPreVideoView videoView = (MyPreVideoView) event
							.getLocalState();
					videoView.stopPlayback();
					videoView.setContentDescription(null);
					// VideoPreFragment.videoPreFragment.playingList.remove(videoView);
					videoView.setBackgroundResource(R.drawable.thumb_videoview);
					Log.d("ACTION_DROP", "ACTION_DROP");
					break;
				default:
					break;
				}
				return false;

			}
		});
		return imageView;
	}

}
