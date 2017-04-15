package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;

/**
 * 进行登录的状态的消息处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlCommStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void setRWPkgHandler(MysqlStateContext context) {

	}

	@Override
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {

		MysqlContext context = mysqlContext.getContext();

		ByteBuffer buffer = context.getReadBuffer();

		// 取得消息响应的类型检查消息的类型
		byte flag = buffer.get(5);

		// 从解析程序中找到运行的流程
		MysqlStateInf mysqlState = MysqlStateEnum.getState(flag);

		if (null != mysqlState) {
			mysqlContext.setCurrMysqlState(mysqlState);
			// 设置数据的解析程序
			mysqlContext.getCurrMysqlState().setRWPkgHandler(mysqlContext);
			// 进行运行流程
			mysqlContext.getCurrMysqlState().pkgRead(mysqlContext);
		}

	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
