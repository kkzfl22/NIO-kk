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

		// 获取消息的长度，然后再获取消息的类型信息
		int length = ByteBufferTools.getLength(iostateContext.getMysqlConn().getReadBuffer(), 0);

		// 获取消息的类型
		int msgType = iostateContext.getMysqlConn().getReadBuffer().get(4);

		// 当前获取消息成功，则将消息转为进行写入操作
		if (msgType == 10 && length == iostateContext.getMysqlConn().getReadBuffer().position()) {
			
			
			System.out.println("后——》前 ，收到大小:"+length);
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

				System.out.println("前——》后 ，写入大小:"+writePosition);

				// 检查当前是否已经写入完成,则切换状态为读取监听
				if (buffer.position() == writePosition) {
					
					buffer.clear();
					iostateContext.getMysqlConn().setWritePostion(0);

					iostateContext.getMysqlConn().getCurrSelkey().interestOps(
							iostateContext.getMysqlConn().getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
									| SelectionKey.OP_READ);
					iostateContext.getMysqlConn().getSelect().wakeup();

					return true;
				}
			}
		}

		return false;
	}

}
