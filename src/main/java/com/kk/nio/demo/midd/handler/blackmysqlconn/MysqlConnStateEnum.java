package com.kk.nio.demo.midd.handler.blackmysqlconn;

import com.kk.nio.demo.midd.handler.blackmysqlconn.connstate.BlackMysqlConnCreateState;
import com.kk.nio.demo.midd.handler.blackmysqlconn.connstate.BlackMysqlConnUseState;
import com.kk.nio.demo.midd.handler.blackmysqlconn.connstate.MysqlConnStateInf;

/**
 * 具体的io事件处理的枚举类
 * 
 * @since 2017年6月20日 下午3:07:23
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlConnStateEnum {

	/**
	 * 进行连接的创建
	 */
	MYSQLCONN_CREATE(new BlackMysqlConnCreateState()),

	/**
	 * 进行连接的使用
	 */
	MYSQLCONN_USE(new BlackMysqlConnUseState()),

	;

	/**
	 * 状态接口信息
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
