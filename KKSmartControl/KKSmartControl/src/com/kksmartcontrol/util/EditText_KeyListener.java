package com.kksmartcontrol.util;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author BigBigBoy Edit_Text输入完成后判断字符是否合法，并作出相应处理
 */
public class EditText_KeyListener implements OnKeyListener {

	private EditText MyEditText = null;
	private Context context = null;

	public EditText_KeyListener(Context context) {
		// MyEditText = editText;
		this.context = context;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// KEYCODE_BACKy与KEYCODE_ENTER分别是判断点击返回键及确定键
		MyEditText = (EditText) v;
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
			// 判断输入框有无字符，无字符将其设为默认值2，存在字符再判断字符合法性，
			// 若不合法则判断末尾字符是否合法，合法则将其值设为末尾字符，否则设为默认值2
			if ((0 != MyEditText.getText().length())) {
				int num = Integer.parseInt(MyEditText.getText().toString());
				if (num > 10) {
					num = num % 10;
					MyEditText.setText(String.valueOf(num));
					Toast.makeText(context,
							"行数与列数最大为10,\n\n已设置为尾数   " + num + "   ！",
							Toast.LENGTH_SHORT).show();
				} else if (num < 1) {
					MyEditText.setText(String.valueOf(2));
					Toast.makeText(context,
							"行数与列数不可小于1,\n\n已设置为默认值   " + num + "   ！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				MyEditText.setText("2");
				Toast.makeText(context, "行数与列数不可为空,\n\n已设置为默认值   2   ！",
						Toast.LENGTH_SHORT).show();
			}
			MyEditText.setSelection(MyEditText.getText().length());
		}

		return false;
	}
}