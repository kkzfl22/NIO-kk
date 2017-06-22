package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidStateEnum;
import com.kk.nio.demo.midd.util.ByteBufferTools;

/**
 * 在登录完成之后的查询版本信息处理操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class MultMidStateAuthSelVersion implements MultMidStateInf {

	@Override
	public void doRead(MultMidConnHandler multMidContext) throws Exception {

		if (multMidContext.getReadLock().compareAndSet(false, true)) {
			try {

				ByteBuffer byteBuffer = multMidContext.getMysqlConn().getWriteBuffer();

				int readPosition = multMidContext.getChannel().read(byteBuffer);

				if (readPosition > 0) {

					// 获取消息的长度，然后再获取消息的类型信息
					int length = ByteBufferTools.getLength(byteBuffer, 0);

					// 检查是否为登录报文，如果是则进行透传
					if (length == byteBuffer.position()) {

						System.out.println("中间件,响应登录成功之后的版本查询请求:readPosition:" + readPosition + ",length:" + length);
						// 设置写入的大小
						multMidContext.getMysqlConn().setWritePostion(byteBuffer.position());

						// 将当前的事件改变为写入响应，待后台有响应之后，立即处理
						multMidContext.getCurrSelkey()
								.interestOps(multMidContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_READ
										| SelectionKey.OP_WRITE);
						multMidContext.getSelect().wakeup();
					}

				}
			} finally {
				multMidContext.getReadLock().set(false);
			}
		}
	}

	@Override
	public void doWrite(MultMidConnHandler iostateContext) throws Exception {

		if (iostateContext.getWriteLock().compareAndSet(false, true)) {
			try {
				int readPos = iostateContext.getMysqlConn().getReadPostion();

				if (readPos > 0) {
					ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();

					int currPos = buffer.position();
					// 开始从头进行写入
					buffer.position(0);
					buffer.limit(currPos);

					int rsp = iostateContext.getChannel().write(buffer);

					if (rsp == readPos) {
						System.out.println("中间件,响应登录成功之后的版本查询结果:" + rsp);
						iostateContext.getMysqlConn().setReadPostion(0);
						buffer.clear();

						// 在收到版本查询结果之后，将进行一次心跳消息
						iostateContext
								.setCurrMidState(MultMidStateEnum.MULTMIDSTATE_AUTH_SELECT_USER.getMultMidState());

						// 取消写入事件，注册读取事件
						iostateContext.getCurrSelkey()
								.interestOps(iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE
										| SelectionKey.OP_READ);
						iostateContext.getSelect().wakeup();
					}
				}
			} finally {
				iostateContext.getWriteLock().set(false);
			}
		}
	}

}
