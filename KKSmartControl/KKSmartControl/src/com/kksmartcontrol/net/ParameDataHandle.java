package com.kksmartcontrol.net;

import java.util.List;

import com.kksmartcontrol.bean.Coordinate;

import android.util.Log;

public class ParameDataHandle {

	byte PKG_SET = 0x31;
	byte PKG_GET = 0x35;
	byte PKG_GET_ALL = 0x36;
	byte PKG_SET_ID = 0x38;

	byte packet_id = 0x00;
	byte packet_cmd = 0x00;
	byte packet_function = 0x00;
	byte packet_dataHigh = 0x00;
	byte packet_dateLow = 0x00;
	byte packet_checksum = 0x00;

	String TAG = "ParameDataHandle";

	public enum SystemFuntion {
		SYSTEM_FUNCTION_NULL, SET_PJ_DVI, SET_PJ_HDMI, SET_PJ_YPBPR, SET_PJ_VGA, SET_PJ_CVBS,

		SET_PICMODE_STANDARD, SET_PICMODE_DYNAMIC, SET_PICMODE_MANUAL,

		SET_PICMODE_BRIGHTNESS, SET_PICMODE_CONTRAST, SET_PICMODE_TON, SET_PICMODE_SHAPNESS,

		SET_COLORMODE_6500K, SET_COLORMODE_9300K, SET_COLORMODE_MANUAL,

		SET_COLORMODE_R, SET_COLORMODE_G, SET_COLORMODE_B,

		SET_ADJUST_BACKLIGHT, SET_SYSTEM_SLEEP, SET_SYSTEM_WAKE, SET_LOCAL_ID,
	}

	public byte[] setAdjustColorRGBPacket(byte id, byte cmd,
			SystemFuntion function, byte dataHigh, byte dataLow, byte gainR,
			byte gainG, byte gainB, byte offsetR, byte offsetG, byte offsetB) {

		packet_id = id;
		packet_cmd = cmd;
		packet_dataHigh = dataHigh;
		packet_dateLow = dataLow;
		switch (function) {
		case SET_COLORMODE_6500K:
			break;
		case SET_COLORMODE_9300K:
			packet_function = 0x75;
			break;
		case SET_COLORMODE_R:
		case SET_COLORMODE_G:
		case SET_COLORMODE_B:
			packet_function = 0x6B;
			break;
		default:
			break;
		}
		packet_checksum = (byte) (packet_id + packet_cmd + packet_function
				+ packet_dataHigh + packet_dateLow);
		if (function == SystemFuntion.SET_COLORMODE_6500K) {
			packet_function = 0x74;
			byte packet[] = { (byte) packet_id, (byte) packet_cmd,
					(byte) packet_function, (byte) packet_dataHigh,
					(byte) packet_dateLow, (byte) packet_checksum, (byte) 0xFF,
					(byte) 0xFF, (byte) 0xFF };
			return packet;
		} else if (function == SystemFuntion.SET_COLORMODE_9300K) {
			packet_function = 0x75;
			byte packet[] = { (byte) packet_id, (byte) packet_cmd,
					(byte) packet_function, (byte) packet_dataHigh,
					(byte) packet_dateLow, (byte) packet_checksum, (byte) 0xF0,
					(byte) 0xFF, (byte) 0xE6 };
			return packet;
		} else if ((function == SystemFuntion.SET_COLORMODE_R)
				|| (function == SystemFuntion.SET_COLORMODE_G)
				|| (function == SystemFuntion.SET_COLORMODE_B)) {
			packet_function = 0x6B;
			byte packet[] = { (byte) packet_id, (byte) packet_cmd,
					(byte) packet_function, (byte) packet_dataHigh,
					(byte) packet_dateLow, (byte) packet_checksum, gainR,
					offsetR, gainG, offsetG, gainB, offsetB };
			return packet;
		} else {
			return null;
		}
	}

