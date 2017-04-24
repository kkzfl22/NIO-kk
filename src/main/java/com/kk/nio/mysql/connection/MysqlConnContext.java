package com.kk.nio.mysql.connection;

import java.io.IOException;

import com.kk.nio.mysql.chain.MsgEnDecodeInf;
import com.kk.nio.mysql.chain.MysqlContext;

/**
 * 上下文对象信息
 * 
 * @since 2017年4月15日 下午12:12:12
 * @version 0.0.1
 * @author kk
 */
public class MysqlConnContext {

	/**
	 * 上下文对象信息
	 */
	private MysqlContext context;

	/**
	 * 当前连接的状态处理
	 */
	private volatile MysqlConnStateInf mysqlConnState;

	/**
	 * 设置解码器对象
	 */
	private MsgEnDecodeInf msgEndecode;

	public MysqlContext getContext() {
		return context;
	}

	public void setContext(MysqlContext context) {
		this.context = context;
	}

	public MysqlConnStateInf getMysqlConnState() {
		return mysqlConnState;
	}

	public void setMysqlConnState(MysqlConnStateInf mysqlConnState) {
		this.mysqlConnState = mysqlConnState;
	}

	public void stateReadProcess() throws IOException {
		mysqlConnState.stateReadProcess(this);
	}

	public void stateWriteProcess() throws IOException {
		mysqlConnState.stateWriteProcess(this);
	}

	public MsgEnDecodeInf getMsgEndecode() {
		return msgEndecode;
	}

	public void setMsgEndecode(MsgEnDecodeInf msgEndecode) {
		this.msgEndecode = msgEndecode;
	}

}
