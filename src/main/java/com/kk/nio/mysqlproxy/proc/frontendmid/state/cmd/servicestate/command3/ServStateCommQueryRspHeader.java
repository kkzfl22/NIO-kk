package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgResultSetHander;
import com.kk.nio.mysqlproxy.mysqlpkg.console.PkgEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.PkgFlagEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServFlowEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.ServStateRspEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceContext;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;

/**
 * 
 * @since 2017年7月2日 上午11:21:01
 * @version 0.0.1
 * @author liujun
 */
public class ServStateCommQueryRspHeader implements MysqlServiceStateInf {

	@Override
	public void serviceDoInvoke(MysqlServiceContext mysqlService) throws IOException {
		FrontendMidConnnectHandler handler = mysqlService.getFrontedConn();

		ByteBuffer readBuffer = handler.getBackMysqlConn().getReadBuffer();

		if (readBuffer.position() > 5) {
			// 首先检查当前包是否为错误响应包
			byte flag = readBuffer.get(4);

			// 如果当前为错误包
			if (flag == PkgFlagEnum.PKG_ERROR_FLAG.getPkgFlag()) {
				// 当前流程结束，直接透传
				mysqlService.setReadPosition(readBuffer.position());
				// 取消写入事件的注册，并进行写入事件
				mysqlService.getFrontedConn().eventRigCancelWriteOpenRead();
			}
			// 如果当前为ok包
			else if (flag == PkgFlagEnum.PKG_OK_FLAG.getPkgFlag()) {
				// 当前流程结束 ，直接透传
				mysqlService.setReadPosition(readBuffer.position());
				// 取消写入事件的注册，并进行写入事件
				mysqlService.getFrontedConn().eventRigCancelWriteOpenRead();
			}
			// 其他包按查询流程进行解析
			else {
				// 首先读取列数
				PkgResultSetHander resultSetHeader = (PkgResultSetHander) PkgEnum.PKG_RESULTSET_HEAD.getPkgDecode()
						.readPackage(readBuffer);

				if (resultSetHeader != null && resultSetHeader.getFieldCount() > 0) {
					mysqlService.putTransBean(ServFlowEnum.STATE_RESULTSET_FIELDNUM.getKey(), resultSetHeader);
					// 当前流程切换到进行响应列的部分解析中
					mysqlService.setCurrState(ServStateRspEnum.SERV_STATE_RSP_COLUMN_OVER.getStateProc());

					// 设置当前已经检查的大小
					mysqlService.setReadPosition(resultSetHeader.getLength());

					// 将流程切换到下一个状态的检查
					mysqlService.serviceDoInvoke();
				}

			}
		}

	}

}
