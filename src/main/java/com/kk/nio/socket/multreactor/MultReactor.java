package com.kk.nio.socket.multreactor;

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
public class MultReactor extends Thread {

	/**
	 * 选择器对象信息
	 */
	private Selector select;

	/**
	 * 线程池信息
	 */
	private final ExecutorService executor;

	public MultReactor(ExecutorService executor) throws IOException {
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
		// new TelnetIOHandler(select, socket);
		// new TelnetEnDecoderIOHandler(select, socket);
		new TelnetChainIOHandler(select, socket);
	}

	@Override
	public void run() {

		Set<SelectionKey> selectKey = null;
		while (true) {
			try {
				select.select(500);
				selectKey = select.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			for (SelectionKey selectionKey : selectKey) {
				// 从attach对象中获取处理对象信息
				// MultIOHandler handler = (MultIOHandler)
				// selectionKey.attachment();
				MultChainIOHandler handler = (MultChainIOHandler) selectionKey.attachment();
				// 提交线程池处理
				executor.execute(handler);
			}

		}

	}

}
