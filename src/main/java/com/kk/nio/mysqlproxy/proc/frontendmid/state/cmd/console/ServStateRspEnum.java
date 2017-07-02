package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console;

import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.MysqlServiceStateInf;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3.ServStateCommQueryRspColumn;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3.ServStateCommQueryRspData;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate.command3.ServStateCommQueryRspHeader;

/**
 * mysql的业务的状态
 * 
 * @since 2017年7月2日 上午11:03:02
 * @version 0.0.1
 * @author liujun
 */
public enum ServStateRspEnum {

	/**
	 * 响应头的解析
	 */
	SERV_STATE_RSP_HEADER_OVER(new ServStateCommQueryRspHeader()),

	/**
	 * 进行列的解析
	 */
	SERV_STATE_RSP_COLUMN_OVER(new ServStateCommQueryRspColumn()),

	/**
	 * 进行行数据结果的解析
	 */
	SERV_STATE_RSP_DATA_OVER(new ServStateCommQueryRspData());

	/**
	 * 对应的状态处理
	 */
	private MysqlServiceStateInf stateProc;

	private ServStateRspEnum(MysqlServiceStateInf stateProc) {
		this.stateProc = stateProc;
	}

	public MysqlServiceStateInf getStateProc() {
		return stateProc;
	}

	public void setStateProc(MysqlServiceStateInf stateProc) {
		this.stateProc = stateProc;
	}

}
