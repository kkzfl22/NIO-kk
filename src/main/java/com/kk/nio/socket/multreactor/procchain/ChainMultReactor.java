package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * 多路reactor实现
 * 
 * @since 2017年3月28日 下午2:15:00
 * @version 0.0.1
 * @author liujun
 */
public class ChainMultReactor extends Thread {

	/**
	 * 选择器对象信息
	 */
	private Selector select;

	/**
	 * 线程池信息
	 */
	private final ExecutorService executor;

	public ChainMultReactor(ExecutorService executor) throws IOException {
		this.select = Selector.open();
		this.executor = executor;
	}

	/**
	 * 注册连接信息
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public void rigisterNewConn(SocketChannel socket) throws IOException {

		System.out.println("register conn :" + socket);
		new ChainTelnetIOHandler(select, socket);
	}

	@Override
	public void run() {

		Set<SelectionKey> selectKey = null;
		while (true) {
			try {
				select.select(100);
				selectKey = select.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			for (SelectionKey selectionKey : selectKey) {
				// 从attach对象中获取处理对象信息
				ChainMultIOHandler handler = (ChainMultIOHandler) selectionKey.attachment();
				if (null != handler) {
					// 提交线程池处理
					executor.execute(handler);
				}

			}
			selectKey.clear();
		}

	}

}
