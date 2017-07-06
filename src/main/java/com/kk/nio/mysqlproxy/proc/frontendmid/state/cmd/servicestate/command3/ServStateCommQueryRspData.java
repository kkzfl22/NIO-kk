package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgEofBean;
import com.kk.nio.mysqlproxy.mysqlpkg.console.MysqlServerStatusEnum;
import com.kk.nio.mysqlproxy.mysqlpkg.console.PkgEnum;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console.PkgFlagEnum;
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

		while (mysqlService.getReadPosition() + 5 < readBuffer.limit()) {

			// 进行eof包大小的解析
			int length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());

			if (mysqlService.getReadPosition() + length <= readBuffer.limit()) {

				// 取出当前包的类型
				byte pkgType = readBuffer.get(mysqlService.getReadPosition() + 4);

				// 检查包长度，然后进行设置已经读取到的列信息
				if (pkgType != PkgFlagEnum.PKG_EOF_FLAG.getPkgFlag()) {

					mysqlService.setReadPosition(mysqlService.getReadPosition() + length);
				} else {
					PkgEofBean eofBean = (PkgEofBean) PkgEnum.PKG_EOF.getPkgDecode().readPackage(readBuffer,
							mysqlService.getReadPosition());

					// 进行已经读取的包设置
					mysqlService.setReadPosition(mysqlService.getReadPosition() + length);

					// 进行多结果集检查
					boolean multRsp = MysqlServerStatusEnum.StatusCheck(eofBean.getStatusFlag(),
							MysqlServerStatusEnum.MORE_RESULTS);

					// 进行多个查询
					boolean queryRsp = MysqlServerStatusEnum.StatusCheck(eofBean.getStatusFlag(),
							MysqlServerStatusEnum.MULT_QUERY);

					if (multRsp || queryRsp) {
						// 下一步流程为响应结果头的检查
						mysqlService.setCurrState(ServStateRspEnum.SERV_STATE_RSP_HEADER_OVER.getStateProc());
						// 执行下一步流程
						mysqlService.serviceDoInvoke();

						break;
					}
					break;
				}
			}

		}

	}

}
