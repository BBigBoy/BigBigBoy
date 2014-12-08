package com.glh.montagecontrol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeviceSearch implements IPacket
{	
	public int getCmd() { return IPacket.CMD_DevSearch; }	
	public int getBodyLen() { return 0; }

 	public void read(DataInputStream is, int len) throws IOException {}

	public void write(DataOutputStream os) throws IOException 
	{
		os.writeShort(getBodyLen());
		os.writeShort(getCmd());	
	}
}
