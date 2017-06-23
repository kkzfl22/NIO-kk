package com.kk.nio.mysqlproxy.proc.blackmysql.iostate;

import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.BlackMysqlIoStateAuthRsp;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.BlackMysqlIoStateHandshake;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.BlackMysqlIoStateSelUser;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.BlackMysqlIoStateSelVersion;

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
