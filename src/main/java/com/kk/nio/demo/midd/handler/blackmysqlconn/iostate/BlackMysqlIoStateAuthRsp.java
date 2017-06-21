package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

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

			// 首先读取鉴权结果
			int length = ByteBufferTools.getLength(buffer, 0);

			// 读取消息的类型
			byte rsp = buffer.get(4);

			System.out.println("认证结果:包长度（" + length + "),类型:" + rsp);

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

		return false;
	}

}
