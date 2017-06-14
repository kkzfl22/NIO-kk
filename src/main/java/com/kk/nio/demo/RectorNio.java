package com.kk.nio.demo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class RectorNio extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 线程池框架
	 */
	private final ExecutorService executor;

	public RectorNio(ExecutorService executor) throws IOException {
		this.executor = executor;
		select = Selector.open();
	}

	public void regectServerChannel(SocketChannel channel) throws IOException {
		new IOHandlerBase(select, channel);
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
					IOHandlerBase ioHandler = (IOHandlerBase) selectionKey.attachment();

					// 将当前的任务提交线程池处理
					executor.submit(ioHandler);
				}

				selKeys.clear();
			}

		}
	}

}
