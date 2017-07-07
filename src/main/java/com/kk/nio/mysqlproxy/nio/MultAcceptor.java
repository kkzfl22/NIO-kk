package com.kk.nio.mysqlproxy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.kk.nio.mysqlproxy.proc.ConnectHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;

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

		System.out.println("服务启动成功:,端口号:" + port);
	}

	/**
	 * 注册后端的连接信息
	 * 
	 * @param ip
	 *            ip地址
	 * @param port
	 *            端口信息
	 * @param conn
	 *            连接对象
	 * @throws IOException
	 */
	public void registBlackConn(String ip, int port, ConnectHandler conn) throws IOException {
		SocketChannel socketChann = SocketChannel.open();
		socketChann.configureBlocking(false);
		// 进行连接
		socketChann.connect(new InetSocketAddress(ip, port));
		// 注册连接事件
		socketChann.register(this.accSelect, SelectionKey.OP_CONNECT, conn);
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
						// 如果为连接rector事件
						if (selectionKey.isConnectable()) {
							try {
								SocketChannel channel = (SocketChannel) selectionKey.channel();
								// 如果正在连接，则完成连接
								if (channel.isConnectionPending()) {
									channel.finishConnect();
								}

								FrontendMidConnnectHandler midConn = (FrontendMidConnnectHandler) selectionKey
										.attachment();
								// 设置成非阻塞
								channel.configureBlocking(false);

								// 创建mysql的连接对象
								BlackMysqlClientHandler mysqlConn = new BlackMysqlClientHandler();
								mysqlConn.setChannel(channel);
								mysqlConn.setMidConnHandler(midConn);

								// 后端设置前端的信息，做相互的对象绑定
								midConn.setBackMysqlConn(mysqlConn);

								// 将后端的连接对象进行注册
								midConn.getBindRector().registMysqlHandler(mysqlConn);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						// 如果当前为接收连接事件
						if (selectionKey.isAcceptable()) {
							try {
								ServerSocketChannel serverSocketChann = (ServerSocketChannel) selectionKey.channel();
								SocketChannel socketChannel = serverSocketChann.accept();
								// 设置消息为异步模式
								socketChannel.configureBlocking(false);

								FrontendMidConnnectHandler midConn = new FrontendMidConnnectHandler();

								// 设置后端的连接对象信息
								this.registBlackConn("172.16.18.167", 3306, midConn);

								// 将连接注册到多路的rector中
								int index = ThreadLocalRandom.current().nextInt(0, rectors.length - 1);

								// 针对当前的连接指定rector处理
								midConn.setBindRector(rectors[index]);
								midConn.setChannel(socketChannel);

								// 注册中间件
								rectors[index].registMidHandler(midConn);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				}

				acctorSet.clear();
			}

		}

	}

}
