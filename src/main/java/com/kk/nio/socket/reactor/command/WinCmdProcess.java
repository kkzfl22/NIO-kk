package com.kk.nio.socket.reactor.command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.reactor.IOHandler;
import com.kk.nio.socket.util.CmdUtils;

/**
 * 对于window下命令的支持运行
 * 
 * @since 2017年3月20日 下午2:29:42
 * @version 0.0.1
 * @author liujun
 */
public class WinCmdProcess implements CommandInf {

	/**
	 * windows平台下开始字符串
	 */
	public static final String WIN_START = "win";

	@Override
	public String commandProc(String command, SelectionKey selectKey) {

		if (command.startsWith(WIN_START)) {

			String commandProc = command.substring(WIN_START.length()).trim();

			IOHandler hander = (IOHandler) selectKey.attachment();
			
			hander.setRunCmd(WIN_START);

			// 执行命令
			String msg = CmdUtils.runCmd(commandProc);
			try {
				hander.getWriteBuffer().put(msg.getBytes("GBK"));
				hander.getWriteBuffer().flip();
				this.doWrite(hander.getWriteBuffer(), selectKey);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return command;
	}

	@Override
	public String getCommandStart() {
		return WIN_START;
	}

	public void doWrite(ByteBuffer writeBuffer, SelectionKey selectKey) throws IOException {
		SocketChannel channel = (SocketChannel) selectKey.channel();

		int rds = channel.write(writeBuffer);

		// 如果需要继续写入，则继续注册事件，否则，仅关注读取事件
		if (writeBuffer.hasRemaining()) {
			System.out.println("write :" + rds + ";");
			selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_WRITE);
		} else {
			System.out.println("write :" + rds + ";");
			writeBuffer.clear();
			selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		}
	}

}
