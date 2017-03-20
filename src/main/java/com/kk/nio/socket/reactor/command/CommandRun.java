package com.kk.nio.socket.reactor.command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令运行操作
 * @since 2017年3月20日 下午6:03:42
 * @version 0.0.1
 * @author liujun
 */
public class CommandRun {

	/**
	 * 进行数据的运行时的map处理
	 */
	private static final Map<String, CommandInf> RUNCOMMAND = new HashMap<>();

	static {
		// cmd 命令
		CommandInf winCmd = new WinCmdProcess();
		RUNCOMMAND.put(winCmd.getCommandStart(), winCmd);

		// download命令
		CommandInf download = new DownLoadCommandProc();
		RUNCOMMAND.put(download.getCommandStart(), download);
	}

	/**
	 * 进行命令的运行
	 * 
	 * @param command
	 * @param selectKey
	 */
	public void runCmd(String command, SelectionKey selectKey) {
		if (null != command) {
			String runCmd = command.substring(0, command.indexOf(" "));
			RUNCOMMAND.get(runCmd).commandProc(command, selectKey);
		}
	}

	/**
	 * 进行命令的写入
	 * 
	 * @param command
	 * @param selectKey
	 */
	public void doWrite(String command, ByteBuffer writeBuffer, SelectionKey selectKey) {
		if (null != command) {
			try {
				RUNCOMMAND.get(command).doWrite(writeBuffer, selectKey);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
