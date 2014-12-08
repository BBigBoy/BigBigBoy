package com.glh.montagecontrol.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.util.Log;

import com.glh.montagecontrol.net.client.NetState;
import com.glh.montagecontrol.net.packet.Alive;
import com.glh.montagecontrol.net.packet.IPacket;

// tcp/udp会话。内部接口，外部不需要关注
abstract class SimpleThread extends Thread {
	public void quit() {
		mIsRunning = false;
	}

	public void run() {
		mIsRunning = true;
		initWork();

		while (mIsRunning)
			work();

		uninitWork();
	}

	protected void initWork() {
		Log.d(NetClient.TAG, "SimpleThread.initWork()");
	}

	protected void uninitWork() {
		Log.d(NetClient.TAG, "SimpleThread.uninitWork()");
	}

	protected abstract void work();

	private boolean mIsRunning;
}

class TcpSession extends SimpleThread {
	private NetClient mNetClient;

	public TcpSession(NetClient nc) {
		mNetClient = nc;
	}

	private SocketAddress mAddr;

	public void connect(String ip, int port) {
		mAddr = new InetSocketAddress(ip, port);
		start();
	}

	public void send(IPacket pkt) {
		try {
			pkt.write(mOs);
			mLastSendTime = System.currentTimeMillis();
		} catch (IOException e) {
			Log.e(NetClient.TAG,
					"TcpSession.send(" + pkt.getClass() + ") " + e.getClass()
							+ ": " + e.getMessage());
			mNetClient.distributeNetEvent(NetState.TCP_ERR);
		}
	}

	private long mLastRecvTime;
	private long mLastSendTime;

	protected void initWork() {
		super.initWork();

		try {
			mSock = new Socket();
			mSock.setSoTimeout(SOCKET_BLOCK_TIMEOUT);
			mSock.connect(mAddr, SOCKET_RECV_TIMEOUT);
		} catch (Exception e) {
			Log.e(NetClient.TAG,
					"TcpSession.initWork()\t connect error, " + e.getClass()
							+ ": " + e.getMessage());
			mNetClient.distributeNetEvent(NetState.TCP_CONN_ERR);
			return;
		}

		try {
			mIs = new DataInputStream(mSock.getInputStream());
			mOs = new DataOutputStream(mSock.getOutputStream());
		} catch (IOException e) {
			Log.e(NetClient.TAG, "TcpSession.initWork()\t link stream error, "
					+ e.getClass() + ": " + e.getMessage());
			mNetClient.distributeNetEvent(NetState.TCP_CONN_ERR);
			return;
		}

		mLastRecvTime = mLastSendTime = System.currentTimeMillis();

		setNetState(NetState.TCP_CONN_OPEN);
	}

	protected void uninitWork() {
		try {
			mIs.close();
		} catch (Exception e) {
		}

		try {
			mOs.close();
		} catch (Exception e) {
		}

		try {
			mSock.close();
		} catch (Exception e) {
		}

		if (mNetState == NetState.TCP_CONN_OPEN)
			setNetState(NetState.TCP_CONN_CLOSE);

		super.uninitWork();
	}

	private int mTotalLen = 0;
	private int mRecvLen = 0;
	private byte[] mBuf = new byte[PACKET_MAZ_SIZE];

	protected void work() {
		Log.d(NetClient.TAG,
				"TcpSession.work()\t  mLastSendTime="
						+ (System.currentTimeMillis() - mLastSendTime)
						+ ", mLastRecvTime="
						+ (System.currentTimeMillis() - mLastRecvTime)
						+ ", mTotalLen=" + mTotalLen + ", mRecvLen=" + mRecvLen);

		if (System.currentTimeMillis() - mLastSendTime > SOCKET_NOSEND_TIMEOUT)
			send(new Alive());

		if (System.currentTimeMillis() - mLastRecvTime > SOCKET_RECV_TIMEOUT) {
			Log.e(NetClient.TAG, "TcpSession.work()\t read timeout");
			setNetState(NetState.TCP_ERR);
			return;
		}

		if (mTotalLen == 0) {
			try {
				mTotalLen = mIs.readUnsignedShort() + 2;
				mRecvLen = 0;
				mLastRecvTime = System.currentTimeMillis();
			} catch (IOException e) {
				if (e instanceof InterruptedIOException
						&& ((InterruptedIOException) e).bytesTransferred == 0) {
					mLastRecvTime = System.currentTimeMillis();
					return;
				}

				Log.e(NetClient.TAG,
						"TcpSession.work()\t read len error, " + e.getClass()
								+ ": " + e.getMessage());
				setNetState(NetState.TCP_ERR);
				return;
			}
		}

		if (mTotalLen > mRecvLen) {
			try {
				int l = mIs.read(mBuf, mRecvLen, mTotalLen - mRecvLen);
				mLastRecvTime = System.currentTimeMillis();

				if (l == -1)
					return;
				else {
					mRecvLen += l;
					if (mRecvLen < mTotalLen)
						return;
				}
			} catch (InterruptedIOException e) {
				mLastRecvTime = System.currentTimeMillis();

				mRecvLen += e.bytesTransferred;
				if (mRecvLen < mTotalLen)
					return;
			} catch (IOException e) {
				Log.e(NetClient.TAG,
						"readToPacket()\t read packet error, " + e.getClass()
								+ ": " + e.getMessage());
				setNetState(NetState.TCP_ERR);
				return;
			}
		}

		DataInputStream is = new DataInputStream(new ByteArrayInputStream(mBuf,
				0, mRecvLen));
		try {
			mNetClient.readToPacket(is, null, mRecvLen);
		} catch (IOException e) {
			Log.e(NetClient.TAG,
					"readToPacket()\t readToPacket error, " + e.getClass()
							+ ": " + e.getMessage());
			setNetState(NetState.TCP_ERR);
		}

		try {
			is.close();
		} catch (IOException e) {
		}

		mTotalLen = 0;
		mRecvLen = 0;
	}

