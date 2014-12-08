package com.glh.montagecontrol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 拼接命令包.
 * <p>需要转发到串口的命令，都走此包
 */
public class CommandPacket implements IPacket 
{
	/**
	 * 拼接命令的字节数组
	 */
	public byte [] buf;
	
	public int getCmd() { return CMD_CommandPacket; }
	public int getBodyLen() { return buf.length; }

	public void read(DataInputStream is, int len) throws IOException 
	{
		buf = new byte[len];
		is.read(buf, 0, len);
	}

	public void write(DataOutputStream os) throws IOException 
	{
		if (buf == null)
			return;
		os.writeShort(getBodyLen());
		os.writeShort(getCmd());
		os.write(buf);
	}
}
