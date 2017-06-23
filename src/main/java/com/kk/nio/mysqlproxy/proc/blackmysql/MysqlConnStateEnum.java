package com.kk.nio.mysqlproxy.proc.blackmysql;

import com.kk.nio.mysqlproxy.proc.blackmysql.connstate.BlackMysqlConnIOStateCreate;
import com.kk.nio.mysqlproxy.proc.blackmysql.connstate.BlackMysqlConnIOStateUse;

/**
 * 进行mysql的状态枚举信息
 * 
 * @since 2017年6月23日 下午3:00:04
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlConnStateEnum {

	/**
	 * 当前的状态为创建连接状态
	 */
	BLACLMYSQLCONNSTATE_CONN(new BlackMysqlConnIOStateCreate()),

	/**
	 * 当前的状态为使用连接状态
	 */
	BLACLMYSQLCONNSTATE_USE(new BlackMysqlConnIOStateUse()),

	;

	/**
	 * 连接的状态
	 */
	private BlackMysqlConnHandStateInf mysqlConnState;

	private MysqlConnStateEnum(BlackMysqlConnHandStateInf mysqlConnState) {
		this.mysqlConnState = mysqlConnState;
	}

	public BlackMysqlConnHandStateInf getMysqlConnState() {
		return mysqlConnState;
	}

	public void setMysqlConnState(BlackMysqlConnHandStateInf mysqlConnState) {
		this.mysqlConnState = mysqlConnState;
	}

}