	private NetState mNetState;

	void setNetState(NetState state) {
		mNetState = state;
		mNetClient.distributeNetEvent(mNetState);

		if (state == NetState.TCP_CONN_ERR || state == NetState.TCP_ERR)
			quit();
	}

	private Socket mSock;
	private DataInputStream mIs;
	private DataOutputStream mOs;

	static final int SOCKET_BLOCK_TIMEOUT = 1000; // socket阻塞时间，ms
	static final int SOCKET_NOSEND_TIMEOUT = 3000; // 无发数据的超时间隔
	static final int SOCKET_RECV_TIMEOUT = 10000; // 收数据失败的超时，3 *
													// SOCKET_SEND_TIMEOUT
	static final int PACKET_MAZ_SIZE = 8 * 1024; // 8k报文
};

class UdpSession extends SimpleThread {
	private NetClient mNetClient;

	public UdpSession(NetClient nc) {
		mNetClient = nc;
	}

	public void send(SocketAddress sa, IPacket pkt) {
		int len = pkt.getBodyLen() + 4;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(len);

		try {
			pkt.write(new DataOutputStream(bos));
		} catch (IOException e) {
			Log.e(NetClient.TAG, "UDPSession.send()\t " + pkt.getClass()
					+ ") write error:\t  " + e.getMessage());
			setNetState(NetState.UDP_ERR);
			return;
		}

		DatagramPacket dp = new DatagramPacket(bos.toByteArray(), len);
		dp.setSocketAddress(sa);
		try {
			if (mSock == null)
				Log.d("mSockmSockmSockmSockmSock", "mSockmSockmSockmSockmSock");
			if (mSock != null)
				mSock.send(dp);
		} catch (IOException e) {
			Log.e(NetClient.TAG, "UdpSession.send(" + pkt.getClass()
					+ ")\t send error, " + e.getClass() + ": " + e.getMessage());
			setNetState(NetState.UDP_ERR);
			return;
		}

		Log.d(NetClient.TAG, "UdpSession.send(" + pkt.getClass() + ")\t sent");
	}

	private DatagramSocket mSock;

	protected void initWork() {
		super.initWork();

		try {
			mSock = new DatagramSocket();
			mSock.setSoTimeout(TcpSession.SOCKET_RECV_TIMEOUT);
			mSock.setBroadcast(true);
		} catch (Exception e) {
			Log.e(NetClient.TAG, "UdpSession.initWork()\t " + e.getClass()
					+ ": " + e.getMessage());
			setNetState(NetState.UDP_ERR);
			return;
		}

		setNetState(NetState.UDP_OPEN);
	}

	protected void uninitWork() {
		try {
			mSock.close();
		} catch (Exception e) {
		}

		super.uninitWork();
	}

	private byte[] mBuf = new byte[TcpSession.PACKET_MAZ_SIZE];
	private DatagramPacket mPacket = new DatagramPacket(mBuf,
			TcpSession.PACKET_MAZ_SIZE);

	protected void work() {
		try {
			mSock.receive(mPacket);
		} catch (IOException e) {
			Log.e(NetClient.TAG,
					"UdpSession.work()\t receive error, " + e.getClass() + ": "
							+ e.getMessage());
			setNetState(NetState.UDP_ERR);
			return;
		}

		DataInputStream is = new DataInputStream(new ByteArrayInputStream(
				mPacket.getData()));
		try {
			mNetClient.readToPacket(is, mPacket.getSocketAddress(), -1);
		} catch (IOException e) {
			Log.e(NetClient.TAG,
					"UdpSession.work() read packet\t " + e.getClass() + ": "
							+ e.getMessage());
		}

		try {
			is.close();
		} catch (IOException e) {
		}

		quit();
	}

	private NetState mNetState;

	void setNetState(NetState state) {
		mNetState = state;
		mNetClient.distributeNetEvent(mNetState);

		if (state == NetState.UDP_ERR)
			quit();
	}
}
