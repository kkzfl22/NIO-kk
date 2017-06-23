package com.kk.nio.mysqlproxy.proc.frontendmid;

import com.kk.nio.mysqlproxy.proc.frontendmid.state.FrontedIoStateAuthRsp;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.FrontedIoStateHandshake;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.FrontedIoStateSelUser;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.FrontedIoStateSelVersion;

/**
 * 进行mysql的状态枚举信息
 * 
 * @since 2017年6月23日 下午3:00:04
 * @version 0.0.1
 * @author liujun
 */
public enum FrontendMidStateEnum {

	/**
	 * 进行握手包流程的操作
	 */
	FRONTENDSTATE_HANDSHAKE(new FrontedIoStateHandshake()),
	
	
	/**
	 * 进行握手包结果响应
	 */
	FRONTENDSTATE_AUTHRSP(new FrontedIoStateAuthRsp()),
	
	/**
	 * 进行查询mysql版本的透传
	 */
	FRONTENDSTATE_SELVERSION(new FrontedIoStateSelVersion()),
	
	
	/**
	 * 进行查询mysql一次心跳检查
	 */
	FRONTENDSTATE_SELUSER(new FrontedIoStateSelUser()),
	
	
	
	
	

	;

	/**
	 * 连接的状态
	 */
	private FrontendIOHandStateInf mysqlConnState;

	private FrontendMidStateEnum(FrontendIOHandStateInf mysqlConnState) {
		this.mysqlConnState = mysqlConnState;
	}

	public FrontendIOHandStateInf getMysqlConnState() {
		return mysqlConnState;
	}

	public void setMysqlConnState(FrontendIOHandStateInf mysqlConnState) {
		this.mysqlConnState = mysqlConnState;
	}

}