package com.kk.nio.mysql.console;

import com.kk.nio.mysql.connection.MysqlConnStateInf;
import com.kk.nio.mysql.connection.state.MysqlConnCreateState;
import com.kk.nio.mysql.connection.state.MysqlConnUseState;

/**
 * mysql的连接的状态信息
 * 
 * @since 2017年4月14日 下午4:12:45
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlConnStateEnum {

	/**
	 * 创建连接状态
	 */
	MYSQL_CONN_STATE_CREATE(new MysqlConnCreateState()),
	
	
	/**
	 * 使用连接的状态 
	 */
	MYSQL_CONN_STATE_USE(new MysqlConnUseState()),

	;

	/**
	 * 状态信息
	 */
	private MysqlConnStateInf connState;

	private MysqlConnStateEnum(MysqlConnStateInf connState) {
		this.connState = connState;
	}

	public MysqlConnStateInf getConnState() {
		return connState;
	}

	public void setConnState(MysqlConnStateInf connState) {
		this.connState = connState;
	}

}
