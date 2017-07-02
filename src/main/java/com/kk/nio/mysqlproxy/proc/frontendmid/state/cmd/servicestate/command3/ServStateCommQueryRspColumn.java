package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgResultSetHander;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServFlowEnum;
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

		PkgResultSetHander resultSetHeader = (PkgResultSetHander) mysqlService
				.getTransBean(ServFlowEnum.STATE_RESULTSET_FIELDNUM.getKey());

		int colNum = resultSetHeader.getFieldCount();

		Map<Integer, Boolean> columnCheck = new HashMap<>();

		for (int i = 0; i < colNum; i++) {
			columnCheck.put(i, false);
		}

		for (int i = 0; i < colNum; i++) {
			// 读取消息头
			int length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());

			int currPosition = readBuffer.position();
			// 检查包长度，然后进行设置已经读取到的列信息
			if (currPosition + length < readBuffer.limit()) {
				mysqlService.setReadPosition(mysqlService.getReadPosition() + length);
				columnCheck.put(i, true);
			}
		}

		// 如果当前所有的列都已经跳过成功，则进行下一步流程
		if (!columnCheck.containsKey(false)) {

			// 进行列结束项的检查
			int length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());

			int currPosition = readBuffer.position();
			// 检查包长度，然后进行设置已经读取到的列信息
			if (currPosition + length < readBuffer.limit()) {
				mysqlService.setReadPosition(mysqlService.getReadPosition() + length);
				// 下一步流程为行数据的检查
				mysqlService.setCurrState(ServStateRspEnum.SERV_STATE_RSP_DATA_OVER.getStateProc());
				// 执行下一步流程
				mysqlService.serviceDoInvoke();
			}
		}

	}

}
