package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.util.ByteBufferTools;

/**
 * 进行mysql的握手协议包的操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIoStateHandshake implements MysqlIoStateInf {

	@Override
	public boolean doRead(BlackMysqlIostateContext iostateContext) throws Exception {

		// 首先进行数据读取
		int readPositon = iostateContext.getMysqlConn().getChannel()
				.read(iostateContext.getMysqlConn().getReadBuffer());

		if (readPositon != -1) {
			iostateContext.getMysqlConn().setReadPostion(iostateContext.getMysqlConn().getReadPostion() + readPositon);
		}

		// 进行检查
		boolean bufferCheck = ByteBufferTools.checkLength(iostateContext.getMysqlConn().getReadBuffer(), 0);

		if (bufferCheck) {
			// 进行将状态切换为登录鉴权
			// 需要将事件变为写入监听
			iostateContext.getMysqlConn().getCurrSelkey()
					.interestOps(iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_READ
							| SelectionKey.OP_WRITE);
			iostateContext.getMysqlConn().getSelect().wakeup();
			return true;
		}

		return false;

	}

	@Override
	public boolean doWrite(BlackMysqlIostateContext iostateContext) throws Exception {

		ByteBuffer buffer = iostateContext.getMysqlConn().getWriteBuffer();

		if (buffer.position() > 0) {

			int curPos = buffer.position();

			buffer.position(iostateContext.getMysqlConn().getWritePostion());
			buffer.limit(curPos);

			// 设置当前写入的位置为上一次写入结束的位置
			int writePosition = iostateContext.getMysqlConn().getChannel().write(buffer);

			if (writePosition > 0) {

				iostateContext.getMysqlConn()
						.setWritePostion(iostateContext.getMysqlConn().getWritePostion() + writePosition);

				System.out.println("mysql端传送用户名和密码：" + writePosition);

				// 检查当前是否已经写入完成,则切换状态为读取监听
				if (buffer.hasRemaining()) {
					// 进行压缩
					buffer.compact();

					iostateContext.getMysqlConn()
							.setWritePostion(iostateContext.getMysqlConn().getWritePostion() + writePosition);

					return false;

				} else {
					iostateContext.getMysqlConn().getCurrSelkey().interestOps(
							iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
									| SelectionKey.OP_READ);
					iostateContext.getMysqlConn().getSelect().wakeup();
					buffer.clear();

					iostateContext.getMysqlConn().setWritePostion(0);

					return true;
				}
			}
		}

		return false;
	}

}
