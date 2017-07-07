package com.kk.nio.mysqlproxy.proc.blackmysql.iostate;

import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.auth.BlackMysqlIoStateAuthRsp;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.auth.BlackMysqlIoStateHandshake;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.auth.BlackMysqlIoStateSelUser;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.auth.BlackMysqlIoStateSelVersion;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.cmd.BlackMysqlIoStateCommand;

/**
 * 进行mysql的状态枚举信息
 * 
 * @since 2017年6月23日 下午3:00:04
 * @version 0.0.1
 * @author liujun
 */
public enum MysqIOStateEnum {

	/**
	 * 当前的状态的握手状态
	 */
	BLACLMYSQLIOSTATE_HANDSHAKE(new BlackMysqlIoStateHandshake()),

	/**
	 * 当前的状态的为鉴权结果检查
	 */
	BLACLMYSQLIOSTATE_AUTHRSP(new BlackMysqlIoStateAuthRsp()),

	/**
	 * 当前的状态的为查询版本信息
	 */
	BLACLMYSQLIOSTATE_SELVERSION(new BlackMysqlIoStateSelVersion()),

	/**
	 * 当前的状态的为进行一跳检查selectUser
	 */
	BLACLMYSQLIOSTATE_SELUSER(new BlackMysqlIoStateSelUser()),

	/**
	 * 进行sql的透传的处理
	 */
	BLACKMYSQLIOSTATE_QUEYRCOMMAND(new BlackMysqlIoStateCommand())

	;

	/**
	 * 具体的事件处理的状态
	 */
	private BlackMysqlIOStateInf mysqlIOState;

	private MysqIOStateEnum(BlackMysqlIOStateInf mysqlIOState) {
		this.mysqlIOState = mysqlIOState;
	}

	public BlackMysqlIOStateInf getMysqlIOState() {
		return mysqlIOState;
	}

	public void setMysqlIOState(BlackMysqlIOStateInf mysqlIOState) {
		this.mysqlIOState = mysqlIOState;
	}

}
