package com.kk.nio.mysqlproxy.proc.blackmysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.demo.midd.memory.MemoryPool;
import com.kk.nio.mysqlproxy.memory.ByteBufferPool;
import com.kk.nio.mysqlproxy.proc.ConnectHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.BlackMysqlIOStateContext;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;

/**
 * 进行mysql后端连接的处理
 * 
 * @since 2017年6月23日 下午1:21:54
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlClientHandler extends ConnectHandler {

	/**
	 * 中间件接收数据的处理信息
	 */
	private FrontendMidConnnectHandler midConnHandler;

	/**
	 * 进行具体的io处理的上下文信息
	 */
	private BlackMysqlIOStateContext iostateContext = new BlackMysqlIOStateContext();

	/**
	 * 当前的连接状态
	 */
	private BlackMysqlConnHandStateInf currConnState;

	public BlackMysqlClientHandler() {
		currConnState = MysqlConnStateEnum.BLACLMYSQLCONNSTATE_CONN.getMysqlConnState();
		// 分别配制读取与写入的buffer信息
//		this.setReadBuffer(ByteBufferPool.Instance().allocate(170));
//		this.setWriteBuffer(ByteBufferPool.Instance().allocate(170));
		this.setReadBuffer(ByteBuffer.allocateDirect(400));
		this.setWriteBuffer(ByteBuffer.allocateDirect(400));

		iostateContext.setMysqlConnStateContext(this);
	}

	public FrontendMidConnnectHandler getMidConnHandler() {
		return midConnHandler;
	}

	public void setMidConnHandler(FrontendMidConnnectHandler midConnHandler) {
		this.midConnHandler = midConnHandler;
	}

	@Override
	public void doRead() throws IOException {
		this.currConnState.doRead(this);
	}

	@Override
	public void doWrite() throws IOException {
		this.currConnState.doWrite(this);
	}

	public BlackMysqlIOStateContext getIostateContext() {
		return iostateContext;
	}

	public void setIostateContext(BlackMysqlIOStateContext iostateContext) {
		this.iostateContext = iostateContext;
	}

	public BlackMysqlConnHandStateInf getCurrConnState() {
		return currConnState;
	}

	public void setCurrConnState(BlackMysqlConnHandStateInf currConnState) {
		this.currConnState = currConnState;
	}

}
