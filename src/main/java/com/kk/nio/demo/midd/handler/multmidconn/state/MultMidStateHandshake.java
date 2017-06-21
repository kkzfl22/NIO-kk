package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidStateEnum;

/**
 * 进行mysql的握手协议包的操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class MultMidStateHandshake implements MultMidStateInf {

	@Override
	public void doRead(MultMidConnHandler multMidContext) throws Exception {

		ByteBuffer byteBuffer = multMidContext.getMysqlConn().getWriteBuffer();

		int readPosition = multMidContext.getChannel().read(byteBuffer);

		if (readPosition > 5) {

			// 设置流程为进行结果集的透传
			multMidContext.setCurrMidState(MultMidStateEnum.MULTMIDSTATE_AUTHRSP.getMultMidState());
			multMidContext.getCurrSelkey().interestOps(
					multMidContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			multMidContext.getSelect().wakeup();
			System.out.println("mult前台发送的用户名与密码:" + readPosition);
		}
	}

	@Override
	public void doWrite(MultMidConnHandler multMidContext) throws Exception {

		// 1,读取到后端的读取流
		ByteBuffer byteBuffer = multMidContext.getMysqlConn().getReadBuffer();

		if (byteBuffer.position() > 0) {

			int currPosition = byteBuffer.position();
			// 指定开始写入的位置
			byteBuffer.position(multMidContext.getWritePostion());
			byteBuffer.limit(currPosition);

			// 重新设置位置
			int writePos = multMidContext.getChannel().write(byteBuffer);

			multMidContext.setWritePostion(multMidContext.getWritePostion() + writePos);

			// 检查当前是否已经写入完成,如果未写入完成，则继续写入，写入完成了，则切换为读
			if (multMidContext.getMysqlConn().getReadBuffer().hasRemaining()) {
				multMidContext.getCurrSelkey()
						.interestOps(multMidContext.getCurrSelkey().interestOps() & SelectionKey.OP_WRITE);
			} else {
				// 完成一次流程操作，进行一次容器的压缩
				byteBuffer.compact();
				multMidContext.setWritePostion(byteBuffer.position());

				multMidContext.getCurrSelkey().interestOps(
						multMidContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
				multMidContext.getSelect().wakeup();
			}
		}
	}

}
