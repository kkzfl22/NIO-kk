package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServStateRspEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceContext;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;
import com.kk.nio.mysqlproxy.util.BufferTools;

/**
 * 进行行数据的检查跳过
 * 
 * @since 2017年7月2日 上午11:21:01
 * @version 0.0.1
 * @author liujun
 */
public class ServStateCommQueryRspData implements MysqlServiceStateInf {

	@Override
	public void serviceDoInvoke(MysqlServiceContext mysqlService) throws IOException {
		FrontendMidConnnectHandler handler = mysqlService.getFrontedConn();

		ByteBuffer readBuffer = handler.getBackMysqlConn().getReadBuffer();

		// 进行eof包大小的解析
		int length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());

		int currPosition = readBuffer.position();
		// 检查包长度，然后进行设置已经读取到的列信息
		if (currPosition + length < readBuffer.limit()) {
			mysqlService.setReadPosition(mysqlService.getReadPosition() + length);
			// 设置接下来的状态为数据项的检查
			mysqlService.setCurrState(ServStateRspEnum.SERV_STATE_RSP_DATA_OVER.getStateProc());

		}

	}

}
