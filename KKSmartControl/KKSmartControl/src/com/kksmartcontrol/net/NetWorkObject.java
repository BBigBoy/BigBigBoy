package com.kksmartcontrol.net;

import java.lang.ref.WeakReference;
import java.net.SocketAddress;

import com.example.kksmartcontrol.R;
import com.glh.montagecontrol.net.client.INetDoer;
import com.glh.montagecontrol.net.client.NetClient;
import com.glh.montagecontrol.net.client.NetState;
import com.glh.montagecontrol.net.packet.CommandPacketAck;
import com.glh.montagecontrol.net.packet.IPacket;
import com.kksmartcontrol.activity.MainActivity;
import com.kksmartcontrol.dialog.ConnectServerDialog;
import com.kksmartcontrol.dialog.NetErrDialog;
import com.kksmartcontrol.floatwindow.MyWindowManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

	/**
	 * 此上下文对象将在Activity的oncreate中执行，由于手势切换中先生成新的activity，后销毁，因此不会出现null的情况，
	 * 也不用去ondestory中置空
	 */
	public static Context context = null;

	private MyHandler mHandler = new MyHandler();

	private static class MyHandler extends Handler {
		// 记录网络重连次数,重连3次连接不上就放弃重连
		int reConnectTime = 0;
		ConnectServerDialog connectServerDialog = null;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity activity = new WeakReference<MainActivity>(
					(MainActivity) context).get();
			NetWorkObject netWorkObject = NetWorkObject.getInstance();
			// 接收到一次网络事件即认为上一次任务已完成
			netWorkObject.workState = false;

			if (activity != null) {
				switch (msg.what) {
				case 1://
					if (connectServerDialog == null) {
						connectServerDialog = new ConnectServerDialog();
					}
					connectServerDialog.show(activity.getFragmentManager(),
							"ConServer");
					break;
				case 2:// tcp connect
						// TopTitleFragment.setDeviceConnectState(true);
					MyWindowManager.setNetState(true);
					// 关闭正在连接网络对话框
					if (connectServerDialog != null) {
						reConnectTime = 0;
						connectServerDialog.dismiss();
						connectServerDialog = null;
					}

					// 关闭网络错误对话框
					NetErrDialog netErrDialog2 = (NetErrDialog) activity
							.getFragmentManager().findFragmentByTag("NetErr");
					if (netErrDialog2 != null)
						netErrDialog2.dismiss();

					Log.i(activity.TAG, "tcp connect");
					Toast.makeText(activity, R.string.device_connect,
							Toast.LENGTH_SHORT).show();
					break;
				case 3:// tcp connect err主动连接未成功
						// TopTitleFragment.setDeviceConnectState(false);
					MyWindowManager.setNetState(false);
					Toast.makeText(activity,
							"第" + (reConnectTime + 1) + "次" + "设备连接失败了!",
							Toast.LENGTH_SHORT).show();

					if ((reConnectTime++ < 2)) {
						netWorkObject.connectDev();
						netWorkObject.workState = true;
						Log.i(activity.TAG, activity.TAG + "reConnectTime33---"
								+ reConnectTime);
					} else {
						reConnectTime = 0;
						if (connectServerDialog != null)
							connectServerDialog.dismiss();
					}

					break;
				case 4:// tcp connect close
						// TopTitleFragment.setDeviceConnectState(false);
					MyWindowManager.setNetState(false);
					Toast.makeText(activity, R.string.device_connect_close,
							Toast.LENGTH_SHORT).show();
					break;
				case 5:// tcp err
						// TopTitleFragment.setDeviceConnectState(false);
					MyWindowManager.setNetState(false);
					NetErrDialog netErrDialog = (NetErrDialog) activity
							.getFragmentManager().findFragmentByTag("NetErr");
					if (netErrDialog == null) {
						new NetErrDialog().show(activity.getFragmentManager(),
								"NetErr");
					} else if (!netErrDialog.isVisible()) {
						netErrDialog.show(activity.getFragmentManager(),
								"NetErr");
						Log.i(activity.TAG, activity.TAG + "edfhnngf");
					}
					break;
				default:
					break;
				}
			}
		}
	}

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
		if (netState != NetState.TCP_CONN_OPEN) {
			if (!judgeNetConfig(context))// 网络状态错误即不进行后面判断
				return;

			mHandler.sendEmptyMessage(1);

			if (workState == false) {
				connectDev();
				workState = true;
			} else {
				Toast.makeText(context, "正在处理网络连接，请稍后！", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, R.string.device_connect, Toast.LENGTH_SHORT)
					.show();
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
			mHandler.sendEmptyMessage(5);
			return false;
		} else {
			boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.isConnectedOrConnecting();
			if (!wifi) { // 提示使用wifi
				mHandler.sendEmptyMessage(5);
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
	}

	/**
	 * 网络退出
	 */
	public void unInitNetClient() {
		netclient.popDoer();
		netclient.uninit();
	}

	/**
	 * 网络断开后重连时调用
	 */
	public void connectDev() {

		netclient.connectDev();
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

		switch (ev) {
		case TCP_CONN_OPEN:
			setNetStatus(NetState.TCP_CONN_OPEN);
			mHandler.sendEmptyMessage(2);
			break;
		case TCP_CONN_ERR:
			setNetStatus(NetState.TCP_CONN_ERR);
			mHandler.sendEmptyMessage(3);
			break;
		case TCP_CONN_CLOSE:
			setNetStatus(NetState.TCP_CONN_CLOSE);
			mHandler.sendEmptyMessage(4);
			break;
		case TCP_ERR:
			setNetStatus(NetState.TCP_ERR);
			mHandler.sendEmptyMessage(5);
			break;
		default:
			return false;
		}
		return false;
	}

}