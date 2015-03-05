package com.kksmartcontrol.bean;

public class Coordinate implements Cloneable {
	public int columns;
	public int rows;

	public Coordinate(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public Coordinate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		Coordinate coordinate = (Coordinate) object;
		if ((columns == coordinate.columns) && (rows == coordinate.rows))
			return true;
		else
			return false;
	}

	@Override
	public Coordinate clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		// 只有基础类型，浅复制即可
		return (Coordinate) super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Coordinate.columns--->" + this.columns
				+ "------Coordinate.rows--->" + this.rows;
	}
}