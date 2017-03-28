package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class ChainTelnetIOHandler extends ChainMultIOHandler {

	/**
	 * 换行符
	 */
	private static final String LINE = "\r\n";

	/**
	 * 写的入队列信息
	 */
	private volatile LinkedList<ByteBuffer> writeQueue = new LinkedList<>();

	/**
	 * 写入标识
	 */
	private AtomicBoolean writeFlag = new AtomicBoolean(false);

	public ChainTelnetIOHandler(Selector select, SocketChannel socket) throws IOException {

		super(select, socket);

		// 进行数据的首次写入
		this.doConnection();

	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !").append(LINE);
		msg.append("1,input command ").append(LINE);
		msg.append("2,exit ").append(LINE);

		this.writeData(msg.toString().getBytes());
	}

	@Override
	protected void doHandler() throws IOException {
		System.out.println("当前读取操作");
	}

	@Override
	protected void onError() {
		System.out.println("curr handler process error");
	}

	@Override
	protected void onClose() {
		this.selectKey.cancel();
		try {
			this.selectKey.channel().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	/**
	 * 进行数据写入
	 * 
	 * @throws IOException
	 */
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

	/**
	 * 将数据写入通道
	 * 
	 * @param writeBufferinfo
	 * @throws IOException
	 */
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
