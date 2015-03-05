package com.kksmartcontrol.netcmd;

import java.util.List;

import com.glh.montagecontrol.net.client.NetClient;
import com.glh.montagecontrol.net.packet.CommandPacket;
import com.kksmartcontrol.bean.Coordinate;
import com.kksmartcontrol.bean.KKSmartControlDataBean;
import com.kksmartcontrol.net.NetWorkObject;
import com.kksmartcontrol.net.ParameDataHandle;
import com.kksmartcontrol.net.ParameDataHandle.SystemFuntion;

public class SetPJ_Infor {

	ParameDataHandle parameData = new ParameDataHandle();
	NetClient netclient = NetWorkObject.getInstance().getNetClient();

	private static class SetPJ_InforHolder {
		public final static SetPJ_Infor instance = new SetPJ_Infor();
	}

	// 设计单例模式
	public static SetPJ_Infor getInstance() {
		return SetPJ_InforHolder.instance;
	}

	public void setPjSource(List<Coordinate> coordinateList,
			SystemFuntion function, byte cmd) {
		CommandPacket cmdPacket = new CommandPacket();
		byte id = 0;
		byte dataHigh = 0;
		byte dataLow = 0;
		Coordinate[] PolarCoordinates = parameData
				.getPolarCoordinates(coordinateList);
		dataHigh = parameData.getPacketDataHigh(PolarCoordinates);

		for (Coordinate item : coordinateList) {
			id = parameData.getPacketID(item,
					KKSmartControlDataBean.getColumnNum(),
					KKSmartControlDataBean.getRowNum());
			dataLow = parameData.getPacketDataLow(item, PolarCoordinates);
			byte packet[] = parameData.setpacket(id, (byte) 0x31, function,
					dataHigh, dataLow);
			cmdPacket.buf = packet;
			sendCmdPacket(cmdPacket);
		}
	}

	public void setPjFunctionPacket(List<Coordinate> coordinateList,
			SystemFuntion function, byte cmd, byte dataHigh, byte dataLow) {

		CommandPacket cmdPacket = new CommandPacket();
		for (Coordinate item : coordinateList) {
			byte id = parameData.getPacketID(item,
					KKSmartControlDataBean.getColumnNum(),
					KKSmartControlDataBean.getRowNum());
			byte packet[] = parameData.setpacket(id, (byte) 0x31, function,
					dataHigh, dataLow);
			cmdPacket.buf = packet;
			sendCmdPacket(cmdPacket);
		}

	}

	public void adjustColorMode(List<Coordinate> coordinateList,
			SystemFuntion function, byte cmd, byte dataHigh, byte dataLow,
			byte gainR, byte gainG, byte gainB, byte offsetR, byte offsetG,
			byte offsetB) {
		CommandPacket cmdPacket = new CommandPacket();
		for (Coordinate item : coordinateList) {
			byte id = parameData.getPacketID(item,
					KKSmartControlDataBean.getColumnNum(),
					KKSmartControlDataBean.getRowNum());
			byte packet[] = parameData.setAdjustColorRGBPacket(id, (byte) 0x31,
					function, dataHigh, dataLow, gainR, gainG, gainB, offsetR,
					offsetG, offsetB);
			cmdPacket.buf = packet;
			sendCmdPacket(cmdPacket);
		}

	}

	public void setPjFunctionSleep(byte specialId, SystemFuntion function,
			byte cmd, byte dataHigh, byte dataLow) {
		CommandPacket cmdPacket = new CommandPacket();
		byte packet[] = parameData.setpacket(specialId, (byte) 0x31, function,
				dataHigh, dataLow);
		cmdPacket.buf = packet;
		sendCmdPacket(cmdPacket);
	}

	/**
	 * 通过网络客户端发送命令包
	 * 
	 * @param cmdPacket
	 *            命令包
	 */
	void sendCmdPacket(CommandPacket cmdPacket) {
		netclient.send(cmdPacket);
	}

}