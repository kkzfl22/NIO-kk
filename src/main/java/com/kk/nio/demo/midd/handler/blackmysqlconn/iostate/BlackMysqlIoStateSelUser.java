package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.util.ByteBufferTools;

/**
 * 进行mysql的登录的一次心跳消息
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIoStateSelUser implements MysqlIoStateInf {

	@Override
	public void doRead(BlackMysqlIostateContext iostateContext) throws Exception {

		if (iostateContext.getMysqlConn().getReadLock().compareAndSet(false, true)) {

			try {

				ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();

				// 进行通道中数据的读取
				int writeLength = iostateContext.getMysqlConn().getChannel().read(buffer);

				if (writeLength == buffer.position()) {

					// 首先读取鉴权结果
					int length = ByteBufferTools.getLength(buffer, 0);

					System.out.println("mysql-client端，收到写入select user查询的响应,:authRsp:" + writeLength + ",length:"
							+ length + ",当前buffer.position():" + buffer.position());

					// 需要将事件变为写入监听
					iostateContext.getMysqlConn().getCurrSelkey().interestOps(
							iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_READ
									| SelectionKey.OP_WRITE);

					iostateContext.getMysqlConn().getSelect().wakeup();

					// 设置读取到的大小为
					iostateContext.getMysqlConn().setReadPostion(buffer.position());

				}

			} finally {
				iostateContext.getMysqlConn().getReadLock().set(false);
			}
		}

	}

	@Override
	public void doWrite(BlackMysqlIostateContext iostateContext) throws Exception {

		if (iostateContext.getMysqlConn().getWriteLock().compareAndSet(false, true)) {

			try {

				// 检查当前需要写入的大小是否为0
				int writePost = iostateContext.getMysqlConn().getWritePostion();

				if (writePost > 0) {

					ByteBuffer buffer = iostateContext.getMysqlConn().getWriteBuffer();

					int curPos = buffer.position();

					buffer.position(0);
					buffer.limit(curPos);

					// 进行写入
					int writePosition = iostateContext.getMysqlConn().getChannel().write(buffer);

					if (buffer.position() == writePosition) {

						System.out.println("mysql-client端，收到写入select user查询的请求,writePost:" + writePost + ",curPos:"
								+ curPos + ",buffer:" + buffer);

						// 检查当前是否已经写入完成,则切换状态为读取监听
						iostateContext.getMysqlConn().setWritePostion(0);
						buffer.clear();

						// 当前状态不切换，将进行设置结果的透传
						iostateContext.getMysqlConn().getCurrSelkey().interestOps(
								iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
										| SelectionKey.OP_READ);
						iostateContext.getMysqlConn().getSelect().wakeup();
					}
				}
			} finally {
				iostateContext.getMysqlConn().getWriteLock().set(false);
			}

		}
	}

}
