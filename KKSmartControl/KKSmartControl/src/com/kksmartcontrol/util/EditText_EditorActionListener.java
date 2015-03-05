package com.kksmartcontrol.util;

import com.kksmartcontrol.activity.MainActivity;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.preference.PreferencesUtils;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * @author BigBigBoy Edit_Text输入完成后判断字符是否合法，并作出相应处理
 */
public class EditText_EditorActionListener implements OnEditorActionListener {

	// private Context context = null;
	//
	// public EditText_EditorActionListener(Context context) {
	// // MyEditText = editText;
	// this.context = context;
	// }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// KEYCODE_BACKy与KEYCODE_ENTER分别是判断点击返回键及确定键
		EditText myEditText = (EditText) v;
		Context context = v.getContext();
		// 判断输入框有无字符，无字符将其设为默认值2，存在字符再判断字符合法性，
		// 若不合法则判断末尾字符是否合法，合法则将其值设为末尾字符，否则设为默认值2
		if ((0 != myEditText.getText().length())) {
			int num = Integer.parseInt(myEditText.getText().toString());
			if (num > 10) {
				num = num % 10;
				if (num == 0) {
					num = 10;
				}
				myEditText.setText(String.valueOf(num)); 
				ToastUtil.showToast(context, "行数与列数最大为10,\n\n已设置为尾数   " + num
						+ "   ！", Toast.LENGTH_SHORT);
			} else if (num < 1) {
				myEditText.setText(String.valueOf(2));
				ToastUtil.showToast(context, "行数与列数不可小于1,\n\n已设置为默认值   " + 2
						+ "   ！", Toast.LENGTH_SHORT);
			}
		} else {
			myEditText.setText("2");
			ToastUtil.showToast(context, "行数与列数不可为空,\n\n已设置为默认值   2   ！",
					Toast.LENGTH_SHORT);
		}
		myEditText.setSelection(myEditText.getText().length());
		int num = Integer.parseInt(myEditText.getText().toString());
		// 每次设置好后，自动更新拼接方式
		if (v.getTag().equals("row")) {
			KKSmartControlDataBean.setRowNum(num);
			PreferencesUtils.putInt(context, "rowNum", num);
		} else {
			KKSmartControlDataBean.setColumnNum(num);
			PreferencesUtils.putInt(context, "columnNum", num);
		}
		((PjScreenViewInterface) (MainActivity) context).setPJSplicesMode(
				KKSmartControlDataBean.getRowNum(),
				KKSmartControlDataBean.getColumnNum());

		return false;
	}

}