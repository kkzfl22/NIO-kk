package com.kk.nio.mysql.connection.state;

import java.io.IOException;

import com.kk.nio.mysql.connection.MysqlConnContext;
import com.kk.nio.mysql.connection.MysqlConnStateInf;
import com.kk.nio.mysql.console.MysqlConnStateEnum;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;

/**
 * 进行mysql的连接的状态处理
 * 
 * @since 2017年4月15日 下午12:17:27
 * @version 0.0.1
 * @author kk
 */
public class MysqlConnCreateState implements MysqlConnStateInf {

	/**
	 * 上下文对象
	 */
	private MysqlStateContext mysqlContext = new MysqlStateContext();

	@Override
	public void stateReadProcess(MysqlConnContext context) throws IOException {

		// 设置mysql的上下文处理对象
		mysqlContext.setContext(context.getContext());

		// 设置解码器信息
		mysqlContext.setMsgEndecode(context.getMsgEndecode());

		// 如果当前是第一次处理，则指定为登录处理
		if (mysqlContext.getCurrMysqlState() == null) {
			// 指定默认的处理器
			mysqlContext.setCurrMysqlState(MysqlStateEnum.LOGIN_AUTH.getState());
		}

		// 进行流程包的设置
		mysqlContext.setRWPkgHandler();

		// 进行指定的流程包处理
		mysqlContext.pkgRead();

		Boolean result = (Boolean) mysqlContext.getResult();

		// 检查数据是否已经处理成功返回
		if (null != result && result) {
			// 当前已经成功连接，状态切换为使用SQL状态
			context.setMysqlConnState(MysqlConnStateEnum.MYSQL_CONN_STATE_USE.getConnState());
			//并且操作为发送SQL
			context.stateWriteProcess();
		}
	}

	@Override
	public void stateWriteProcess(MysqlConnContext context) throws IOException {
		// 设置mysql的上下文处理对象
		mysqlContext.setContext(context.getContext());

		// 继续进行数据的写入
		mysqlContext.pkgWrite();

	}

}
