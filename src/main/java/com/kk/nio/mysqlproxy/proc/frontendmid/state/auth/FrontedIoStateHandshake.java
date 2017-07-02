package com.kk.nio.mysqlproxy.proc.frontendmid.state.auth;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendIOHandStateInf;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidStateEnum;

/**
 * 进行前端握手包的流程处理操作
 * 
 * @since 2017年6月23日 下午4:05:04
 * @version 0.0.1
 * @author liujun
 */
public class FrontedIoStateHandshake implements FrontendIOHandStateInf {

	@Override
	public void doRead(FrontendMidConnnectHandler handler) throws IOException {
		ByteBuffer writeBuffer = handler.getBackMysqlConn().getWriteBuffer();

		int readPos = handler.getChannel().read(writeBuffer);

		if (readPos > 0) {
			handler.getBackMysqlConn().setWritePosition(readPos);
			// 取消当前的读取事件
			handler.eventRigCancelRead();
			// 注册后端的写入事件
			handler.getBackMysqlConn().eventRigOpenWrite();

			// 当前的状态完成，需要切换为结果检查
			handler.setCurrState(FrontendMidStateEnum.FRONTENDSTATE_AUTHRSP.getMysqlConnState());
		}
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
				// 当前写入成功，则需要切换到读取状态，以读取当前上传的鉴权信息
				handler.eventRigCancelWriteOpenRead();
			}
		}
	}

}