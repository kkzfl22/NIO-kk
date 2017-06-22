package com.kk.nio.demo.midd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.kk.nio.demo.midd.handler.blackmysqlconn.BlackmysqlConnHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;
import com.kk.nio.demo.midd.memory.MemoryPool;

/**
 * 用于处理服务端的连接,仅处理连接，不进行连接的遍历
 * 
 * @since 2017年6月14日 下午3:47:57
 * @version 0.0.1
 * @author liujun
 */
public class MysqlMidAcctor implements Runnable {

	/**
	 * 多路rector模式
	 */
	private MysqlMidRectorNio[] rectors;

	/**
	 * 连接选择器对象
	 */
	private Selector connSelect;

	public MysqlMidAcctor(MysqlMidRectorNio[] rector) throws IOException {
		this.rectors = rector;
		connSelect = Selector.open();
	}

	/**
	 * 注册后端的连接
	 * 
	 * @param ip
	 *            ip信息
	 * @param port
	 *            端口信息
	 * @return 返回通道信息
	 * @throws IOException
	 *             异常
	 */
	public BlackmysqlConnHandler regictBlackMysqlConn(String ip, int port) throws IOException {
		// 客户端的连接信息
		SocketChannel socketChannel = SocketChannel.open();
		// 开启异步模式
		socketChannel.configureBlocking(false);

		socketChannel.connect(new InetSocketAddress(ip, port));

		// 将处理对象附着到通道中
		BlackmysqlConnHandler connHandler = new BlackmysqlConnHandler(socketChannel, SelectionKey.OP_READ);

		// 获取一个读取的byteBuffer
		connHandler.setReadBuffer(MemoryPool.Instance().allocate(1024 * 1024));
		// 获取一个写入的bytebuffer
		connHandler.setWriteBuffer(MemoryPool.Instance().allocate(1024 * 1024));

		// 注册连接事件
		socketChannel.register(connSelect, SelectionKey.OP_CONNECT, connHandler);

		return connHandler;
	}

	/**
	 * 注册前端的连接信息
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void regiectMultConn(int port) throws IOException {
		// 开启服务端的端口信息
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 开启异步模式
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(port));

		// 注册观察连接事件
		serverChannel.register(connSelect, SelectionKey.OP_ACCEPT);

		System.out.println("mysql mid server  start success ,port is " + port);
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
				// 进行客户端连接服务端的处理
				if (selKey.isConnectable()) {
					try {
						SocketChannel channel = (SocketChannel) selKey.channel();
						// 如果正在连接，则完成连接
						if (channel.isConnectionPending()) {
							channel.finishConnect();
						}

						BlackmysqlConnHandler mysqlHandler = (BlackmysqlConnHandler) selKey.attachment();
						// 设置成非阻塞
						channel.configureBlocking(false);
						// 当服务器收到连接之后
						int index = ThreadLocalRandom.current().nextInt(0, rectors.length - 1);
						// 注册连接事件
						rectors[index].registBlackMysqlConnChannel(mysqlHandler);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 进行服务器端面消息进行处理
				else if (selKey.isAcceptable()) {
					// 当服务器收到连接之后
					try {
						ServerSocketChannel serverChannel = (ServerSocketChannel) selKey.channel();
						SocketChannel channel = serverChannel.accept();
						// 设置为异步
						channel.configureBlocking(false);

						// 进行创建后端的连接
						BlackmysqlConnHandler blackMysqlConn = regictBlackMysqlConn("localhost", 3306);

						int index = ThreadLocalRandom.current().nextInt(0, rectors.length - 1);
						// 创建中间件接口器对象
						MultMidConnHandler multMidConn = new MultMidConnHandler(channel, SelectionKey.OP_WRITE,
								blackMysqlConn);
						// 注册连接事件
						rectors[index].registMultMidConnChannel(multMidConn);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			key.clear();

		}

	}

}
