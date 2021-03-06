package com.kk.nio.demo.client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class MysqlClientIoHandler extends MysqlClientIOHandlerBase {

	/**
	 * 进行事件的读取操作
	 */
	public void doRead() {
		try {
			SocketChannel channel = (SocketChannel) currSelectKey.channel();
			int readByte = channel.read(readBuffer);

			if (readByte > 0) {

				int size = readBuffer.position();
				// 进行从后端读取数据
				byte[] array = new byte[size];
				readBuffer.position(0);
				readBuffer.get(array);
				System.out.println(Arrays.toString(array));
				System.out.println(new String(array));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doWrite() {

	}

}
