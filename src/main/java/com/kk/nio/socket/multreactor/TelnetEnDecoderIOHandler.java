package com.kk.nio.socket.multreactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.multreactor.endecoder.CoderProcess;
import com.kk.nio.socket.multreactor.endecoder.EnDecoderBean;
import com.kk.nio.socket.multreactor.endecoder.LineMsgCoderProcess;
import com.kk.nio.socket.reactor.command.CommandRun;
import com.kk.nio.socket.util.CmdUtils;

/**
 * 带消息编解处理的实现
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class TelnetEnDecoderIOHandler extends MultIOHandler {

	/**
	 * 上一次写入的位置
	 */
	private int lastModPositon = 0;

	private CoderProcess<String> process = new LineMsgCoderProcess();

	public TelnetEnDecoderIOHandler(Selector select, SocketChannel socket) throws IOException {
		super(select, socket);
	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !\r\n");
		msg.append("1,input command \r\n");
		msg.append("2,exit \r\n");

		this.writeData(msg.toString().getBytes());
	}

	@Override
	protected void doHandler() throws IOException {

		// 1,首先从将数据加载到bytebuffer中
		socketChannel.read(readerBuffer);

		// 首先进行消息的解码
		EnDecoderBean decodeBean = new EnDecoderBean(readerBuffer, lastModPositon);
		String line = process.decoder(decodeBean);

		// 重新设置最后的位置
		this.lastModPositon = decodeBean.getLastModPositon();

		// 3,检查是否需要执行命令,将数据写入返回
		if (null != line && !"".equals(line)) {
			// 取消事件注册
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

}