	public byte[] setpacket(byte id, byte cmd, SystemFuntion function,
			byte dataHigh, byte dataLow) {
		packet_id = id;
		packet_cmd = cmd;
		packet_dataHigh = dataHigh;
		packet_dateLow = dataLow;
		switch (function) {
		case SET_PJ_DVI:
			packet_function = 0x59;
			break;
		case SET_PJ_HDMI:
			packet_function = 0x58;
			break;
		case SET_PJ_YPBPR:
			packet_function = 0x56;
			break;
		case SET_PJ_VGA:
			packet_function = 0x57;
			break;
		case SET_PJ_CVBS:
			packet_function = 0x55;
			break;

		case SET_PICMODE_STANDARD:
		case SET_PICMODE_DYNAMIC:
			packet_function = 0x4D;
			break;
		case SET_PICMODE_CONTRAST:
			packet_function = 0x06;
			break;
		case SET_PICMODE_BRIGHTNESS:
			packet_function = 0x05;
			break;
		case SET_PICMODE_TON:
			packet_function = 0x07;
			break;
		case SET_PICMODE_SHAPNESS:
			packet_function = 0x08;
			break;

		case SET_ADJUST_BACKLIGHT:
			packet_function = 0x4F;
			break;
		case SET_SYSTEM_SLEEP:
		case SET_SYSTEM_WAKE:
			packet_function = 0x67;
			break;
		case SET_LOCAL_ID:
			packet_function = 0x38;
			break;
		default:
			break;
		}
		// packet_function = function;
		packet_checksum = (byte) (packet_id + packet_cmd + packet_function
				+ packet_dataHigh + packet_dateLow);
		byte packet[] = { (byte) packet_id, (byte) packet_cmd,
				(byte) packet_function, (byte) packet_dataHigh,
				(byte) packet_dateLow, (byte) packet_checksum };
		for (int i = 0; i < packet.length; i++) {
			Log.d("setpacket", "i==========" + i + "--------"
					+ "packet[i] =======" + (byte) packet[i]);
		}
		return packet;
	}

	/**
	 * @param coordinate
	 *            拼接屏坐标对象
	 * @param columnNum
	 *            拼接墙列数
	 * @param rowNum
	 *            拼接墙行数
	 * @return 返回拼接屏逻辑ID号
	 */
	public byte getPacketID(Coordinate coordinate, int columnNum, int rowNum) {
		int id = 0;
		if ((rowNum - coordinate.rows) % 2 != 0) {
			id = ((rowNum - coordinate.rows) * columnNum + coordinate.columns);

		} else {
			id = ((rowNum - coordinate.rows) * columnNum + (columnNum
					- coordinate.columns + 1));
		}
		return (byte) id;
	}

	/**
	 * @param itemCoordinate
	 *            选中的某一个拼接屏
	 * @param polarCoordinate选中屏幕的极限坐标
	 *            ，即最大及最小行列值的坐标对象
	 * @return DataLow
	 */
	public byte getPacketDataLow(Coordinate itemCoordinate,
			Coordinate[] polarCoordinate) {

		int dataLow = (itemCoordinate.columns - polarCoordinate[0].columns) + 1
				+ (itemCoordinate.rows - polarCoordinate[0].rows)
				* (polarCoordinate[1].columns - polarCoordinate[0].columns + 1);

		return (byte) dataLow;
	}

	/**
	 * @param polarCoordinate
	 *            选中屏幕的极限坐标，即最大及最小行列值的坐标对象
	 * @return
	 */
	public byte getPacketDataHigh(Coordinate[] polarCoordinate) {
		int dataHigh = (polarCoordinate[1].columns - polarCoordinate[0].columns + 1)
				* 16 + polarCoordinate[1].rows - polarCoordinate[0].rows + 1;

		return (byte) dataHigh;
	}

	/**
	 * @param coordinateList
	 *            选中拼接屏的坐标集合
	 * @return 返回最大及最小行列值的坐标对象
	 * @throws CloneNotSupportedException
	 */
	public Coordinate[] getPolarCoordinates(List<Coordinate> coordinateList) {
		Log.d(TAG, coordinateList.toString());
		// polarCoordinates极坐标
		// polarCoordinates[0]行列值最小的拼接屏
		// polarCoordinates[1]行列值最大的拼接屏
		Coordinate[] polarCoordinates = new Coordinate[2];
		// 赋初值
		try {
			polarCoordinates[0] = coordinateList.get(0).clone();
			polarCoordinates[1] = coordinateList.get(0).clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Coordinate item : coordinateList) {
			if (item.columns > polarCoordinates[1].columns)
				polarCoordinates[1].columns = item.columns;
			else if (item.columns < polarCoordinates[0].columns)
				polarCoordinates[0].columns = item.columns;

			if (item.rows > polarCoordinates[1].rows)
				polarCoordinates[1].rows = item.rows;
			else if (item.rows < polarCoordinates[0].rows)
				polarCoordinates[0].rows = item.rows;
		}
		return polarCoordinates;
	}

}