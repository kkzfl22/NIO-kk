package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.blackmysqlconn.MysqlIoStateEnum;
import com.kk.nio.demo.midd.util.ByteBufferTools;

/**
 * 进行mysql的登录的结果信息
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIoStateAuthRsp implements MysqlIoStateInf {

	@Override
	public void doRead(BlackMysqlIostateContext iostateContext) throws Exception {

		if (iostateContext.getMysqlConn().getReadLock().compareAndSet(false, true)) {
			try {
				ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();

				// 首先进行数据读取
				int authRsp = iostateContext.getMysqlConn().getChannel().read(buffer);

				if (authRsp == buffer.position()) {

					// 首先读取鉴权结果
					int length = ByteBufferTools.getLength(buffer, 0);

					System.out.println("mysql-client端收到鉴权结果:authRsp:" + authRsp + ",length:" + length
							+ ",当前buffer.position():" + buffer.position());

					// 设置读取到的大小为
					iostateContext.getMysqlConn().setReadPostion(buffer.position());

					// 需要将事件变为写入监听
					iostateContext.getMysqlConn().getCurrSelkey().interestOps(
							iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_READ
									| SelectionKey.OP_WRITE);

					// 当前登录鉴权结果获取,进行登录之后的版本号查询
					iostateContext.setCurrState(MysqlIoStateEnum.IOSTATE_SELVERSION.getIoState());

					iostateContext.getMysqlConn().getSelect().wakeup();

				}
			} finally {
				//将锁释放
				iostateContext.getMysqlConn().getReadLock().set(false);
			}

		}

	}

	@Override
	public void doWrite(BlackMysqlIostateContext iostateContext) throws Exception {
	}

}
