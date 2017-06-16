package com.kk.nio.demo.midd.multmidconn;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public abstract class MultMidConnHandlerBase implements Runnable {

	/**
	 * 通道信息
	 */
	protected SocketChannel channel;

	/**
	 * 当前注册的key信息
	 */
	protected SelectionKey currSelectKey;

	public MultMidConnHandlerBase(Selector select, SocketChannel channel) throws IOException {
		this.channel = channel;

		// 设置为非阻塞模式
		channel.configureBlocking(false);

		// 首先注册读取事件
		currSelectKey = channel.register(select, SelectionKey.OP_READ, this);
	}

	@Override
	public void run() {

		// 如果当前是读取事件，交给读取方法
		if (currSelectKey.isReadable()) {
			doMultRead();
		}
		// 如果是写入事件，交给写入方法处理
		else if (currSelectKey.isWritable()) {
			doMultWrite();
		}
	}

	/**
	 * 进行数据读取操作
	 */
	protected abstract void doMultRead();

	/**
	 * 进行数据写入
	 */
	protected abstract void doMultWrite();
}
