package com.kk.nio.socket.reactor.command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * 进入
 * 
 * @since 2017年3月20日 下午2:20:01
 * @version 0.0.1
 * @author liujun
 */
public interface CommandInf {

	/**
	 * 进行命令的处理
	 * 
	 * @param command
	 * @return
	 */
	public String commandProc(String command, SelectionKey selectKey);

	/**
	 * 获取当前命令解析开始的字符串，用于流程
	 * 
	 * @return
	 */
	public String getCommandStart();

	/**
	 * 进行数据的持续写入
	 * 
	 * @param writeBuffer
	 *            缓冲对象信息
	 * @param selectKey
	 *            注册的键信息
	 * @throws IOException
	 *             异常
	 */
	public void doWrite(ByteBuffer writeBuffer, SelectionKey selectKey) throws IOException;

}
