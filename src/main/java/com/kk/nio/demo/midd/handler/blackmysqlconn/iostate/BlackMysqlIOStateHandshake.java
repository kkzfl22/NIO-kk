package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.blackmysqlconn.MysqlIoStateEnum;

/**
 * 进行mysql的握手协议包的操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIOStateHandshake implements MysqlIoStateInf {

	@Override
	public void doRead(BlackMysqlIostateContext iostateContext) throws Exception {

		if (iostateContext.getMysqlConn().getReadLock().compareAndSet(false, true)) {
			try {
				// 首先进行数据读取
				int readPositon = iostateContext.getMysqlConn().getChannel()
						.read(iostateContext.getMysqlConn().getReadBuffer());

				if (readPositon > 0) {

					System.out.println("mysql-client端 ，收到大小:" + readPositon);

					iostateContext.getMysqlConn()
							.setReadPostion(iostateContext.getMysqlConn().getReadBuffer().position());

					// 当前获取消息成功，则将消息转为进行写入操作
					if (readPositon == iostateContext.getMysqlConn().getReadBuffer().position()) {

						// 需要将事件变为写入监听
						iostateContext.getMysqlConn().getCurrSelkey().interestOps(
								iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_READ
										| SelectionKey.OP_WRITE);

						iostateContext.getMysqlConn().getSelect().wakeup();

						System.out.println("mysql-client端，验证成功，改为写入事件!");

					}
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

					// 设置当前写入的位置为上一次写入结束的位置
					int writePosition = iostateContext.getMysqlConn().getChannel().write(buffer);

					if (writePost == buffer.position()) {

						System.out.println(
								"mysql-client端 ，写入大小:writePosition:" + writePosition + ",writePost:" + writePost);

						iostateContext.getMysqlConn()
								.setWritePostion(iostateContext.getMysqlConn().getWritePostion() + writePosition);

						// 检查当前是否已经写入完成,则切换状态为读取监听
						if (buffer.position() == writePosition) {
							iostateContext.getMysqlConn().setWritePostion(0);
							buffer.clear();

							// 当握手消息透传成功后，将当前的流程设置为透传的结果解析
							iostateContext.setCurrState(MysqlIoStateEnum.IOSTATE_AUTHRSP.getIoState());

							// 接下来的流程为进行用户登录结果的透传
							iostateContext.getMysqlConn().getCurrSelkey().interestOps(
									iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
											| SelectionKey.OP_READ);
							iostateContext.getMysqlConn().getSelect().wakeup();
						}
					}
				}
			} finally {
				iostateContext.getMysqlConn().getWriteLock().set(false);
			}
		}

	}

}
