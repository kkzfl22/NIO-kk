package com.kk.nio.demo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用于处理服务端的连接,仅处理连接，不进行连接的遍历
 * 
 * @since 2017年6月14日 下午3:47:57
 * @version 0.0.1
 * @author liujun
 */
public class NioAcctor implements Runnable {

	/**
	 * 进行服务端的连接信息
	 */
	private ServerSocketChannel serverChannel;

	/**
	 * 多路rector模式
	 */
	private RectorNio[] rectors;

	/**
	 * 连接选择器对象
	 */
	private Selector connSelect;

	public NioAcctor(int port, RectorNio[] rector) throws IOException {

		this.rectors = rector;
		// 开启服务端的端口信息
		serverChannel = ServerSocketChannel.open();

		// 开启异步模式
		serverChannel.configureBlocking(false);

		serverChannel.bind(new InetSocketAddress(port));

		connSelect = Selector.open();
		// 注册观察连接事件
		serverChannel.register(connSelect, SelectionKey.OP_ACCEPT);

		System.out.println("start success port is :" + port);

		// 进行连接

	}

	@Override
	public void run() {
		Set<SelectionKey> key = null;
		while (true) {

			try {
				connSelect.select(100);

				key = connSelect.selectedKeys();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			for (SelectionKey selKey : key) {

				if (selKey.isAcceptable()) {
					// 当服务器收到连接之后
					try {
						SocketChannel channel = serverChannel.accept();
						int index = ThreadLocalRandom.current().nextInt(0, rectors.length - 1);
						// 注册连接事件
						rectors[index].addChannelQueue(channel);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			key.clear();

		}

	}

}
