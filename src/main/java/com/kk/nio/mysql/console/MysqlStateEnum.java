package com.kk.nio.mysql.console;

import com.kk.nio.mysql.servicehandler.flow.MysqlCommStateHandler;
import com.kk.nio.mysql.servicehandler.flow.MysqlLoginStateHandler;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * mysql的状态信息
 * 
 * @since 2017年4月14日 下午4:12:45
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlStateEnum {

	/**
	 * 进行mysql登录的状态处理
	 */
	LOGIN_AUTH((byte) 255, new MysqlLoginStateHandler()),

	/**
	 * 进行mysql登录结果的处理
	 */
	PGK_COMM((byte) 255, new MysqlCommStateHandler()),
	

	;

	/**
	 * 报文标识
	 */
	private byte flag;

	/**
	 * 状态信息
	 */
	private MysqlStateInf state;

	private MysqlStateEnum(byte flag, MysqlStateInf state) {
		this.flag = flag;
		this.state = state;
	}

	public MysqlStateInf getState() {
		return state;
	}

	public void setState(MysqlStateInf state) {
		this.state = state;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public static MysqlStateInf getState(byte flag) {
		MysqlStateEnum[] vals = values();

		for (MysqlStateEnum stateEnum : vals) {
			if (stateEnum.flag == flag) {
				return stateEnum.getState();
			}
		}

		return null;
	}

}
