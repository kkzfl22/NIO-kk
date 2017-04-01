package com.kk.nio.socket.multreactor.procchain.chain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息接口
 * 
 * @since 2017年3月29日 上午12:28:16
 * @version 0.0.1
 * @author kk
 */
public class ReactorMsgHandlerBase implements MsgBaseInf {

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
	 * 进行数据的写入
	 * 
	 * @param data
	 *            写的数据
	 * @param context
	 *            上下文对象信息
	 * @throws IOException
	 *             异常信息
	 */
	public void writeData(Context context) throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			// 将数据加入到待发送的队列中
			this.writeQueue.add(context.getWriteBuffer());

			ByteBuffer theWriteBuffer = writeBuffer;

			if (theWriteBuffer == null) {
				theWriteBuffer = this.writeQueue.remove(0);
			}

			// 继续发送当前待发送的数据
			this.doWriteToChannel(theWriteBuffer, context);
		} finally {
			// release
			writeFlag.lazySet(true);
		}
	}

	/**
	 * 将数据写入到通道中
	 * 
	 * @param writeBufferinfo
	 *            数据的bytebuffer信息
	 * @param context
	 *            上下文对象信息
	 * @throws IOException
	 *             异常
	 */
	private void doWriteToChannel(ByteBuffer writeBufferinfo, Context context) throws IOException {
		writeBufferinfo.flip();
		int writeLength = context.getSocketChannel().write(writeBufferinfo);
		System.out.println("curr write length :" + writeLength);

		// 如果当前还有未发送的完成的数据,则继续保持对当前写事件的兴趣
		if (writeBufferinfo.hasRemaining()) {
			context.getSelectKey().interestOps(context.getSelectKey().interestOps() | SelectionKey.OP_WRITE);
			// 检查当前的buffer是否与发送的buffer是同一个
			if (writeBufferinfo != writeBuffer) {
				this.writeBuffer = writeBufferinfo;
			}
		}
		// 写入完成，则取消写事件
		else {
			System.out.println("data write finish!");
			writeBufferinfo.clear();
			// 检查当前队列是否已经完成,完成取取消写事件，注册读取事件
			if (this.writeQueue.isEmpty()) {
				System.out.println("wite over ,queue is null,cancel wirte event!");
				context.getSelectKey().interestOps(
						context.getSelectKey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			}
			// 如果未完成，则遍历队列进行写入
			else {
				ByteBuffer buffer = writeQueue.removeFirst();
				// 进行递归的数据写入
				this.doWriteToChannel(buffer, context);
			}
		}
	}

	@Override
	public ByteBuffer readData(Context context) throws IOException {
		if (null != context.getReadBuffer()) {
			// 进行数据读取操作
			context.getSocketChannel().read(context.getReadBuffer());
		}

		return context.getReadBuffer();
	}

}
