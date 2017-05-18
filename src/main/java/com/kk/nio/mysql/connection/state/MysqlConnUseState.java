package com.kk.nio.mysql.connection.state;

import java.io.IOException;

import com.kk.nio.mysql.connection.MysqlConnContext;
import com.kk.nio.mysql.connection.MysqlConnStateInf;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;

/**
 * 当前mysql的状态为使用中
 * 
 * @since 2017年4月15日 下午12:17:27
 * @version 0.0.1
 * @author kk
 */
public class MysqlConnUseState implements MysqlConnStateInf {

	/**
	 * 上下文对象
	 */
	private MysqlStateContext mysqlContext = new MysqlStateContext();

	@Override
	public void stateReadProcess(MysqlConnContext context) throws IOException {
		// 进行响应数据的读取
		// 设置mysql的上下文处理对象
		mysqlContext.setContext(context.getContext());

		//context.getContext().getReadBuffer().clear();

		// 设置编码码器信息
		mysqlContext.setMsgEndecode(context.getMsgEndecode());

		// 进行流程包的设置
		mysqlContext.setRWPkgHandler();

		// 进行消息的读取
		mysqlContext.pkgRead();

	}

	@Override
	public void stateWriteProcess(MysqlConnContext context) throws IOException {
		// 设置mysql的上下文处理对象
		mysqlContext.setContext(context.getContext());

		// 设置当前的状态为连接请求状态
		//mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_REQ.getState());
		mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_PROC_PARAM_SET_REQ.getState());

		// 设置编码码器信息
		mysqlContext.setMsgEndecode(context.getMsgEndecode());

		// 进行流程包的设置
		mysqlContext.setRWPkgHandler();

		// 继续进行数据的写入
		mysqlContext.pkgWrite();

	}

}
