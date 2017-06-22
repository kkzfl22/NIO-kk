package com.kk.nio.demo.midd.handler.blackmysqlconn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.midd.handler.BaseHandler;
import com.kk.nio.demo.midd.handler.blackmysqlconn.connstate.MysqlConnStateInf;
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
	 * 当前的连接状态
	 */
	private volatile MysqlConnStateInf currConnState;

	/**
	 * 进行io事件处理的上下文信息
	 */
	private BlackMysqlIostateContext iostateContext;

	public BlackmysqlConnHandler(SocketChannel channel, int event) throws IOException {
		super(channel, event);
		// 进行io的上下文处理
		this.iostateContext = new BlackMysqlIostateContext(this);
		// 设置io的初始化状态为握手包读取
		this.iostateContext.setCurrState(MysqlIoStateEnum.IOSTATE_HANDSHAKE.getIoState());
		// 设置当前的状态
		this.currConnState = MysqlConnStateEnum.MYSQLCONN_CREATE.getConnState();
	}

	@Override
	protected void doRead() {

		try {
			currConnState.doRead(this);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.getChannel().close();
				this.getCurrSelkey().cancel();
				this.setReadPostion(0);
				this.getReadBuffer().clear();
				this.setWritePostion(0);
				this.getWriteBuffer().clear();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	protected void doWrite() {
		try {
			currConnState.doWrite(this);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.getChannel().close();
				this.getCurrSelkey().cancel();
				this.setReadPostion(0);
				this.getReadBuffer().clear();
				this.setWritePostion(0);
				this.getWriteBuffer().clear();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
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

}
