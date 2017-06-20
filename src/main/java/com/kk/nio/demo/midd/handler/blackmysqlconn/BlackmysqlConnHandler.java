package com.kk.nio.demo.midd.handler.blackmysqlconn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.midd.handler.BaseHandler;
import com.kk.nio.demo.midd.handler.blackmysqlconn.connstate.BlackMysqlConnStateContext;
import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIostateContext;

/**
 * 用来进行mysql的读写的相关处理
 * 
 * @since 2017年6月14日 下午4:00:24
 * @version 0.0.1
 * @author liujun
 */
public class BlackmysqlConnHandler extends BaseHandler {

	/**
	 * 保留mysql的连接的上下文引用
	 */
	private BlackMysqlConnStateContext mysqlConnContext;

	public BlackmysqlConnHandler(SocketChannel channel, int event) throws IOException {
		super(channel, event);
		// 进行io的上下文处理
		BlackMysqlIostateContext iostateContext = new BlackMysqlIostateContext(channel);
		// 进行mysql连接的上下文处理
		mysqlConnContext = new BlackMysqlConnStateContext(iostateContext);
		// 设置当前的状态
		mysqlConnContext.setCurrConnState(MysqlConnStateEnum.MYSQLCONN_CREATE.getConnState());
	}

	@Override
	protected void doRead() {

		// 设置读取共用的buffer
		mysqlConnContext.getIostateContext().setReadBuffer(this.getReadBuffer());
		mysqlConnContext.getIostateContext().setCurrSelkey(this.getCurrSelectKey());

		try {
			mysqlConnContext.doRead();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doWrite() {
		// 设置读取共用的buffer
		mysqlConnContext.getIostateContext().setWriteBuffer(this.getWriteBuffer());
		mysqlConnContext.getIostateContext().setCurrSelkey(this.getCurrSelectKey());

		try {
			mysqlConnContext.doWrite();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
