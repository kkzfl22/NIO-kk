package com.kk.nio.socket.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用来进行NIO的轮循操作
 * 
 * @since 2017年3月12日 下午6:18:25
 * @version 0.0.1
 * @author kk
 */
public class ReactorNio extends Thread {

	/**
	 * 选择器对象信息
	 */
	private Selector select;

	/**
	 * 服务器的socket信息
	 */
	private ServerSocketChannel serverSocket;
	
	/**
	 * 线程池处理
	 */
	private ExecutorService execService = Executors.newFixedThreadPool(4);

	/**
	 * 用来构造基本的模型信息
	 * 
	 * @param port
	 *            端口信息
	 */
	public ReactorNio(int port) {

		try {
			select = Selector.open();

			// 构造服务器端口启动信息
			serverSocket = ServerSocketChannel.open();
			// 设置非阻塞模式
			serverSocket.configureBlocking(false);
			serverSocket.bind(new InetSocketAddress(port));

			// 注册连接事件
			serverSocket.register(select, SelectionKey.OP_ACCEPT);
			
			System.out.println("NIO reactor start up port :"+port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void run() {

		while (true) {
			Set<SelectionKey> selectKeySet;
			// 首先进行select()方法
			try {
				select.select();
				selectKeySet = select.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
				// 在发生异常后继续处理
				continue;
			}

			// 针对已经注册的key做连接的轮循操作
			for (SelectionKey selKey : selectKeySet) {
				if (selKey.isAcceptable()) {
					// 进行连接器对象的处理
					new Acceptor(select, serverSocket).run();
				}
				// 当发生其他读取或者写入事件，则使用IOHandler来处理
				else {
					//System.out.println("收到其他处理请求..");
					
					execService.submit((IOHandler) selKey.attachment());
					//进行具体的数据处理 
					//((IOHandler) selKey.attachment()).run();
				}
			}
			//清除非当前感兴趣的事件
			selectKeySet.clear();
		}
	}

}
