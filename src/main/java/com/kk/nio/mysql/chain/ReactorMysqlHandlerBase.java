package com.kk.nio.mysql.chain;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息接口
 * 
 * @since 2017年3月29日 上午12:28:16
 * @version 0.0.1
 * @author kk
 */
public class ReactorMysqlHandlerBase implements MsgBaseInf {

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
	public void writeData(MysqlContext context) throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			// 继续发送当前待发送的数据
			context.getWriteBuffer().flip();
			int writeLength = context.getSocketChannel().write(context.getWriteBuffer());
			System.out.println("curr write length :" + writeLength);

			// 如果当前还有未发送的完成的数据,则继续保持对当前写事件的兴趣
			if (context.getWriteBuffer().hasRemaining()) {
				context.getSelectKey().interestOps(context.getSelectKey().interestOps() | SelectionKey.OP_WRITE);
			}
			// 写入完成，则取消写事件
			else {
				System.out.println("data write finish!");
				context.getWriteBuffer().clear();
				// 检查当前队列是否已经完成,完成取取消写事件，注册读取事件
				context.getSelectKey().interestOps(
						context.getSelectKey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			}

		} finally {
			// release
			writeFlag.lazySet(true);
		}
	}

	@Override
	public void readData(MysqlContext context) throws IOException {

		// 进行数据读取操作
		context.getSocketChannel().read(context.getReadBuffer());

	}

}
