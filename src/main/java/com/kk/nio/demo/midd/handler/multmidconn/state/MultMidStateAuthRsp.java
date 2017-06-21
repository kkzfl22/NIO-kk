package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;

/**
 * 进行mysql的握手协议包的操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class MultMidStateAuthRsp implements MultMidStateInf {

	@Override
	public void doRead(MultMidConnHandler iostateContext) throws Exception {

	}

	@Override
	public void doWrite(MultMidConnHandler iostateContext) throws Exception {
		ByteBuffer buffer = iostateContext.getMysqlConn().getReadBuffer();

		if (buffer.position() > 0) {

			int currPos = buffer.position();
			// 开始从头进行写入
			buffer.position(0);
			buffer.limit(currPos);

			int rsp = iostateContext.getChannel().write(buffer);

			if (rsp > 0 ) {
				System.out.println("中间件,响应登录认证结果:" + rsp);
				
				buffer.clear();

				iostateContext.getCurrSelkey()
						.interestOps(iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE);
				iostateContext.getSelect().wakeup();
				
			}
		}
	}

}
