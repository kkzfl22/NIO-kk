package com.kk.nio.demo.midd.handler.multmidconn.state;

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

		int readPosition = iostateContext.getChannel().read(iostateContext.getMysqlConn().getReadBuffer());

		if (readPosition > 0) {
			// 设置流程为进行结果集的透传
		}
	}

	@Override
	public void doWrite(MultMidConnHandler iostateContext) throws Exception {}

}
