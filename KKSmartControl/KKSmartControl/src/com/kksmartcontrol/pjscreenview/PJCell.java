package com.kksmartcontrol.pjscreenview;

import android.graphics.Color;

/**
 * Created by BBBoy on 2014/12/19.
 */
public class PJCell {
	// 用于设置边缘宽度
	// public static int lineDegree;
	//
	// public static void setLineDegree(int lineDegree) {
	// PJCell.lineDegree = lineDegree;
	// }

	public static int selectLineColor = Color.GREEN;
	public static int selectCellColor = Color.CYAN;
	private boolean selectState = false;
	public int rowNum, columnNum;
	public String inputSignl = "VGA";
	public int lineColor = Color.BLUE;
	public int cellColor = Color.GREEN;

	public boolean isSelected() {
		return selectState;
	}

	public void setSelectState(boolean selectState) {
		this.selectState = selectState;
	}

	int backLight = 100;
	int[] rgbPlus = { 128, 128, 128 };
	int[] picMode = { 40, 40, 50, 12 };

	public void setInputSignl(String inputSignl, int lineColor, int cellColor) {
		this.inputSignl = inputSignl;
		this.lineColor = lineColor;
		this.cellColor = cellColor;
	}

	public String getInputSignl() {
		return inputSignl;
	}

	public PJCell(int columnNum, int rowNum) {
		this.columnNum = columnNum;
		this.rowNum = rowNum;
	}

	@Override
	public String toString() {
		return "PJCell{" + "rowNum=" + rowNum + ", columnNum=" + columnNum
				+ '}';
	}

	//
	// public int getLineColor() {
	// return lineColor;
	// }
	//
	// public void setLineColor(int lineColor) {
	// this.lineColor = lineColor;
	// }
	//
	// public int getCellColor() {
	// return cellColor;
	// }
	//
	// public void setCellColor(int cellColor) {
	// this.cellColor = cellColor;
	// }
	/*
	 * //PJCell自己提供的绘制方法，暂时不使用 public static void draw(PJCell cell, int
	 * cellWidth, int cellHeight, Canvas cacheCanvas) {
	 * 
	 * float xLocation = (cell.columnNum - 1) * cellWidth; float yLocation =
	 * (cell.rowNum - 1) * cellHeight; paint.setColor(cell.cellColor);
	 * cacheCanvas.drawRect(xLocation, yLocation, xLocation + cellWidth,
	 * yLocation + cellHeight, paint); paint.setColor(cell.lineColor);
	 * cacheCanvas.drawLine(xLocation, yLocation, xLocation + cellWidth,
	 * yLocation, paint); cacheCanvas.drawLine(xLocation + cellWidth, yLocation,
	 * xLocation + cellWidth, yLocation + cellHeight, paint);
	 * cacheCanvas.drawLine(xLocation, yLocation + cellHeight, xLocation +
	 * cellWidth, yLocation + cellHeight, paint);
	 * cacheCanvas.drawLine(xLocation, yLocation, xLocation, yLocation +
	 * cellHeight, paint); }
	 */
}
