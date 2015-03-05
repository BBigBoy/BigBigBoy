package com.kksmartcontrol.util;

import java.util.List;

import com.kksmartcontrol.bean.Coordinate;

public interface PjScreenViewInterface {
	/**
	 * @return 返回选择的屏幕集合坐标
	 */
	public List<Coordinate> getCoordinateList();

	/**
	 * @return 判断是否存在选中的屏幕
	 */
	public boolean isSelectListEmpty();

	/**
	 * 根据给定的行列值 刷新拼接界面
	 * 
	 * @param ROW
	 *            拼接的行数
	 * @param COLUMN
	 *            拼接的列数
	 */
	public void setPJSplicesMode(int ROW, int COLUMN);

	/**
	 * 设置选中屏的输入信号
	 * 
	 * @param inputSignl
	 *            输入信号名称
	 */
	public void setInputSignl(String inputSignl);
}
