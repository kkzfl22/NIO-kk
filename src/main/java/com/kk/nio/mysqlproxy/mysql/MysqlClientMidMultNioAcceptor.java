package com.kk.nio.mysqlproxy.mysql;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import com.kk.nio.mysql.bean.MysqlConnBean;

/**
 * 用来进行连接的管理
 * 
 * @since 2017年3月28日 下午2:12:55
 * @version 0.0.1
 * @author liujun
 */
public class MysqlClientMidMultNioAcceptor implements Runnable {

	/**
	 * socket连接通道
	 */
	private SocketChannel sockethannel;

	/**
	 * 多路选择器,进行具体的业务处理的reactor
	 */
	private MysqlClientMultReactor[] reactor;

	/**
	 * 连接选择器
	 */
	private Selector acceptorSelect;

	/**
	 * 最大连接数
	 */
	private static final int maxQueueSize = 512;

	/**
	 * 最大的连接数的队列信息
	 */
	private static ArrayBlockingQueue<MysqlConnBean> CONNQUEUE = new ArrayBlockingQueue<>(maxQueueSize);

	public MysqlClientMidMultNioAcceptor(MysqlClientMultReactor[] reactor) throws IOException {
		this.reactor = reactor;
		acceptorSelect = Selector.open();

		sockethannel = SocketChannel.open();
		// 最开始设置为阻塞模式,改为非阻塞模式
		sockethannel.configureBlocking(false);

		// 启动注册管理的线程
		new Thread(new ConnReg()).start();
	}

	/**
	 * 注册连接
	 * 
	 * @param conn
	 *            连接信息
	 */
	public static void AddConnection(MysqlConnBean conn) {
		CONNQUEUE.add(conn);
	}

	/**
	 * 连接注册线程
	 * 
	 * @since 2017年4月16日 下午7:25:56
	 * @version 0.0.1
	 * @author kk
	 */
	class ConnReg implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					MysqlConnBean takeConn = CONNQUEUE.take();

					sockethannel.connect(new InetSocketAddress(takeConn.getServerIP(), takeConn.getPort()));
					// 注册连接事件
					sockethannel.register(acceptorSelect, SelectionKey.OP_CONNECT);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {

		Set<SelectionKey> selectKey = null;

		while (true) {
			try {
				acceptorSelect.select(100);
				selectKey = acceptorSelect.selectedKeys();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			for (SelectionKey selectionKey : selectKey) {
				// 如果为连接事件则注册到具体的某个reactor处理
				if (selectionKey.isConnectable()) {

					SocketChannel socket = (SocketChannel) selectionKey.channel();

					if (socket.isConnectionPending()) {
						// 结束连接，以完成整个连接过程
						try {
							socket.finishConnect();
							// 完成连接后，则进行连接的注册与取消
							socket.register(acceptorSelect, SelectionKey.OP_READ);
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
				// 其他的数据处理则交给具体的线程去处理
				else if (selectionKey.isReadable()) {
					try {
						SocketChannel socket = (SocketChannel) selectionKey.channel();
						// 交给其他的任务线程去处理
						System.out.println("curr connection event :" + socket.getRemoteAddress());
						int index = ThreadLocalRandom.current().nextInt(0, this.reactor.length);
						// 注册新连接
						reactor[index].rigisterNewConn(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					selectionKey.cancel();
				}
			}

			selectKey.clear();

		}

	}

}
