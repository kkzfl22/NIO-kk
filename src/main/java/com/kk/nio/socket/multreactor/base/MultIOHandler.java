package com.kk.nio.socket.multreactor.base;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 所有Iohandler操作
 * 
 * @since 2017年3月28日 下午2:45:41
 * @version 0.0.1
 * @author liujun
 */
public abstract class MultIOHandler implements Runnable {

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
	 * 进行写入的buffer信息
	 */
	private volatile ByteBuffer writeBuffer;

	/**
	 * 写的入队列信息
	 */
	private volatile LinkedList<ByteBuffer> writeQueue = new LinkedList<>();

	/**
	 * 写入标识
	 */
	private AtomicBoolean writeFlag = new AtomicBoolean(false);

	/**
	 * 分配的读取的readerbuffer信息
	 */
	protected ByteBuffer readerBuffer;

	public MultIOHandler(Selector select, SocketChannel socket) throws IOException {
		super();
		this.select = select;
		this.socketChannel = socket;

		// 设置为非阻塞模式
		socket.configureBlocking(false);

		// 注册当前为reader事件感兴趣
		selectKey = socketChannel.register(select, SelectionKey.OP_READ);

		readerBuffer = ByteBuffer.allocateDirect(1024);

		// 将当前对象信息附加到通道上
		selectKey.attach(this);

		// 进行数据的首次写入
		this.doConnection();
	}

	@Override
	public void run() {
		try {
			// 进行写入操作
			if (selectKey.isWritable()) {
				this.writeData();
			}
			// 进行读取数据处理操作
			else if (selectKey.isReadable()) {
				this.doHandler();
			}
		} catch (IOException e) {
			e.printStackTrace();
			// 发生错误，调用onerror方法
			this.onError();
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
	 * 进行具体的事件的错误处理
	 * 
	 * @throws IOException
	 */
	protected abstract void onError();

	/**
	 * 进行具体的
	 * 
	 * @throws IOException
	 */
	protected abstract void onClose();

	/**
	 * 进行数据的写入
	 * 
	 * @param data
	 *            写的数据
	 * @throws IOException
	 *             异常信息
	 */
	public void writeData(byte[] data) throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			ByteBuffer theWriteBuffer = writeBuffer;

			// 所有待发送的数据都为空，则直接发送
			if (theWriteBuffer == null && writeQueue.isEmpty()) {
				this.doWriteToChannel(ByteBuffer.wrap(data));
			}
			// 否则将数据加入到待发送的队列中
			else {
				this.writeQueue.add(ByteBuffer.wrap(data));
				// 继续发送当前待发送的数据
				this.doWriteToChannel(theWriteBuffer);
			}
		} finally {
			// release
			writeFlag.lazySet(true);
		}
	}

	public void writeData() throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			// 直接写入writebuffer信息
			ByteBuffer theWriteBuffer = writeBuffer;
			this.doWriteToChannel(theWriteBuffer);
		} finally {
			this.writeFlag.lazySet(true);
		}
	}

	private void doWriteToChannel(ByteBuffer writeBufferinfo) throws IOException {
		int writeLength = socketChannel.write(writeBufferinfo);
		System.out.println("curr write length :" + writeLength);

		// 如果当前还有未发送的完成的数据,则继续保持对当前写事件的兴趣
		if (writeBufferinfo.hasRemaining()) {
			selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_WRITE);
			// 检查当前的buffer是否与发送的buffer是同一个
			if (writeBufferinfo != writeBuffer) {
				this.writeBuffer = writeBufferinfo;
			}
		}
		// 写入完成，则取消写事件
		else {
			System.out.println("data write finish!");
			// 检查当前队列是否已经完成,完成取取消写事件，注册读取事件
			if (this.writeQueue.isEmpty()) {
				System.out.println("wite over ,queue is null,cancel wirte event!");
				selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			}
			// 如果未完成，则遍历队列进行写入
			else {
				ByteBuffer buffer = writeQueue.removeFirst();
				buffer.flip();
				// 进行递归的数据写入
				this.doWriteToChannel(buffer);
			}

		}
	}

}
