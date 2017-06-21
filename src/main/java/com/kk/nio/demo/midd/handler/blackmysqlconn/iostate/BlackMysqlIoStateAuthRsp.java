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
	public boolean doRead(BlackMysqlIostateContext iostateContext) throws Exception {

		ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();

		// 首先进行数据读取
		int authRsp = iostateContext.getMysqlConn().getChannel().read(buffer);

		if (authRsp > 0) {
			System.out.println("收到鉴权结果:" + authRsp);
			
			int cutPos = buffer.position();
			buffer.position(0);
			buffer.limit(cutPos);
			
			byte[] valuByte = new byte[cutPos];
			buffer.get(valuByte);
			
			System.out.println("收到鉴权结果信息:" + new String(valuByte));

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
		}

		return false;

	}

	@Override
	public boolean doWrite(BlackMysqlIostateContext iostateContext) throws Exception {

		ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();
		int writePos = iostateContext.getMysqlConn().getChannel().write(buffer);

		if (writePos > 0) {

			// 检查当前是否已经写入完成,则切换状态为读取监听
			if (buffer.hasRemaining()) {

				// 进行压缩
				buffer.compact();
				return false;

			} else {
				// 将状态变为结果读取
				iostateContext.setCurrState(MysqlIoStateEnum.IOSTATE_AUTHRSP.getIoState());

				// 注册结果读取事件
				iostateContext.getMysqlConn().getCurrSelkey().interestOps(
						iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
								| SelectionKey.OP_READ);

				return true;
			}
		}

		return false;
	}

}
