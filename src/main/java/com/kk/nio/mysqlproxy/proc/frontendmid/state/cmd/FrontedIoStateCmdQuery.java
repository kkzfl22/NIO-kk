package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendIOHandStateInf;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServStateReqEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceContext;

/**
 * 进行SQL查询的操作
 * 
 * @since 2017年6月23日 下午4:05:04
 * @version 0.0.1
 * @author liujun
 */
public class FrontedIoStateCmdQuery implements FrontendIOHandStateInf {

	/**
	 * 进行sql命令处理的状态机
	 */
	private MysqlServiceContext context = new MysqlServiceContext();

	@Override
	public void doRead(FrontendMidConnnectHandler handler) throws IOException {
		ByteBuffer writeBuffer = handler.getBackMysqlConn().getWriteBuffer();

		int readPos = handler.getChannel().read(writeBuffer);

		if (readPos > 4) {

			// 当读取到数据后，开始解析byte字符，找到当前请求的上下文中的类型标识
			byte flag = writeBuffer.get(4);

			ServStateReqEnum stateProc = ServStateReqEnum.getpkgProc(flag);

			if (null != stateProc) {
				context.setCurrState(stateProc.getStateProc());
				context.setFrontedConn(handler);
				// 进行状态的读取流程
				context.serviceDoInvoke();
			}
		}
	}

	@Override
	public void doWrite(FrontendMidConnnectHandler mysqlService) throws IOException {
		if (context.getCurrState() != null) {
			context.setFrontedConn(mysqlService);
			// 进行数据写入流程
			context.serviceDoInvoke();

			// 进行当前位置的设置
			ByteBuffer writeBuffer = mysqlService.getBackMysqlConn().getReadBuffer();

			int pos = writeBuffer.position();
			writeBuffer.position(0);
			writeBuffer.limit(context.getReadPosition());

			int writeSize = mysqlService.getChannel().write(writeBuffer);

			// 重新设置buffer位置
			writeBuffer.position(writeSize);
			writeBuffer.limit(pos);

			// 进行压缩
			writeBuffer.compact();

		}
	}

}
