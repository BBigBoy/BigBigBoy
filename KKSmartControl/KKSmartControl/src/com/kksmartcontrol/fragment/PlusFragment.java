package com.kksmartcontrol.fragment;

import com.example.kksmartcontrol.R;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.util.EditText_EditorActionListener;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class PlusFragment extends Fragment implements OnClickListener {
	View rowset, columnset;
	EditText edit_row, edit_column;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layoutView = inflater.inflate(R.layout.plussetting, container,
				false);
		layoutView.setPivotY(0);
		rowset = (View) layoutView.findViewById(R.id.rowset);
		columnset = (View) layoutView.findViewById(R.id.columnset);
		rowset.setOnClickListener(this);
		columnset.setOnClickListener(this);
		edit_row = (EditText) layoutView.findViewById(R.id.edit_row);
		edit_column = (EditText) layoutView.findViewById(R.id.edit_column2);
		// Edit_Text输入完成后判断字符是否合法，并作出相应处理
		edit_row.setOnEditorActionListener(new EditText_EditorActionListener());
		edit_column
				.setOnEditorActionListener(new EditText_EditorActionListener());

		edit_row.setText(String.valueOf(KKSmartControlDataBean.getRowNum()));
		edit_column.setText(String.valueOf(KKSmartControlDataBean
				.getColumnNum()));
		edit_row.setSelection(edit_row.getText().length());
		edit_column.setSelection(edit_column.getText().length());
		return layoutView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showSoftInput();
		switch (v.getId()) {
		case R.id.rowset:
			edit_row.requestFocus();
			break;
		case R.id.columnset:
			edit_column.requestFocus();
			break;
		}
	}

	/**
	 * 打开软键盘
	 */
	private void showSoftInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					0);
		}
	}

	// private void hideSoftInput(View view) {
	// InputMethodManager inputMethodManager = (InputMethodManager)
	// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	// inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	// }
}
