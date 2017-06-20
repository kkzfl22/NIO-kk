package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

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

		// 首先进行数据读取
		iostateContext.getChannel().read(iostateContext.getReadBuffer());

		// 进行检查
		boolean bufferCheck = ByteBufferTools.checkLength(iostateContext.getReadBuffer());

		if (bufferCheck) {
			// 进行将状态切换为登录鉴权
			// 需要将事件变为写入监听
			iostateContext.getCurrSelkey().interestOps(
					iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			return true;
		}

		return false;

	}

	@Override
	public boolean doWrite(BlackMysqlIostateContext iostateContext) throws Exception {

		iostateContext.getChannel().write(iostateContext.getWriteBuffer());

		// 检查当前是否已经写入完成,则切换状态为读取监听
		if (iostateContext.getWriteBuffer().hasRemaining()) {

			// 进行压缩
			iostateContext.getWriteBuffer().compact();
			return false;

		} else {
			// 将状态变为结果读取
			iostateContext.setCurrState(MysqlIoStateEnum.IOSTATE_AUTHRSP.getIoState());

			// 注册结果读取事件
			iostateContext.getCurrSelkey().interestOps(
					iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);

			return true;
		}
	}

}
