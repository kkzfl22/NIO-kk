package com.kk.nio.demo.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class TelnetIoHandler extends IOHandlerBase {

	/**
	 * 写入标识，只能一个写入
	 */
	private AtomicBoolean writingFlag = new AtomicBoolean(false);

	public TelnetIoHandler(SocketChannel socketChannel) throws IOException {
		super(socketChannel);
	}

	/**
	 * 进行事件的读取操作
	 */
	public void doRead() {
		try {
			// 通道信息
			SocketChannel channel = (SocketChannel) currSelectKey.channel();
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
						writeBuffer.clear();
						// 向缓冲区中写入数据
						writeBuffer.put(new String("receving msg is :" + msg + "\r\n").getBytes());
						writeBuffer.flip();
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

		while (writingFlag.compareAndSet(false, true)) {
		}

		try {
			SocketChannel channel = (SocketChannel) currSelectKey.channel();
			int writeBytes = channel.write(writeBuffer);

			// 检查当前是否还存在需要写入的数据，如果需要，则进行写入
			if (writeBuffer.hasRemaining()) {
				System.out.println("写入成功:" + writeBytes + "字节");
				currSelectKey.interestOps(currSelectKey.interestOps() | SelectionKey.OP_WRITE);
			}
			// 写入完成，取消写事件，注册读取事件
			else {
				currSelectKey
						.interestOps((currSelectKey.interestOps() & ~SelectionKey.OP_WRITE) | SelectionKey.OP_READ);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writingFlag.set(false);
		}

	}

	@Override
	public void doconnect() {
		writeBuffer.clear();
		writeBuffer.put("welcome to kk telnet server demo;\r\n".getBytes());
		writeBuffer.flip();
		this.doWrite();
	}

}
