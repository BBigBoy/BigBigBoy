package com.kksmartcontrol.bean;

import java.util.ArrayList;


public class KKSmartControlDataBean {
	static String TAG = "KKSmartControlDataBean";
	static ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
	/**
	 * 拼接墙行数
	 */
	static int rowNum = 2;
	/**
	 * 拼接墙列数
	 */
	static int columnNum = 2;

	public static int getRowNum() {
		return rowNum;
	}

	public static void setRowNum(int rowNum) {
		KKSmartControlDataBean.rowNum = rowNum;
	}

	public static int getColumnNum() {
		return columnNum;
	}

	public static void setColumnNum(int columnNum) {
		KKSmartControlDataBean.columnNum = columnNum;
	}

	/**
	 * @return 返回选中屏的坐标集合
	 */
	public static ArrayList<Coordinate> getCoordinateList() {
		return coordinateList;
	}

	/**
	 * 添加一个选中屏的坐标
	 * 
	 * @param coordinate
	 */
	public static void addToCoordinateList(Coordinate coordinate) {
		coordinateList.add(coordinate);
	}

	/**
	 * 移除一个选中屏的坐标
	 * 
	 * @param coordinate
	 */
	public static void removeFromCoordinateList(Coordinate coordinate) {
		coordinateList.remove(coordinate);
	}

	/**
	 * 清除选中的拼接屏
	 */
	public static void clearCoordinateList() {
		coordinateList.clear();
	}

	public static int getCoordinateListSize() {
		return coordinateList.size();

	}

}
