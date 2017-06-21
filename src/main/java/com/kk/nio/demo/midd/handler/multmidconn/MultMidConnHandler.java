package com.kk.nio.demo.midd.handler.multmidconn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.midd.handler.BaseHandler;
import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateInf;

/**
 * 进行中间件的多路连接处理
 * 
 * @since 2017年6月16日 下午3:02:31
 * @version 0.0.1
 * @author liujun
 */
public class MultMidConnHandler extends BaseHandler {

	/**
	 * 后端的连接信息
	 */
	protected BaseHandler mysqlConn;

	/**
	 * 当前中间处理的状态
	 */
	private MultMidStateInf currMidState;

	public MultMidConnHandler(SocketChannel channel, int event, BaseHandler backMysqlConn) throws IOException {
		super(channel, event);
		this.mysqlConn = backMysqlConn;
		// 设置当前的中间件状态
		this.currMidState = MultMidStateEnum.MULTMIDSTATE_HANDSHAKE.getMultMidState();
	}

	@Override
	protected void doRead() {
		try {
			this.currMidState.doRead(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doWrite() {
		try {
			this.currMidState.doWrite(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public BaseHandler getMysqlConn() {
		return mysqlConn;
	}

	public MultMidStateInf getCurrMidState() {
		return currMidState;
	}

	public void setCurrMidState(MultMidStateInf currMidState) {
		this.currMidState = currMidState;
	}

	public void setMysqlConn(BaseHandler mysqlConn) {
		this.mysqlConn = mysqlConn;
	}

}
