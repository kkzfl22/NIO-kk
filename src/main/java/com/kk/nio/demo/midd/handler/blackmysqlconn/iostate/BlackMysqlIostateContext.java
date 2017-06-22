package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import com.kk.nio.demo.midd.handler.blackmysqlconn.BlackmysqlConnHandler;

/**
 * 进行具体的iostate连接状态的处理
 * 
 * @since 2017年6月20日 下午2:06:59
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIostateContext {

	/**
	 * 连接对象信息
	 */
	private BlackmysqlConnHandler mysqlConn;

	/**
	 * 当前的状态信息
	 */
	private MysqlIoStateInf currState;

	public BlackMysqlIostateContext(BlackmysqlConnHandler mysqlConn) {
		this.mysqlConn = mysqlConn;
	}

	/**
	 * 进行数据的读取操作
	 * 
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             异常信息
	 */
	public void doRead() throws Exception {
		this.currState.doRead(this);
	}

	/**
	 * 进行数据写入操作
	 * 
	 * @throws Exception
	 *             连接的异常信息
	 */
	public void doWrite() throws Exception {
		this.currState.doWrite(this);
	}

	public BlackmysqlConnHandler getMysqlConn() {
		return mysqlConn;
	}

	public void setMysqlConn(BlackmysqlConnHandler mysqlConn) {
		this.mysqlConn = mysqlConn;
	}

	public MysqlIoStateInf getCurrState() {
		return currState;
	}

	public void setCurrState(MysqlIoStateInf currState) {
		this.currState = currState;
	}

}
