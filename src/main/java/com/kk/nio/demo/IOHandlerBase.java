package com.kk.nio.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 进行io事情处理的基本类
 * 
 * @since 2017年6月14日 下午4:00:24
 * @version 0.0.1
 * @author liujun
 */
public class IOHandlerBase implements Runnable {

	/**
	 * 通道信息
	 */
	private SocketChannel channel;

	/**
	 * 当前注册的key信息
	 */
	private SelectionKey currSelectKey;

	/**
	 * 读取的buffer信息
	 */
	private ByteBuffer readBuffer;

	/**
	 * 进行写入的buffer信息
	 */
	private ByteBuffer writeBuffer;

	/**
	 * 读取到的字节数
	 */
	private int readOption = 0;

	/**
	 * 写入标识，只能一个写入
	 */
	private AtomicBoolean writingFlag = new AtomicBoolean(false);

	public IOHandlerBase(Selector select, SocketChannel channel) throws IOException {
		super();
		this.channel = channel;

		// 设置为非阻塞模式
		channel.configureBlocking(false);

		// 首先注册读取事件
		currSelectKey = channel.register(select, SelectionKey.OP_READ);

		currSelectKey.attach(this);

		// 定义一个512字节大小的缓冲区
		writeBuffer = ByteBuffer.allocate(1024 * 512);
		readBuffer = ByteBuffer.allocate(1024 * 512);

		writeBuffer.put("welcome to kk server!!!\n".getBytes());

		writeBuffer.flip();
		channel.write(writeBuffer);
		writeBuffer.clear();
	}

	@Override
	public void run() {

		// 如果当前是读取事件，交给读取方法
		if (currSelectKey.isReadable()) {
			doRead();
		}
		// 如果是写入事件，交给写入方法处理
		else if (currSelectKey.isWritable()) {
			doWrite();
		}
	}

	/**
	 * 进行事件的读取操作
	 */
	public void doRead() {
		try {
			int readByte = channel.read(readBuffer);

			if (readByte > 0) {
				// 解析数据
				int currPosition = readBuffer.position();
				// 进行检查是否收到了回车符
				for (int i = readOption; i < currPosition; i++) {
					if (readBuffer.get(i) == 13) {
						byte[] bufferValue = new byte[i - readOption];
						readBuffer.position(readOption);
						readBuffer.get(bufferValue);
						String msg = new String(bufferValue);

						readOption = i;

						System.out.println("收到消息:" + msg);

						// 向缓冲区中写入数据
						writeBuffer.put(new String("write msg !! ").getBytes());

						// 将当前的事件注册为写入
						currSelectKey.interestOps(currSelectKey.interestOps() | SelectionKey.OP_WRITE);
						break;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doWrite() {
		// 进行数据写入操作

		if (writingFlag.compareAndSet(false, true)) {
			try {
				writeBuffer.flip();
				int writeBytes = channel.write(writeBuffer);

				// 检查当前是否还存在需要写入的数据，如果需要，则进行写入
				if (writeBuffer.hasRemaining()) {
					System.out.println("写入成功:" + writeBytes + "字节");
					currSelectKey.interestOps(currSelectKey.interestOps() | SelectionKey.OP_WRITE);
				}
				// 写入完成，取消写事件，注册读取事件
				else {
					writeBuffer.clear();
					System.out.println("取消写事件!");
					currSelectKey
							.interestOps((currSelectKey.interestOps() & ~SelectionKey.OP_WRITE) | SelectionKey.OP_READ);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				writingFlag.set(false);
			}
		}
	}

}
