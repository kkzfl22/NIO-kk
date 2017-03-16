package com.kk.nio.socket.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.util.CmdUtils;

/**
 * 进行数据处理的接口
 * 
 * @since 2017年3月12日 下午6:42:41
 * @version 0.0.1
 * @author kk
 */
public class IOHandler implements Runnable {

	/**
	 * 通道信息
	 */
	private final SocketChannel channel;

	/**
	 * 注册的选择的键集信息
	 */
	private SelectionKey selectKey;

	/**
	 * 设置读取的buffer
	 */
	private ByteBuffer readBuffer;

	/**
	 * 设置写入的buffer
	 */
	private ByteBuffer writeBuffer;

	/**
	 * 上一次操作的position
	 */
	private int lastModPositon = 0;

	public IOHandler(SocketChannel channel, Selector select) throws IOException {
		// 设置非阻塞模式
		channel.configureBlocking(false);
		this.channel = channel;
		// 进行注册
		selectKey = channel.register(select, 0);
		// 设置对具体的监听
		selectKey.interestOps(SelectionKey.OP_READ);

		// 设置读写缓冲区
		readBuffer = ByteBuffer.allocateDirect(1024);
		writeBuffer = ByteBuffer.allocateDirect(10240);

		// 将当前对象绑定到会话
		selectKey.attach(this);

		System.out.println("attach object success!");

		// 首先从服务器向客户端写入一段话，提示欢迎信息
		writeBuffer.put("welcome to kk reactor nio!\r\n".getBytes());
		writeBuffer.flip();
		this.doWrite();
	}

	@Override
	public void run() {
		try {
			// 如果当前已经读取到，则进行读取操作
			if (selectKey.isReadable()) {
				doRead();
			} else if (selectKey.isWritable()) {
				doWrite();
			}
		} catch (IOException e) {
			e.printStackTrace();
			selectKey.cancel();
			try {
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void doRead() throws IOException {
		// 1,首先从将数据加载到bytebuffer中
		channel.read(readBuffer);

		// 取得当前的位置
		int readOpts = readBuffer.position();

		String line = null;

		// 2,将数据按行进行分隔,得到一行记录
		for (int i = lastModPositon; i < readOpts; i++) {
			// 找到换行符
			if (readBuffer.get(i) == 13) {
				byte[] byteValue = new byte[i - lastModPositon];
				// 标识位置，然后开始读取
				readBuffer.position(lastModPositon);
				readBuffer.get(byteValue);

				lastModPositon = i;

				line = new String(byteValue);
				System.out.println("收到msg :" + line);
				break;
			}
		}

		// 3,检查是否需要执行命令,将数据写入返回
		if (null != line && !"".equals(line)) {
			// 取消事件注册
			selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_READ);
			// 执行命令
			String msg = CmdUtils.runCmd(line);
			writeBuffer.put(msg.getBytes("GBK"));
			writeBuffer.flip();
			doWrite();
		}

		// 4,当容量超过总容量的2分之一大小则需要压缩
		if (readBuffer.position() > readBuffer.capacity() / 2) {
			// 重新标识position,将当前的容量标识为buffer中已有数据的容量
			readBuffer.limit(readBuffer.position());
			//标识当前pos为上一次已经读取到的数据的pos
			readBuffer.position(lastModPositon);

			// 压缩数据,即将上一次读取之前的记录丢弃
			readBuffer.compact();

			lastModPositon = 0;
		}
	}

	public void doWrite() throws IOException {
		int rds = channel.write(writeBuffer);

		// 如果需要继续写入，则继续注册事件，否则，仅关注读取事件
		if (writeBuffer.hasRemaining()) {
			System.out.println("write :" + rds + ";");
			selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_WRITE);
		} else {
			System.out.println("write :" + rds + ";");
			writeBuffer.clear();
			selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		}
	}

}
