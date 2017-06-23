package com.kk.nio.mysqlproxy.proc.blackmysql.iostate;

import java.io.IOException;

import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;

/**
 * 进行具体的io处理的状态的上下文信息
 * 
 * @since 2017年6月23日 下午2:15:21
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIOStateContext {

	/**
	 * 当前的状态信息
	 */
	private BlackMysqlIOStateInf currState;

	/**
	 * 进行连接处理的上下文对象
	 */
	private BlackMysqlClientHandler mysqlConnStateContext;

	public BlackMysqlIOStateInf getCurrState() {
		return currState;
	}

	public void setCurrState(BlackMysqlIOStateInf currState) {
		this.currState = currState;
	}

	public BlackMysqlClientHandler getMysqlConnStateContext() {
		return mysqlConnStateContext;
	}

	public void setMysqlConnStateContext(BlackMysqlClientHandler mysqlConnStateContext) {
		this.mysqlConnStateContext = mysqlConnStateContext;
	}

	/**
	 * 进行读取操作
	 * 
	 * @param handler
	 *            连接信息
	 * @throws IOException
	 *             异常
	 */
	public void doRead() throws IOException {
		this.currState.doRead(this);
	}

	/**
	 * 进行写入操作
	 * 
	 * @param handler
	 *            连接信息
	 * @throws IOException
	 *             异常
	 */
	public void doWrite() throws IOException {
		this.currState.doWrite(this);
	}

}
