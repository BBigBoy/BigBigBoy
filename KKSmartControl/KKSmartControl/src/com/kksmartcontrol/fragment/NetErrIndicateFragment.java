package com.kksmartcontrol.fragment;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.net.NetWorkObject;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NetErrIndicateFragment extends Fragment implements OnClickListener {
	Button reconBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layoutView = inflater.inflate(R.layout.neterrindicate, container,
				false);
		reconBtn = (Button) layoutView.findViewById(R.id.reconnect);
		reconBtn.setOnClickListener(this);
		return layoutView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		NetWorkObject.getInstance().connectToServer();
	}
}
