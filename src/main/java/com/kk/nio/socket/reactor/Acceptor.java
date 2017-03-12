package com.kk.nio.socket.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 用来处理连接请求
 * 
 * @since 2017年3月12日 下午6:17:49
 * @version 0.0.1
 * @author kk
 */
public class Acceptor implements Runnable {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 服务端的socket对象信息
	 */
	private ServerSocketChannel serverSocket;

	public Acceptor(Selector select, ServerSocketChannel serverSocket) {
		this.select = select;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			SocketChannel socket = serverSocket.accept();
			// 将handler绑定到attach对象中
			new IOHandler(socket, select);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
