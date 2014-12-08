package com.kksmartcontrol.fragment;

import java.util.HashSet;
import java.util.Set;

import com.kksmartcontrol.prevideoview.MyPreVideoView;
import android.app.Fragment;
import android.os.Bundle; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class VideoPreFragment extends Fragment {

	public static VideoPreFragment videoPreFragment = null;
	// 正在播放的预览窗口的集合
	public Set<MyPreVideoView> videoList = new HashSet<MyPreVideoView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		videoPreFragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return createPreVideoView();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (!hidden) {
			for (MyPreVideoView video : videoList) {
				video.stopPlayback();
				video.setVisibility(View.VISIBLE);
			}
		}
		if (hidden) {
			for (MyPreVideoView video : videoList) {
				video.start();
				video.setVisibility(View.GONE);
			}
		}
		// Log.d("preVideoPlayingList.toString()", playingList.toString());
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		videoPreFragment = null;
		super.onDestroy();
	}

	private View createPreVideoView() {
		LinearLayout preVideo_layout = new LinearLayout(getActivity());
		preVideo_layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		for (int i = 1; i <= 2; i++) {
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 1; j <= 2; j++) {
				// 根据不同位置，控制不同margin
				MyPreVideoView iv = new MyPreVideoView(getActivity(), null);
				linearLayout.addView(iv, params);
				videoList.add(iv);
			}
			preVideo_layout.addView(linearLayout, params);
		}
		return preVideo_layout;
	}

}
