package com.kk.nio.socket.telnet.echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 进行echo的服务的开发操作
 * 
 * @author kk
 * @time 2017年3月6日
 * @version 0.0.1
 */
public class EchoServerAttach {

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

					ByteBuffer buffer = ByteBuffer.allocate(100);

					// 读取数据
					socketChannel.read(buffer);

					// 从附着的对象中获取流对象信息
					buffer = (ByteBuffer) selKey.attachment();

					// 如果首次进入，或者如果都已经发送完成
					if (buffer == null || !buffer.hasRemaining()) {
						int lengthBuffer = socketChannel.socket().getSendBufferSize() * 50;

						buffer = ByteBuffer.allocate(lengthBuffer);

						for (int i = 0; i < buffer.capacity() - 2; i++) {
							buffer.put((byte) ('a' + i % 25));
						}

						buffer.flip();
						System.out.println("send another huge block data:" + buffer);

					}
					int writeNum = socketChannel.write(buffer);

					System.out.println("writed :" + writeNum);

					// 如果还有未发送完的数据
					if (buffer.hasRemaining()) {
						System.out.println("not write finish ,bind to session,remain :" + buffer.remaining());
						// 压缩缓存区
						buffer = buffer.compact();
						selKey.attach(buffer);
						//注册写事件
						selKey.interestOps(selKey.interestOps()|SelectionKey.OP_WRITE);
					}
					//数据发送完成后，需要取消attach信息
					else
					{
						System.out.println("block write finish");
						selKey.attach(null);
						selKey.interestOps(selKey.interestOps()&~SelectionKey.OP_WRITE);
					}
				}
				// 处理写事件
				else if ((selKey.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
					System.out.println("received write event");

					ByteBuffer buffer = (ByteBuffer) selKey.attachment();

					SocketChannel socketChannel = (SocketChannel) selKey.channel();

					if (buffer != null) {
						int writed = socketChannel.write(buffer);

						System.out.println("writed " + writed);

						// 如果还有未发送完的数据
						if (buffer.hasRemaining()) {
							System.out.println("not write finished ,bind to session,remain:" + buffer.remaining());
							System.out.println("compact before:"+buffer);
							buffer = buffer.compact();
							//buffer.limit(buffer.position());
							System.out.println("compact over:"+buffer);
							selKey.attach(buffer);
							//注册写事件
							selKey.interestOps(selKey.interestOps()|SelectionKey.OP_WRITE);
						}
						else
						{
							selKey.attach(null);
							selKey.interestOps(selKey.interestOps()&~SelectionKey.OP_WRITE);
						}
					}
				}

				iterSelkey.remove();

			}
		}
	}

	public static void main(String[] args) {
		EchoServerAttach server = new EchoServerAttach();
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
