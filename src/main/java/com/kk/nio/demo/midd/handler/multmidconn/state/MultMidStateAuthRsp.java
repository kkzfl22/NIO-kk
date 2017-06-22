package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;
import com.kk.nio.demo.midd.handler.multmidconn.MultMidStateEnum;

/**
 * 进行mysql的握手协议包的结果解析，然后执行select @@version_comment limit 1
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class MultMidStateAuthRsp implements MultMidStateInf {

	@Override
	public void doRead(MultMidConnHandler multMidContext) throws Exception {
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
						System.out.println("中间件,响应登录认证结果:" + rsp);

						iostateContext.getMysqlConn().setReadPostion(0);
						buffer.clear();

						// 需要将当前的事件改变为查询版本的请求接收
						iostateContext
								.setCurrMidState(MultMidStateEnum.MULTMIDSTATE_AUTH_SELECT_VERSION.getMultMidState());

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
