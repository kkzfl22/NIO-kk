package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.PkgFlagEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServStateRspEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceContext;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;
import com.kk.nio.mysqlproxy.util.BufferTools;

/**
 * 进行进行查询响应结果resultset中的列读取
 * 
 * @since 2017年7月2日 上午11:21:01
 * @version 0.0.1
 * @author liujun
 */
public class ServStateCommQueryRspColumn implements MysqlServiceStateInf {

	@Override
	public void serviceDoInvoke(MysqlServiceContext mysqlService) throws IOException {
		FrontendMidConnnectHandler handler = mysqlService.getFrontedConn();

		ByteBuffer readBuffer = handler.getBackMysqlConn().getReadBuffer();

		while (mysqlService.getReadPosition() + 5 < readBuffer.limit()) {

			// 读取消息头
			int length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());

			if (mysqlService.getReadPosition() + length <= readBuffer.limit()) {

				// 检查当前的包为eof包
				byte flag = readBuffer.get(mysqlService.getReadPosition() + 4);
				// 设置已经读取包的数据
				mysqlService.setReadPosition(mysqlService.getReadPosition() + length);

				// 如果检查当前为列结束
				if (flag == PkgFlagEnum.PKG_EOF_FLAG.getPkgFlag()) {

					mysqlService.setReadPosition(mysqlService.getReadPosition() + length);
					// 下一步流程为行数据的检查
					mysqlService.setCurrState(ServStateRspEnum.SERV_STATE_RSP_DATA_OVER.getStateProc());
					// 执行下一步流程
					mysqlService.serviceDoInvoke();

					break;
				}
			}
		}

	}

}
