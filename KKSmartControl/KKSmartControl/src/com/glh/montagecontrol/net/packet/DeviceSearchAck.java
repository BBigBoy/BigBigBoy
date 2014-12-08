package com.glh.montagecontrol.net.packet;

public class DeviceSearchAck extends DeviceSearch 
{
	public int getCmd() { return IPacket.CMD_DevSearchAck; }	
}
