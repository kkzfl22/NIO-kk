package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 所有Iohandler操作
 * 
 * @since 2017年3月28日 下午2:45:41
 * @version 0.0.1
 * @author liujun
 */
public abstract class ChainMultIOHandler implements Runnable {

	/**
	 * 选择器信息
	 */
	protected final Selector select;

	/**
	 * socket通道信息
	 */
	protected final SelectionKey selectKey;

	/**
	 * 通道信息
	 */
	protected SocketChannel socketChannel;

	/**
	 * 分配的读取的readerbuffer信息
	 */
	protected ByteBuffer readerBuffer;

	/**
	 * 进行写入的buffer信息
	 */
	protected volatile ByteBuffer writeBuffer;

	public ChainMultIOHandler(Selector select, SocketChannel socket) throws IOException {
		super();
		long time2 = System.currentTimeMillis();
		this.select = select;
		this.socketChannel = socket;

		// 设置为非阻塞模式
		socket.configureBlocking(false);

		// 注册当前为reader事件感兴趣
		selectKey = socketChannel.register(select, SelectionKey.OP_READ);

		// 将当前对象信息附加到通道上
		selectKey.attach(this);

		long tim3 = System.currentTimeMillis();

		System.out.println("用时:" + (tim3 - time2));
	}

	@Override
	public void run() {
		try {
			if (selectKey.isValid()) {
				// 进行写入操作
				if (selectKey.isWritable()) {
					this.writeData();
				}
				// 进行读取数据处理操作
				else if (selectKey.isReadable()) {
					this.doHandler();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 发生错误，调用onerror方法
			this.onError(e);
			// 将当前通道关闭
			this.onClose();
		}
	}

	/**
	 * 连接处理
	 * 
	 * @throws IOException
	 */
	protected abstract void doConnection() throws IOException;

	/**
	 * 进行具体的事件的数据处理
	 * 
	 * @throws IOException
	 */
	protected abstract void doHandler() throws IOException;

	/**
	 * 进行数据写入
	 * 
	 * @throws IOException
	 */
	protected abstract void writeData() throws IOException;

	/**
	 * 进行具体的事件的错误处理
	 * 
	 * @throws IOException
	 */
	protected abstract void onError(Exception e);

	/**
	 * 进行具体的
	 * 
	 * @throws IOException
	 */
	protected abstract void onClose();

	public ByteBuffer getReaderBuffer() {
		return readerBuffer;
	}

	public void setReaderBuffer(ByteBuffer readerBuffer) {
		this.readerBuffer = readerBuffer;
	}

	public ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	public void setWriteBuffer(ByteBuffer writeBuffer) {
		this.writeBuffer = writeBuffer;
	}

}
