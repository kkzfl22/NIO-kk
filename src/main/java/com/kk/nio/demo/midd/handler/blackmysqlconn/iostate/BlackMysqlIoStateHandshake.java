package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

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
		int readPositon = iostateContext.getChannel().read(iostateContext.getReadBuffer());

		iostateContext.setReadPostion(iostateContext.getReadPostion() + readPositon);

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

		// 设置当前写入的位置为上一次写入结束的位置
		iostateContext.getWriteBuffer().position(iostateContext.getWritePostion());

		int writePosition = iostateContext.getChannel().write(iostateContext.getWriteBuffer());

		iostateContext.setWritePostion(iostateContext.getWritePostion() + writePosition);

		// 检查当前是否已经写入完成,则切换状态为读取监听
		if (iostateContext.getWriteBuffer().hasRemaining()) {
			// 进行压缩
			iostateContext.getWriteBuffer().compact();
			return false;

		} else {
			iostateContext.getCurrSelkey().interestOps(
					iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			return true;
		}
	}

}
