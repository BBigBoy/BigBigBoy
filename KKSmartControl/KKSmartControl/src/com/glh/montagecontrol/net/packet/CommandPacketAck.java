package com.glh.montagecontrol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 拼接命令包的应答.
 * <p>根据是否成功写入串口来应答，并不管后续是否执行成功
 */
public class CommandPacketAck implements IPacket 
{
	/** 拼接命令的结果.
	 * <p>0 写入成功; 1 写串口失败； 2 串口未打开
	 */
	public byte ret = 0;		
	
	public int getCmd() { return CMD_CommandPacketAck; }
	public int getBodyLen() { return 1; }

	public void read(DataInputStream is, int len) throws IOException {
		ret = is.readByte();
	}

	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ret);
	}
}
