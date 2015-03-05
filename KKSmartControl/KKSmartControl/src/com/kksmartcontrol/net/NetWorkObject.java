package com.kksmartcontrol.net;

import java.net.SocketAddress;
import com.glh.montagecontrol.net.client.INetDoer;
import com.glh.montagecontrol.net.client.NetClient;
import com.glh.montagecontrol.net.client.NetState;
import com.glh.montagecontrol.net.packet.CommandPacketAck;
import com.glh.montagecontrol.net.packet.IPacket;
import com.kksmartcontrol.activity.MainActivity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWorkObject implements INetDoer {
	String TAG = this.getClass().getName();
	/**
	 * // 网络状态表示，全局只应该有一个，其余通过引用传递
	 */
	NetState netState = NetState.NET_STATUS_ERR;
	/**
	 * // 网络连接客户端，全局只应该有一个，其余通过引用传递
	 */
	NetClient netclient = new NetClient();
	/**
	 * // 标识是否正在处理网络请求，true为正在处理，不接受其他网络请求
	 */
	Boolean workState = false;
	// 记录网络重连次数,重连3次连接不上就放弃重连
	int reConnectTime = 0;

	public static Application application;
	public static Context context;

	private static class NetWorkObjectHolder {
		public final static NetWorkObject instance = new NetWorkObject();
	}

	// 设计单例模式
	public static NetWorkObject getInstance() {
		return NetWorkObjectHolder.instance;
	}

	private NetWorkObject() {
		Log.d(TAG, "NetWorkObject");
		initNetClient();
	}

	/**
	 * 连接服务器
	 */
	public void connectToServer() {
		Log.i("second", "connectToServer");
		if (netState != NetState.TCP_CONN_OPEN) {
			Log.i("second",
					"connectToServer  netState != NetState.TCP_CONN_OPEN");
			if (!judgeNetConfig(application))// 网络状态错误即不进行后面判断
				return;

			if (workState == false) {
				Log.i("second", "workState == false connectDev()");
				connectDev();
			}
		}
	}

	/**
	 * 判断是否联网并提示使用WiFi连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean judgeNetConfig(Context context) {

		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			setNetStatus(NetState.NET_STATUS_ERR);
			return false;
		} else {
			boolean isWifiOk = con
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.isConnectedOrConnecting();
			if (!isWifiOk) { // 提示使用wifi
				setNetStatus(NetState.NET_STATUS_ERR);
				return false;
			}
		}
		return true;
	}

	/**
	 * 网络初始化，不包括连接
	 */
	public void initNetClient() {
		netclient.init();
		netclient.pushDoer(this);
		Log.i("second", "initNetClient");
	}

	/**
	 * 网络退出
	 */
	public void unInitNetClient() {
		netclient.popDoer();
		netclient.uninit();
		Log.i("second", "unInitNetClient");
	}

	/**
	 * 网络断开后重连时调用
	 */
	public void connectDev() {

		netclient.connectDev();
		workState = true;
	}

	/**
	 * @param netstate
	 *            设置当前与服务器网络连接状态
	 */
	public void setNetStatus(NetState netstate) {
		netState = netstate;
	}

	/**
	 * @return 返回当前与服务器连接状态
	 */
	public NetState getNetStatus() {

		return netState;
	}

	/**
	 * @return 返回当前与服务器连接客户端对象
	 */
	public NetClient getNetClient() {

		return netclient;
	}

	/**
	 * 收到网络数据包的事件.
	 * <p>
	 * 在事件函数实现中，不能直接更新UI，需要Handler机制更新。
	 * 
	 * @param addr
	 *            数据包发送方的地址。tcp连接，总是null
	 * @param pkt
	 *            数据包
	 * @return 处理了数据包，返回true；否则，false
	 */
	@Override
	public boolean onReceive(SocketAddress sa, IPacket arg1) {

		// TODO Auto-generated method stub
		if (arg1.getCmd() == IPacket.CMD_CommandPacketAck) {
			Log.i(TAG, "((CommandPacketAck)arg1).ret=============="
					+ ((CommandPacketAck) arg1).ret);

			switch (((CommandPacketAck) arg1).ret) {
			case 0:// 写入成功

				break;
			case 1:// 写入失败
					// aboutDialog(R.string.warning, R.string.write_fail);
				break;
			case 2:// 串口未打开
					// aboutDialog(R.string.warning, R.string.port_close);
				break;
			}
		}
		return false;
	}

	/**
	 * 网络状态改变的事件.
	 * <p>
	 * 在事件函数实现中，不能直接更新UI，需要Handler机制更新。
	 * 
	 * @param addr
	 *            对端的地址
	 *            <p>
	 *            只有tcp连接，才会传出对端的地址；其它为null
	 * @param ev
	 *            网络事件
	 * @return 处理了事件，返回true；否则，false
	 */
	@Override
	public boolean onNetEvent(NetState ev) {
		// TODO Auto-generated method stub
		Log.i(TAG, "TestActivity.onNetEvent(, " + ev + ")");
		workState = false;
		switch (ev) {
		case TCP_CONN_OPEN:
			Log.i("second", " TCP_CONN_OPEN");
			setNetStatus(NetState.TCP_CONN_OPEN);
			reConnectTime = 0;
			break;
		case TCP_CONN_ERR:
			setNetStatus(NetState.TCP_CONN_ERR);
			if ((reConnectTime++ < 2)) {
				connectDev();
				Log.i(TAG, TAG + "reConnectTime---" + reConnectTime);
			} else {
				reConnectTime = 0;
			}
			break;
		case TCP_CONN_CLOSE:
			setNetStatus(NetState.TCP_CONN_CLOSE);
			break;
		case TCP_ERR:
			setNetStatus(NetState.TCP_ERR);
			break;
		default:
			return false;
		}
		if ((context != null) && (context instanceof MainActivity)) {
			if (ev.equals(NetState.TCP_CONN_OPEN))
				((MainActivity) context).hideNetErrIndicate();
			else
				((MainActivity) context).showNetErrIndicate();
		}
		return false;
	}
}