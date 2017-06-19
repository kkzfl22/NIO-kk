package com.kk.nio.demo.midd.handler.multmidconn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.midd.handler.BaseHandler;

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

	public MultMidConnHandler(SocketChannel channel, int event, BaseHandler backMysqlConn) throws IOException {
		super(channel, event);
		this.mysqlConn = backMysqlConn;
	}

	@Override
	protected void doRead() {

	}

	@Override
	protected void doWrite() {

	}

	public BaseHandler getMysqlConn() {
		return mysqlConn;
	}


}
