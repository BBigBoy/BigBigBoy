package com.kksmartcontrol.bean;

public class KKSmartControlDataBean {
	// private static String TAG = "KKSmartControlDataBean";
	// private static List<Coordinate> coordinateList = new
	// ArrayList<Coordinate>();
	/**
	 * 拼接墙行数
	 */
	private static int rowNum = 2;
	/**
	 * 拼接墙列数
	 */
	private static int columnNum = 2;

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

	// /**
	// * @return 返回选中屏的坐标集合
	// */
	// public static List<Coordinate> getCoordinateList() {
	// List<Coordinate> coordinateList = new ArrayList<Coordinate>();
	// List<PJCell> selectList = PJDiaplayFragment.pjDiaplayFragment
	// .getSelectList();
	// Log.d("getCoordinateListlllll", "\n\nselectList.size()---->"
	// + selectList.size());
	// for (PJCell cell : selectList) {
	// coordinateList.add(new Coordinate(cell.rowNum, cell.columnNum));
	// Log.d("getCoordinateList", "\n\n---->" + cell);
	// }
	// Log.d("getCoordinateListlllll", "\n\ncoordinateList.size()---->"
	// + coordinateList.size());
	// return coordinateList;
	// }
	//
	// public static boolean isSelectListEmpty() {
	// return PJDiaplayFragment.pjDiaplayFragment.getSelectList().isEmpty();
	// }

}
