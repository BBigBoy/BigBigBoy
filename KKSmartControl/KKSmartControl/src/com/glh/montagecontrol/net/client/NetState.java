package com.glh.montagecontrol.net.client;

/**
 * 网络状态的类型值定义
 */
public enum NetState {
	// ////////////////
	// 用户需要处理这些状态
	// ////////////////
	NET_STATUS_ERR,
	/**
	 * tcp连接成功
	 */
	TCP_CONN_OPEN,

	/**
	 * tcp连接失败
	 */
	TCP_CONN_ERR,

	/**
	 * 连接关闭
	 */
	TCP_CONN_CLOSE,

	/**
	 * tcp错误。或连接出错，或包出错
	 */
	TCP_ERR,

	// ////////////////
	// 以下状态，用户不需要处理
	// ////////////////
	UDP_OPEN, // udp打开
	UDP_ERR, // udp错误
}
