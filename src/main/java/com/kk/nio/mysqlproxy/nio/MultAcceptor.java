package com.kk.nio.mysqlproxy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 专用进行连接的处理
 * 
 * @since 2017年6月22日 下午11:02:34
 * @version 0.0.1
 * @author liujun
 */
public class MultAcceptor extends Thread {

	/**
	 * 多路rector模式
	 */
	private MultRector[] rectors;

	/**
	 * 进行连接处理的acceptor模式
	 */
	private Selector accSelect;

	public MultAcceptor(MultRector[] rectors) throws IOException {
		super();
		this.rectors = rectors;
		accSelect = Selector.open();
	}

	/**
	 * 进行注册中间件的服务端信息
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void registMidConn(int port) throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();

		serverSocket.configureBlocking(false);
		serverSocket.bind(new InetSocketAddress(port));

		// 注册接收端的信息
		serverSocket.register(this.accSelect, SelectionKey.OP_ACCEPT);
	}

	/**
	 * 注册后端的连接信息
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public void registBlackConn(String ip, int port) throws IOException {
		SocketChannel socketChann = SocketChannel.open();
		socketChann.configureBlocking(false);
		// 进行连接
		socketChann.connect(new InetSocketAddress(ip, port));
		// 注册连接事件
		socketChann.register(this.accSelect, SelectionKey.OP_CONNECT);
	}

	@Override
	public void run() {

		while (true) {
			Set<SelectionKey> acctorSet = null;
			try {
				accSelect.select(200);
				acctorSet = accSelect.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null != acctorSet) {
				for (SelectionKey selectionKey : acctorSet) {
					if (selectionKey.isValid()) {
						// 如果当前为接收连接事件
						if (selectionKey.isAcceptable()) {
							try {
								ServerSocketChannel serverSocketChann = (ServerSocketChannel) selectionKey.channel();
								SocketChannel socketChannel = serverSocketChann.accept();
								// 设置消息为异步模式
								socketChannel.configureBlocking(false);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		}

	}

}
