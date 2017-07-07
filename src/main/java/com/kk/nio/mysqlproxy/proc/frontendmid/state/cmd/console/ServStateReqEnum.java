package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console;

import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3.ServStateCommQuery;

/**
 * mysql的业务的状态
 * 
 * @since 2017年7月2日 上午11:03:02
 * @version 0.0.1
 * @author liujun
 */
public enum ServStateReqEnum {

	/**
	 * 进行查询存储过程等，以命令包为3的类型处理
	 */
	STATE_COMM_PROCQUERY((byte) 3, new ServStateCommQuery()),

	/**
	 * 进行查询存储过程等，以命令包为3的类型处理
	 */
	STATE_COMM_USE((byte) 2, new ServStateCommQuery());

	/**
	 * 包标识信息
	 */
	private byte mysqlpkgFlag;

	/**
	 * 对应的状态处理
	 */
	private MysqlServiceStateInf stateProc;

	private ServStateReqEnum(byte mysqlpkgFlag, MysqlServiceStateInf stateProc) {
		this.mysqlpkgFlag = mysqlpkgFlag;
		this.stateProc = stateProc;
	}

	public byte getMysqlpkgFlag() {
		return mysqlpkgFlag;
	}

	public void setMysqlpkgFlag(byte mysqlpkgFlag) {
		this.mysqlpkgFlag = mysqlpkgFlag;
	}

	public MysqlServiceStateInf getStateProc() {
		return stateProc;
	}

	public void setStateProc(MysqlServiceStateInf stateProc) {
		this.stateProc = stateProc;
	}

	/**
	 * 获取处理的状态信息
	 * 
	 * @param pkgType
	 * @return
	 */
	public static ServStateReqEnum getpkgProc(byte pkgType) {
		for (ServStateReqEnum serviceState : values()) {
			if (serviceState.getMysqlpkgFlag() == pkgType) {
				return serviceState;
			}
		}

		return null;
	}

}
