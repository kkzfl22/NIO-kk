package com.kk.nio.mysqlproxy.proc.frontendmid;

import java.io.IOException;

import com.kk.nio.mysqlproxy.proc.ConnectHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.state.auth.FrontedIoStateHandshake;

/**
 * 进行中间件处理的handler
 * 
 * @since 2017年6月22日 下午11:21:08
 * @version 0.0.1
 * @author liujun
 */
public class FrontendMidConnnectHandler extends ConnectHandler {

	/**
	 * 后端的连接信息
	 */
	private BlackMysqlClientHandler backMysqlConn;

	/**
	 * 当前的前端操作的状态
	 */
	private FrontendIOHandStateInf currState;
	

	public FrontendMidConnnectHandler() {
		//进行初始的状态设置,为握手包的处理
		currState = new FrontedIoStateHandshake();
	}

	public BlackMysqlClientHandler getBackMysqlConn() {
		return backMysqlConn;
	}

	public void setBackMysqlConn(BlackMysqlClientHandler backMysqlConn) {
		this.backMysqlConn = backMysqlConn;
	}

	@Override
	public void doRead() throws IOException {
		this.getCurrState().doRead(this);
	}

	@Override
	public void doWrite() throws IOException {
		this.getCurrState().doWrite(this);
	}

	public FrontendIOHandStateInf getCurrState() {
		return currState;
	}

	public void setCurrState(FrontendIOHandStateInf currState) {
		this.currState = currState;
	}

}
