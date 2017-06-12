package com.kk.nio.mysqlproxy.mysql;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class MysqClientMidIOHandler extends MysqlClientIOHandlerBase {

	public MysqClientMidIOHandler(Selector select, SocketChannel socket) throws IOException {
		super(select, socket);
	}

	@Override
	protected void doConnection() throws IOException {
		// 进行握手消息的读取
		// msgDataService.readData(context);

	}

	@Override
	protected void doHandler() throws IOException {
		// System.out.println("当前读取操作");

		// msgDataService.readData(context);

	}

	@Override
	protected void onError() {
		System.out.println("curr handler process error");
	}

	@Override
	protected void onClose() {
		if (null != this.selectKey) {
			try {
				this.selectKey.channel().close();
				this.selectKey.cancel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void writeData() throws IOException {
		// 进行消息的发送
	}

}
