package com.kksmartcontrol.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.kksmartcontrol.R;
import com.xlistview.adapter.PlayListAdapter;
import com.xlistview.handle.CharacterParser;
import com.xlistview.handle.PinyinComparator;
import com.xlistview.handle.SideBar;
import com.xlistview.handle.SideBar.OnTouchingLetterChangedListener;
import com.xlistview.model.PlayListItemModel;
import com.xlistview.xlistview.XListView;
import com.xlistview.xlistview.XListView.IXListViewListener;
import com.xml.sax.SaxService;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MediaPlayListFragment extends Fragment implements
		IXListViewListener, OnDragListener {

	private SideBar sideBar;
	private TextView dialog;
	private PlayListAdapter adapter;
	private List<PlayListItemModel> SourceDataList;
	private XListView playListView;
	private Handler mHandler;
	// 汉字转换成拼音的类
	private CharacterParser characterParser;
	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator;

	private Context context = null;

	private View selectListViewItem = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = getActivity();
		Log.d("MediaPlayListFragment", "onAttach");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.xlistview_videolist, container,
				false);

		Log.d("MediaPlayListFragment", "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ListView_Init(getView());
		Log.d("MediaPlayListFragment", "onActivityCreated");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("MediaPlayListFragment", "onDestroy");
	}

	/**
	 * 初始化ListView，包括sidebar
	 */
	private void ListView_Init(View view) {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					playListView.setSelection(position + 1);
				}
			}
		});

		playListView = (XListView) view.findViewById(R.id.list);
		playListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		playListView.setXListViewListener(this);
		SourceDataList = filledData(getListFromXML());
		// 根据a-z进行排序源数据
		Collections.sort(SourceDataList, pinyinComparator);
		adapter = new PlayListAdapter(context, SourceDataList);
		playListView.setAdapter(adapter);
		mHandler = new Handler();
		playListView.setOnDragListener(this);
		playListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						context,
						((PlayListItemModel) adapter.getItem(position - 1))
								.getName(), Toast.LENGTH_SHORT).show();
			}
		});
		playListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				TextView sourceName = (TextView) v
						.findViewById(R.id.source_name);
				View dragView = (LinearLayout) v.findViewById(R.id.itemlayout);
				dragView.setBackgroundResource(R.color.red);
				selectListViewItem = dragView;

				ClipData dragData = ClipData.newPlainText("sourceName",
						sourceName.getText());
				View.DragShadowBuilder myShadow = new DragShadowBuilder(
						dragView);
				String localState = "fromListViewItem";

				if (VideoPreFragment.videoPreFragment == null)
					VideoPreFragment.videoPreFragment = new VideoPreFragment();
				FragmentTransaction fragmentTransaction = getActivity()
						.getFragmentManager().beginTransaction();
				VideoPlayFragment videoPlayFragment = (VideoPlayFragment) getActivity()
						.getFragmentManager().findFragmentByTag(
								"VideoPlayFragment");
				if (VideoPreFragment.videoPreFragment.isAdded()) {
					if (VideoPreFragment.videoPreFragment.isHidden()) {
						if (videoPlayFragment != null
								&& videoPlayFragment.isAdded()) {
							getActivity().getFragmentManager().popBackStack();
						} else {
							fragmentTransaction
									.show(VideoPreFragment.videoPreFragment);
						}
					}
				} else {
					fragmentTransaction.add(R.id.videodisplay,
							VideoPreFragment.videoPreFragment);
				}

				fragmentTransaction.hide(PJDiaplayFragment.pjDiaplayFragment);
				fragmentTransaction.commit();
				// Starts the drag
				v.startDrag(dragData, myShadow, localState, 0);
				return false;
			}
		});
	}

	/** 停止刷新， */
	private void onLoad() {
		playListView.stopRefresh();
		playListView.stopLoadMore();
		playListView.setRefreshTime("刚刚");
	}

	// 下拉刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				SourceDataList = filledData(getListFromXML());
				// 根据a-z进行排序源数据
				Collections.sort(SourceDataList, pinyinComparator);
				adapter.updateListView(SourceDataList);
				playListView.setAdapter(adapter);
				onLoad();
			}
		}, 1000);
	}

	// 上拉刷新及加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// getData();
				Toast.makeText(context, "上拉刷新在这里更新数据", Toast.LENGTH_LONG)
						.show();
				onLoad();
			}
		}, 1000);
	}

	private List<HashMap<String, String>> getListFromXML() {
		// String path = Environment.getExternalStorageDirectory().getPath()
		// + "/videoList.xml";
		AssetManager assetManager = getResources().getAssets();
		try {
			InputStream inputStream = assetManager.open("videoList.xml");
			// InputStream inputStream = new FileInputStream(path);
			List<HashMap<String, String>> list = SaxService.readXML(
					inputStream, "video");
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 */
	private List<PlayListItemModel> filledData(
			List<HashMap<String, String>> videoList) {

		List<PlayListItemModel> mSortList = new ArrayList<PlayListItemModel>();

		for (HashMap<String, String> listItem : videoList) {
			PlayListItemModel sortModel = new PlayListItemModel();
			String path = listItem.get("path");
			String fileName = path.substring(path.lastIndexOf("\\") + 1);
			String time_length = listItem.get("time_length");
			Log.d(path, path);
			Log.d(time_length, time_length);
			Log.d(fileName, fileName);
			sortModel.setName(fileName.split("\\.")[0]);
			sortModel.setFormat(fileName.split("\\.")[1]);
			sortModel.setTime_Length(time_length);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(fileName);
			String sortString = pinyin.substring(0, 1).toUpperCase(
					Locale.ENGLISH);
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel
						.setSortLetters(sortString.toUpperCase(Locale.ENGLISH));
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {

		case DragEvent.ACTION_DRAG_ENDED:
			selectListViewItem.setBackgroundResource(R.color.white);
			Log.d("onDragonDrag",
					"DragEvent.ACTION_DRAG_ENDEDDragEvent.ACTION_DRAG_ENDED");
			break;

		default:
			break;
		}

		return false;
	}

}
