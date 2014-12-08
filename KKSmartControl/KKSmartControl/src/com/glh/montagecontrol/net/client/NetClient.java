package com.glh.montagecontrol.net.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import android.util.Log;
import com.glh.montagecontrol.net.packet.Alive;
import com.glh.montagecontrol.net.packet.CommandPacket;
import com.glh.montagecontrol.net.packet.CommandPacketAck;
import com.glh.montagecontrol.net.packet.DeviceSearch;
import com.glh.montagecontrol.net.packet.DeviceSearchAck;
import com.glh.montagecontrol.net.packet.IPacket;

/**
 * 网络客户端.
 * 
 * @author 郭灵辉
 */
public final class NetClient implements INetDoer {
	// ////////////////
	// 接口部分，必须仔细阅读
	// ////////////////

	/**
	 * 注册处理者.
	 * <p>
	 * 和popDoer()成对调用；</br>在init()之后调用；</br>必须在INetDoer对象运行之前调用
	 * 
	 * @param doer
	 *            处理者
	 */
	public void pushDoer(INetDoer doer) {
		mDoerList.add(doer);
	}

	/**
	 * 退出最近一次加入的网络处理者
	 */
	public void popDoer() {
		int size = mDoerList.size();
		if (size > 0)
			mDoerList.remove(size - 1);
	}

	/**
	 * 初始化网络框架.
	 * <p>
	 * 应该在程序最开始处或最后调用
	 */
	public void init() {
		pushDoer(this);
	}

	/**
	 * 释放网络框架.
	 * <p>
	 * 应该在程序结束处调用
	 */
	public void uninit() {
		if (mTcp != null) {
			mTcp.quit();
			mTcp = null;
		}

		if (mUdp != null) {
			mUdp.quit();
			mUdp = null;
		}
		popDoer();
		mDoerList.clear();
	}

	/**
	 * 请求连接服务端.
	 * <p>
	 * 自动连接局域网内，第一个搜索到的服务端
	 * <p>
	 * 连接成功、或失败，会有事件返回，请在 boolean onNetEvent(NetState ev) 中处理。
	 */
	public void connectDev() {
		mUdp = new UdpSession(this);
		mUdp.start();
	}

	/**
	 * 发送一个数据包.
	 * <p>
	 * 必须在连接成功之后，即网络状态必须是 TCP_CONN_OPEN ，才能发送。其它网络状态都表明网络框架不可用。
	 * 
	 * @param pkt
	 */
	public void send(IPacket pkt) {
		mTcp.send(pkt);
	}

	// ////////////////
	// 以下是内部接口，外部不需要关注
	// ////////////////
	void distributePacket(SocketAddress sa, IPacket pkt) {
		for (int i = mDoerList.size() - 1; i >= 0; i--) {
			if (mDoerList.get(i).onReceive(sa, pkt))
				break;
		}
	}

	void distributeNetEvent(NetState ev) {
		for (int i = mDoerList.size() - 1; i >= 0; i--) {
			if (mDoerList.get(i).onNetEvent(ev))
				break;
		}
	}

	void readToPacket(DataInputStream is, SocketAddress sa, int bodyLen)
			throws IOException {
		Log.i(TAG, "readToPacket(, , " + bodyLen + ")");

		int len = bodyLen;
		if (len < 0)
			len = is.readUnsignedShort();

		int cmd = is.readUnsignedShort();

		IPacket pkt = getPacket(cmd);
		if (pkt != null) {
			pkt.read(is, len);
			distributePacket(sa, pkt);
		} else {
			// throw new IOException("no packet cmd=" + cmd);
		}
	}

	static IPacket getPacket(int cmd) {
		switch (cmd) {
		case IPacket.CMD_Alive:
			return new Alive();
		case IPacket.CMD_DevSearchAck:
			return new DeviceSearchAck();
		case IPacket.CMD_CommandPacket:
			return new CommandPacket();
		case IPacket.CMD_CommandPacketAck:
			return new CommandPacketAck();
		}

		Log.e(TAG, "NetClient.getPacket() no packet cmd=" + cmd);
		return null;
	}

	public boolean onReceive(SocketAddress sa, IPacket pkt) {
		Log.i(TAG, "onReceive(, " + pkt.getClass() + ")");

		if (pkt.getCmd() == IPacket.CMD_DevSearchAck) {
			if (sa == null) {
				distributeNetEvent(NetState.TCP_CONN_ERR);
			} else {
				mTcp = new TcpSession(this);
				mTcp.connect(((InetSocketAddress) sa).getHostName(), PORT_SVR);
			}

			if (mUdp != null) {
				mUdp.quit();
				mUdp = null;
			}
			return true;
		}
		return false;
	}

	public boolean onNetEvent(NetState ev) {
		Log.i(TAG, "onNetEvent(, " + ev + ")");

		if (ev == NetState.UDP_ERR) {
			if (mUdp != null) {
				mUdp.quit();
				mUdp = null;
				distributeNetEvent(NetState.TCP_CONN_ERR);
			}
		} else if (ev == NetState.UDP_OPEN) {
			if (mUdp != null) {
				mUdp.send(new InetSocketAddress("255.255.255.255", PORT_SVR),
						new DeviceSearch());
			}
		} else
			return false;

		return true;
	}

	private ArrayList<INetDoer> mDoerList = new ArrayList<INetDoer>();
	private TcpSession mTcp;
	private UdpSession mUdp;

	static final String TAG = "netclient";
	static final short PORT_SVR = 9001;
}