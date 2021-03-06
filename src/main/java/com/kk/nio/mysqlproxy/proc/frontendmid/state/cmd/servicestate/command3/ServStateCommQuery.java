package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServStateRspEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceContext;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;

/**
 * 
 * @since 2017年7月2日 上午11:21:01
 * @version 0.0.1
 * @author liujun
 */
public class ServStateCommQuery implements MysqlServiceStateInf {

	@Override
	public void serviceDoInvoke(MysqlServiceContext mysqlService) throws IOException {

		FrontendMidConnnectHandler handler = mysqlService.getFrontedConn();

		ByteBuffer writeBuffer = handler.getBackMysqlConn().getWriteBuffer();

		handler.getBackMysqlConn().setWritePosition(writeBuffer.position());

		// 首先检查当前数据的最后4个字节是否为000,如果发现此，证明load data的透传已经结束
		if(checkDataTransOver(writeBuffer))
		{
			
		}

		// 取消当前的读取事件
		handler.eventRigCancelRead();
		// 注册后端的写入事件
		handler.getBackMysqlConn().eventRigOpenWrite();
	}

	private boolean checkDataTransOver(ByteBuffer writeBuffer) {
		for (int i = writeBuffer.position() - 1; i > writeBuffer.position() - 4; i--) {
			if(writeBuffer.get(i) != 0)
			{
				return false;
			}
		}
		return true;
	}

}
