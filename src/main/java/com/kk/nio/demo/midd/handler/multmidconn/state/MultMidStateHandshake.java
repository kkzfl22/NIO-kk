package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidStateEnum;
import com.kk.nio.demo.midd.util.ByteBufferTools;

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

		if (readPosition > 0) {
			// 进行检查前台发送的认证请求
			// 获取消息的长度，然后再获取消息的类型信息
			int length = ByteBufferTools.getLength(byteBuffer, 0);
			// 检查是否为登录报文，如果是则进行透传
			if (length == byteBuffer.position()) {

				System.out.println("中间件，收到登录认证:" + length);

				multMidContext.getCurrSelkey().interestOps(
						multMidContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				multMidContext.getSelect().wakeup();
				// 设置流程为进行结果集的透传
				multMidContext.setCurrMidState(MultMidStateEnum.MULTMIDSTATE_AUTHRSP.getMultMidState());
			}
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

			// 写入前端的请求
			int writePos = multMidContext.getChannel().write(byteBuffer);


			// 检查当前是否已经写入完成,如果未写入完成，则继续写入，写入完成了，则切换为读
			if (writePos > 0) {
				multMidContext.setWritePostion(multMidContext.getWritePostion() + writePos);
				
				System.out.println("中间件完成写入:" + writePos);
				// 握手包透传完成，则进行清理容器，并指定读取前端发送的登录认证信息
				byteBuffer.clear();
				multMidContext.setWritePostion(0);

				multMidContext.getCurrSelkey().interestOps(
						multMidContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
				multMidContext.getSelect().wakeup();
			}
		}
	}

}
