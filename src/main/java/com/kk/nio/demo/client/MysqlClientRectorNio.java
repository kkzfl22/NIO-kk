package com.kk.nio.demo.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import com.kk.nio.demo.client.queue.RegBean;

public class MysqlClientRectorNio extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 线程池框架
	 */
	private final ExecutorService executor;

	/**
	 * 操作队列信息
	 */
	private ConcurrentLinkedQueue<RegBean> connectQueue = new ConcurrentLinkedQueue<>();

	public MysqlClientRectorNio(ExecutorService executor) throws IOException {
		this.executor = executor;
		select = Selector.open();
	}

	public void regectServerChannel(MysqlClientIoHandler hanlder, SocketChannel channel) throws IOException {
		// new MysqlClientIoHandler(select, channel);
		RegBean req = hanlder.registObj(select, channel);
		connectQueue.offer(req);

		System.out.println("成功共用regist:" + regist);
	}

	public static int regist = 0;

	public void regist() {
		RegBean req = null;

		while ((req = connectQueue.poll()) != null) {
			try {
				SelectionKey currKey = req.getSocketChannel().register(req.getSelect(), SelectionKey.OP_READ,
						req.getHandler());
				req.getHandler().setSelectKey(currKey);

				System.out.println("注册完成!");
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
				sels = select.select(100);

				regist++;
				regist();
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
					MysqlClientIOHandlerBase ioHandler = (MysqlClientIOHandlerBase) selectionKey.attachment();

					// 将当前的任务提交线程池处理
					executor.submit(ioHandler);
				}

				selKeys.clear();
			}

		}
	}

}
