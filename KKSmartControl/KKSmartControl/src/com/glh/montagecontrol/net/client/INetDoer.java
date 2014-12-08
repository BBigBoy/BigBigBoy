package com.glh.montagecontrol.net.client;

import java.net.SocketAddress;

import com.glh.montagecontrol.net.packet.IPacket;

// 。调用者必须仔细阅读。
/**
 * 处理者接口.
 * <p>如果Activity或其它类要处理网络数据或网络状态信息，就要派生INetDoer接口：
 */
public interface INetDoer 
{
	/**
	 * 收到网络数据包的事件.
	 * <p>在事件函数实现中，不能直接更新UI，需要Handler机制更新。
	 * @param addr 数据包发送方的地址。tcp连接，总是null
	 * @param pkt 数据包
	 * @return 处理了数据包，返回true；否则，false
	 */
    boolean onReceive(SocketAddress sa, IPacket pkt);
    
	/**
	 * 网络状态改变的事件.
	 * <p>在事件函数实现中，不能直接更新UI，需要Handler机制更新。
	 * @param addr 对端的地址
	 * <p>只有tcp连接，才会传出对端的地址；其它为null
	 * @param ev 网络事件
	 * @return 处理了事件，返回true；否则，false
	 */	
    boolean onNetEvent(NetState ev);  	
}
