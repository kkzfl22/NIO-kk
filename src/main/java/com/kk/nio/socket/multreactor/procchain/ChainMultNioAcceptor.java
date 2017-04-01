package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用来进行连接的管理
 * 
 * @since 2017年3月28日 下午2:12:55
 * @version 0.0.1
 * @author liujun
 */
public class ChainMultNioAcceptor implements Runnable {

	/**
	 * 服务端socket信息
	 */
	private ServerSocketChannel serverChannel;

	/**
	 * 多路选择器
	 */
	private ChainMultReactor[] reactor;

	/**
	 * 连接选择器
	 */
	private Selector acceptorSelect;

	public ChainMultNioAcceptor(int port, ChainMultReactor[] reactor) throws IOException {

		this.reactor = reactor;

		acceptorSelect = Selector.open();
		// 绑定到端口信息
		serverChannel = ServerSocketChannel.open();
		// 绑定到端口
		serverChannel.bind(new InetSocketAddress(port));

		// 最开始设置为阻塞模式,改为非阻塞模式
		serverChannel.configureBlocking(false);

		// 注册观察连接事件
		serverChannel.register(acceptorSelect, SelectionKey.OP_ACCEPT);

		System.out.println("start port :" + port);
	}

	@Override
	public void run() {

		Set<SelectionKey> selectKey = null;

		while (true) {
			// 连接的异常与处理连接的异常分开处理
			try {
				acceptorSelect.select(100);
				selectKey = acceptorSelect.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			for (SelectionKey selectionKey : selectKey) {
				// 如果为连接事件则注册到具体的某个reactor处理
				if (selectionKey.isAcceptable()) {
					try {
						TimeColltion.addTime("1_accept", System.currentTimeMillis());

						// 交给其他的任务线程去处理
						ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
						// 接收到socket连接
						SocketChannel socket = channel.accept();
						System.out.println("curr connection event :" + socket.getRemoteAddress());
						int index = ThreadLocalRandom.current().nextInt(0, this.reactor.length);
						// 注册新连接
						reactor[index].rigisterNewConn(socket);
					} catch (IOException e) {
						e.printStackTrace();
						selectionKey.cancel();
					}
				}
			}

			selectKey.clear();

		}

	}

}
