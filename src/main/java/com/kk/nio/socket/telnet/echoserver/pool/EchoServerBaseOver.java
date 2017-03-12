package com.kk.nio.socket.telnet.echoserver.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.Limit;

/**
 * 进行echo的服务的开发操作
 * 
 * @author kk
 * @time 2017年3月6日
 * @version 0.0.1
 */
public class EchoServerBaseOver {

	private AtomicInteger writeNum = new AtomicInteger(0);

	private AtomicInteger writeBytes = new AtomicInteger(0);

	/**
	 * 启动服务操作
	 * 
	 * @param port
	 *            端口信息
	 * @throws IOException
	 */
	public void startUp(int port) throws IOException {
		// 构造选择器对象
		Selector select = Selector.open();

		// 构造服务端socket对象
		ServerSocketChannel serverSocket = ServerSocketChannel.open();

		serverSocket.configureBlocking(false);

		// 绑定具体到端口
		serverSocket.bind(new InetSocketAddress(port));

		// 注册连接事件
		serverSocket.register(select, SelectionKey.OP_ACCEPT);

		while (true) {
			// 每隔0.5秒进行一次尝试激活
			int selectnum = select.select(500);

			// System.out.println("已经激活的键信息:"+selectnum);

			Iterator<SelectionKey> iterSelkey = select.selectedKeys().iterator();

			while (iterSelkey.hasNext()) {
				SelectionKey selKey = iterSelkey.next();

				iterSelkey.remove();

				// 如果当前为连接事件，进行处理
				if ((selKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					// 获得通道
					ServerSocketChannel serverSocketChan = (ServerSocketChannel) selKey.channel();

					// 进行连接处理
					SocketChannel sockChannel = serverSocketChan.accept();

					sockChannel.configureBlocking(false);

					// 注册读取事件
					sockChannel.register(select, SelectionKey.OP_READ);

					// 写入提示信息
					sockChannel.write(ByteBuffer.wrap("Welcome come to kk echo server!\r\n".getBytes()));

				}
				// 进行读事件处理
				else if ((selKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					SocketChannel socketChannel = (SocketChannel) selKey.channel();
					// 1,读取客户端的输入
					ByteBuffer buffer = ByteBuffer.allocate(100);
					socketChannel.read(buffer);
					// 首先产生一个100倍缓冲区大小的缓冲区，然后数字据填充
					int buffsize = socketChannel.socket().getSendBufferSize() * 100;
					ByteBuffer sendBuffer = ByteBuffer.allocate(buffsize);
					for (int i = 0; i < buffsize - 2; i++) {
						sendBuffer.put((byte) ('a' + i % 25));
					}
					System.out.println("总大小:" + buffsize);
					sendBuffer.flip();
					// 将数据进行响应的发送
					int nums = socketChannel.write(sendBuffer);
					writeNum.addAndGet(1);
					System.out.println("读取第" + writeNum.get() + "次写入大小：" + nums);
					writeBytes.addAndGet(nums);
					System.out.println("读取第" + writeNum.get() + "次写入总大小：" + writeBytes.get());

					// 检查是否已经发送完成,未发送完成，则进行放入附着对象中
					if (sendBuffer.hasRemaining()) {
						// 将数据压缩，放入到attach对象中
						sendBuffer.compact();
						// 重新标识当前的positon信息
						sendBuffer.limit(sendBuffer.position());
						sendBuffer.position(0);
						
						selKey.attach(sendBuffer);
						// 继续写事件
						selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
					}
					// 如果发送完成，则进行
					else {
						// 将数据压缩，放入到attach对象中
						selKey.attach(null);
						// 取消写事件
						selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_WRITE);
					}
				}
				// 处理写事件
				else if ((selKey.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
					SocketChannel socketChannel = (SocketChannel) selKey.channel();

					// 从附着的对象中获取发送对象
					ByteBuffer sendBuffer = (ByteBuffer) selKey.attachment();
					if (null != sendBuffer) {
						try {
							int nums = socketChannel.write(sendBuffer);
							writeNum.addAndGet(1);
							System.out.println("写入第" + writeNum.get() + "次写入大小：" + nums);
							writeBytes.addAndGet(nums);
							System.out.println("写入第" + writeNum.get() + "次写入总大小：" + writeBytes.get());
						} catch (Exception e) {
							e.printStackTrace();
						}

						// 如果还有未发送的数据，则继续监听写事件，继续的发送数据
						if (sendBuffer.hasRemaining()) {
							sendBuffer.compact();
							// 重新标识当前的positon信息
							sendBuffer.limit(sendBuffer.position());
							sendBuffer.position(0);
							selKey.attach(sendBuffer);
							selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
						}
						// 如果发送完成，取消写事件
						else {
							selKey.attach(null);
							selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_WRITE);
						}
					}
					// 如果已经完成，则取消息写事件
					else {
						selKey.attach(null);
						selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_WRITE);
					}
				}

			}
		}
	}

	public static void main(String[] args) {
		EchoServerBaseOver server = new EchoServerBaseOver();
		try {
			int port = 94;

			System.out.println("服务启动中！端口：" + port);
			server.startUp(port);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
