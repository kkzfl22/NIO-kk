package com.kk.nio.mysqlproxy.proc.frontendmid.state.auth;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendIOHandStateInf;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidStateEnum;

/**
 * 进行鉴权的结果透传
 * 
 * @since 2017年6月23日 下午4:05:04
 * @version 0.0.1
 * @author liujun
 */
public class FrontedIoStateAuthRsp implements FrontendIOHandStateInf {

	@Override
	public void doRead(FrontendMidConnnectHandler handler) throws IOException {
	}

	@Override
	public void doWrite(FrontendMidConnnectHandler handler) throws IOException {
		// 首先检查确的写入标识
		if (handler.getBackMysqlConn().getReadPostion() > 0) {
			ByteBuffer writeBuffer = handler.getBackMysqlConn().getReadBuffer();

			int pos = writeBuffer.position();
			writeBuffer.position(0);
			writeBuffer.limit(pos);

			int writeSize = handler.getChannel().write(writeBuffer);

			if (writeSize > 0) {
				writeBuffer.clear();
				handler.getBackMysqlConn().setReadPostion(0);
				// 当前写入成功，
				// 切换到查询查询版本的状态中去
				handler.eventRigCancelWriteOpenRead();
				// 将当前状态切换为查询版本的请求读取
				handler.setCurrState(FrontendMidStateEnum.FRONTENDSTATE_SELVERSION.getMysqlConnState());
			}
		}
	}

}
