package com.kksmartcontrol.bean;

public class Coordinate implements Cloneable {
	public int X;
	public int Y;

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		Coordinate coordinate = (Coordinate) object;
		if ((X == coordinate.X) && (Y == coordinate.Y))
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
		return "Coordinate.X--->" + this.X + "------Coordinate.Y--->" + this.Y;
	}
}