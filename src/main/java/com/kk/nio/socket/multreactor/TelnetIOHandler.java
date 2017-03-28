package com.kk.nio.socket.multreactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.reactor.command.CommandRun;
import com.kk.nio.socket.util.CmdUtils;

public class TelnetIOHandler extends MultIOHandler {

	/**
	 * 上一次写入的位置
	 */
	private int lastModPositon = 0;

	public TelnetIOHandler(Selector select, SocketChannel socket) throws IOException {
		super(select, socket);
	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !\n");
		msg.append("1,input command \n");
		msg.append("2,exit \n");

		this.writeData(msg.toString().getBytes());
	}

	@Override
	protected void doHandler() throws IOException {

		// 1,首先从将数据加载到bytebuffer中
		socketChannel.read(readerBuffer);

		// 取得当前的位置
		int readOpts = readerBuffer.position();

		String line = null;

		// 2,将数据按行进行分隔,得到一行记录
		for (int i = lastModPositon; i < readOpts; i++) {
			// 找到换行符
			if (readerBuffer.get(i) == 13) {
				byte[] byteValue = new byte[i - lastModPositon];
				// 标识位置，然后开始读取
				readerBuffer.position(lastModPositon);
				readerBuffer.get(byteValue);

				lastModPositon = i;

				line = new String(byteValue);
				System.out.println("收到msg :" + line);
				break;
			}
		}

		// 3,检查是否需要执行命令,将数据写入返回
		if (null != line && !"".equals(line)) {
			// 取消事件注册，因为要应答数据
			selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_READ);

			// 执行命令
			String msg = CmdUtils.runCmd(line);
			try {
				this.writeData(msg.getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 4,当容量超过总容量的2分之一大小则需要压缩
		if (readerBuffer.position() > readerBuffer.capacity() / 2) {
			// 重新标识position,将当前的容量标识为buffer中已有数据的容量
			readerBuffer.limit(readerBuffer.position());
			// 标识当前pos为上一次已经读取到的数据的pos
			readerBuffer.position(lastModPositon);

			// 压缩数据,即将上一次读取之前的记录丢弃
			readerBuffer.compact();

			lastModPositon = 0;
		}

	}

	@Override
	protected void onError() {
		System.out.println("curr hander process error :");
	}

	@Override
	protected void onClose() {
		this.selectKey.cancel();
		try {
			this.selectKey.channel().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
