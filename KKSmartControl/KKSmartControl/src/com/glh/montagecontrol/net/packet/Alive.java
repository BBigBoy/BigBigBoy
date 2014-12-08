package com.glh.montagecontrol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Alive implements IPacket 
{
	public int getCmd() { return CMD_Alive; }

	public void read(DataInputStream is, int len) {}

	public void write(DataOutputStream os) throws IOException
	{
		os.writeShort(getBodyLen());
		os.writeShort(getCmd());
	}

	public int getBodyLen() { return 0; }
}
