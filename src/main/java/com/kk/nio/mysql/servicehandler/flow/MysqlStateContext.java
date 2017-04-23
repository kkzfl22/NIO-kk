package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;

import com.kk.nio.mysql.chain.MsgEnDecodeInf;
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

	/**
	 * 设置解码器对象
	 */
	private MsgEnDecodeInf msgEndecode;

	/**
	 * 返回结果的数据
	 */
	private Object result;
	
	
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

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public MsgEnDecodeInf getMsgEndecode() {
		return msgEndecode;
	}

	public void setMsgEndecode(MsgEnDecodeInf msgEndecode) {
		this.msgEndecode = msgEndecode;
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
	public void pkgRead() throws IOException {
		currMysqlState.pkgRead(this);

	}

	/**
	 * 进行包的处理
	 * 
	 * @throws IOException
	 */
	public void pkgWrite() throws IOException {
		currMysqlState.pkgWrite(this);

	}

}
