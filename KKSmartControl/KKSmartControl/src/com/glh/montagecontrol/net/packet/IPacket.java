package com.glh.montagecontrol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 网络协议包的基接口.
 */
public interface IPacket {
	// 命令字定义。外部调用者需要知道
	int CMD_DevSearch = 0x0100; // 设备搜索
	int CMD_DevSearchAck = 0x0101; // 设备搜索
	int CMD_Alive = 0x1000;

	/**
	 * 拼接命令包的命令字.
	 */
	int CMD_CommandPacket = 0x1001; // 拼接命令
	int CMD_CommandPacketAck = 0x8001; // 拼接命令应答

	// int CMD_SwitchMatrix = 0x1002;
	// int CMD_MontageType = 0x1003; // 拼接方式
	// int CMD_SerialSwitch = 0x1004; // 串口开关
	// int CMD_SleepWakeup = 0x1005; // 睡眠/唤醒
	// int CMD_MontageSignalSwitch = 0x1006; // 拼接信号切换
	// int CMD_ImageMode = 0x1007; // 图像模式
	// int CMD_brightness = 0x1008; // 亮度调节
	// int CMD_stringInput = 0x1009; // 对比度调节
	// int CMD_stringClose = 0x100c; // 背光调节
	// int CMD_stringSend = 0x100a; // 菜单语言调节
	// int CMD_ASonsorPacket = 0x100b; // VGA校正
	// int CMD_mediaShare = 0x2000; // 相位调节
	// int CMD_PlayFile = 0x2001; // VStart调整
	// int CMD_MediaPlayState = 0x4002; // HStart调整
	// int CMD_Stop = 0x2002; // 拼接设置
	// int CMD_Seek = 0x2003; // 遥控命令
	// int CMD_ = 0x;// 鼠标
	// 获取文件信息
	// 播放文件
	// 播放控制命令
	// 同步选中屏/所有屏
	// 串口状态
	// 睡眠状态
	// 回传文件信息
	// 播放状态

	/**
	 * 取包的命令字
	 * 
	 * @return 16位无符号整型值
	 */
	int getCmd();

	// 以下方法，内部使用，外部不要访问。
	void read(DataInputStream is, int len) throws IOException;

	void write(DataOutputStream os) throws IOException;

	int getBodyLen();
}
