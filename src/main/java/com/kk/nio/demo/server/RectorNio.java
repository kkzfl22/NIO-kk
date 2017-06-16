package com.kk.nio.demo.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
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

	/**
	 * 队列信息
	 */
	private ConcurrentLinkedQueue<IOHandlerBase> queue = new ConcurrentLinkedQueue<>();

	public RectorNio(ExecutorService executor) throws IOException {
		this.executor = executor;
		select = Selector.open();
	}

	public void addChannelQueue(SocketChannel channel) throws IOException {
		// new TelnetIoHandler(select, channel);
		// 将连接信息注册放入到通道中注册
		queue.add(new TelnetIoHandler(channel));
	}

	public void runRegist() {
		IOHandlerBase base = null;

		while ((base = queue.poll()) != null) {
			try {
				base.currSelectKey = base.socketChannel.register(select, SelectionKey.OP_READ, base);

				// 进行首次的信息输入
				base.doconnect();
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {

		int sels = 0;
		Set<SelectionKey> selKeys = null;
		while (true) {
			try {
				sels = select.select(200);
				runRegist();
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
