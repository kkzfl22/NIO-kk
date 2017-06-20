package com.kk.nio.demo.midd.handler.blackmysqlconn.connstate;

import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIostateContext;

/**
 * 进行后端mysql连接的状态的上下文处理
 * 
 * @since 2017年6月20日 下午1:34:33
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnStateContext {

	/**
	 * 当前的连接状态
	 */
	private volatile MysqlConnStateInf currConnState;

	/**
	 * 进行io事件处理的上下文信息
	 */
	private BlackMysqlIostateContext iostateContext;

	public BlackMysqlConnStateContext(BlackMysqlIostateContext iostateContext) {
		super();
		this.iostateContext = iostateContext;
	}

	public MysqlConnStateInf getCurrConnState() {
		return currConnState;
	}

	public void setCurrConnState(MysqlConnStateInf currConnState) {
		this.currConnState = currConnState;
	}

	public BlackMysqlIostateContext getIostateContext() {
		return iostateContext;
	}

	public void setIostateContext(BlackMysqlIostateContext iostateContext) {
		this.iostateContext = iostateContext;
	}

	/**
	 * 进行连接的数据的读取操作
	 * 
	 * @param mysqlConn
	 *            连接的上下文信息
	 * @throws Exception
	 *             异常信息
	 */
	public void doRead() throws Exception {
		 currConnState.doRead(this);
	}

	/**
	 * 进行连接的数据写入操作
	 * 
	 * @param mysqlConn
	 *            连接的上下文信息
	 * @return true 写入结束，false 结束
	 * @throws Exception
	 *             连接的异常信息
	 */
	public void doWrite() throws Exception {
		 currConnState.doWrite(this);
	}

}
