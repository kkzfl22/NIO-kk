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

			// 取出当前包的类型
			byte pkgType = readBuffer.get(mysqlService.getReadPosition() + 4);
			
			// 检查包长度，然后进行设置已经读取到的列信息
			if (pkgType != PkgFlagEnum.PKG_EOF_FLAG.getPkgFlag()) {

				mysqlService.setReadPosition(mysqlService.getReadPosition() + length);

				// 获取下一个包的长度
				length = BufferTools.getLength(readBuffer, mysqlService.getReadPosition());
				// 获取包的类型
				pkgType = readBuffer.get(mysqlService.getReadPosition() + 4);

			}

		}

	}

}
