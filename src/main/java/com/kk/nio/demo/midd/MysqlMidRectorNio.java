package com.kk.nio.demo.midd;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import com.kk.nio.demo.midd.handler.BaseHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;

public class MysqlMidRectorNio extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 线程池框架
	 */
	private final ExecutorService executor;

	/**
	 * 后端的连接信息
	 */
	private ConcurrentLinkedQueue<BaseHandler> blackConn = new ConcurrentLinkedQueue<>();

	public MysqlMidRectorNio(ExecutorService executor) throws IOException {
		this.executor = executor;
		select = Selector.open();
	}


	/**
	 * 进行中间件的提供服务的连接注册
	 * 
	 * @param channel
	 *            通道信息
	 * @throws IOException
	 *             异常信息
	 */
	public void registMultMidConnChannel(BaseHandler multMidConn) throws IOException {
		blackConn.offer(multMidConn);
	}

	/**
	 * 自理注册连接信息
	 */
	public void processRegistConn() {
		BaseHandler handler = null;
		while ((handler = blackConn.poll()) != null) {
			try {
				// 进行事件的注册操作
				handler.setCurrSelectKey(handler.getChannel().register(this.select, handler.getRegistEvent(), handler));
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
				// 被激活之后首先进行连接的注册
				processRegistConn();
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
