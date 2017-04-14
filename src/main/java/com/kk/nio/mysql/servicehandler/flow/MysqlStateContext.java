package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;

/**
 * 状态处理的上下文状态信息
 * 
 * @since 2017年4月14日 下午4:07:03
 * @version 0.0.1
 * @author liujun
 */
public class MysqlStateContext {

	/**
	 * 上下文对象信息
	 */
	private MysqlContext context;

	/**
	 * 当前的状态
	 */
	private MysqlStateInf currMysqlState;

	public MysqlContext getContext() {
		return context;
	}

	public void setContext(MysqlContext context) {
		this.context = context;
	}

	public MysqlStateInf getCurrMysqlState() {
		return currMysqlState;
	}

	public void setCurrMysqlState(MysqlStateInf currMysqlState) {
		this.currMysqlState = currMysqlState;
	}

	/**
	 * 进行设置包的解析
	 */
	public void setRWPkgHandler() {
		currMysqlState.setRWPkgHandler(this);
	}

	/**
	 * 进行包的处理
	 * 
	 * @throws IOException
	 */
	public void pkgHandler() throws IOException {
		currMysqlState.pkgHandler(this);

	}

}
