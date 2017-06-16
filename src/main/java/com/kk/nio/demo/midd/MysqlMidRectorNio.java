package com.kk.nio.demo.midd;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.kk.nio.demo.midd.blackmysqlconn.BlackmysqlConnHandler;
import com.kk.nio.demo.midd.blackmysqlconn.BlackmysqlConnHandlerBase;
import com.kk.nio.demo.midd.multmidconn.MultMidConnHandler;

public class MysqlMidRectorNio extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 线程池框架
	 */
	private final ExecutorService executor;

	public MysqlMidRectorNio(ExecutorService executor) throws IOException {
		this.executor = executor;
		select = Selector.open();
	}

	/**
	 * 进行中间件连接mysql的连接的注册
	 * 
	 * @param channel
	 * @throws IOException
	 */
	public void registBlackMysqlConnChannel(SocketChannel channel) throws IOException {
		new BlackmysqlConnHandler(select, channel);
	}

	/**
	 * 进行中间件的提供服务的连接注册
	 * 
	 * @param channel
	 * @throws IOException
	 */
	public void registMultMidConnChannel(SocketChannel channel) throws IOException {
		new MultMidConnHandler(select, channel);
	}

	@Override
	public void run() {

		int sels = 0;
		Set<SelectionKey> selKeys = null;
		while (true) {
			try {
				sels = select.select(200);
				if (sels > 0) {
					// 获取已经成功注册的键信息
					selKeys = select.selectedKeys();
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			if (null != selKeys) {
				for (SelectionKey selectionKey : selKeys) {
					Runnable ioHandler = (Runnable) selectionKey.attachment();
					// 将当前的任务提交线程池处理
					executor.submit(ioHandler);
				}

				selKeys.clear();
			}

		}
	}

}
