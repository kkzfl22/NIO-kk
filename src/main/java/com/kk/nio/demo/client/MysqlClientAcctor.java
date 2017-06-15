package com.kk.nio.demo.client;

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
public class MysqlClientAcctor implements Runnable {

	/**
	 * 进行服务端的连接信息
	 */
	private SocketChannel socketChannel;

	/**
	 * 多路rector模式
	 */
	private MysqlClientRectorNio[] rectors;

	/**
	 * 连接选择器对象
	 */
	private Selector connSelect;

	public MysqlClientAcctor(String ip, int port, MysqlClientRectorNio[] rector) throws IOException {

		this.rectors = rector;
		// 开启服务端的端口信息
		socketChannel = SocketChannel.open();

		// 开启异步模式
		socketChannel.configureBlocking(false);

		socketChannel.connect(new InetSocketAddress(ip, port));

		connSelect = Selector.open();

		// 注册连接事件
		socketChannel.register(connSelect, SelectionKey.OP_CONNECT);

	}

	@Override
	public void run() {
		Set<SelectionKey> key = null;
		while (true) {

			try {
				connSelect.select(100);

				key = connSelect.selectedKeys();

			} catch (IOException e) {
				e.printStackTrace();
			}

			for (SelectionKey selKey : key) {

				if (selKey.isConnectable()) {
					try {
						SocketChannel channel = (SocketChannel) selKey.channel();
						// 如果正在连接，则完成连接
						if (channel.isConnectionPending()) {
							channel.finishConnect();
						}
						// 设置成非阻塞
						channel.configureBlocking(false);
						// 当服务器收到连接之后
						int index = ThreadLocalRandom.current().nextInt(0, rectors.length - 1);
						// 注册连接事件
						rectors[index].regectServerChannel(channel);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			key.clear();

		}

	}

}
