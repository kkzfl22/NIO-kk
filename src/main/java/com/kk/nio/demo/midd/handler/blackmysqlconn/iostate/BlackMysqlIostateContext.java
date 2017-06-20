package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 进行具体的iostate连接状态的处理
 * 
 * @since 2017年6月20日 下午2:06:59
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIostateContext {

	/**
	 * 通道信息
	 */
	private SocketChannel channel;

	/**
	 * 注册的key的信息
	 */
	private SelectionKey currSelkey;

	/**
	 * 读取的byteBuffer;
	 */
	private volatile ByteBuffer readBuffer;

	/**
	 * 写入的byteBuffer
	 */
	private volatile ByteBuffer writeBuffer;

	/**
	 * 读取的指针位置
	 */
	private int readPostion;

	/**
	 * 写入的指针位置
	 */
	private int writePostion;

	/**
	 * 当前的状态信息
	 */
	private MysqlIoStateInf currState;

	public BlackMysqlIostateContext(SocketChannel channel) {
		super();
		this.channel = channel;
	}

	public void setCurrSelkey(SelectionKey currSelkey) {
		this.currSelkey = currSelkey;
	}

	public ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(ByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}

	public ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	public void setWriteBuffer(ByteBuffer writeBuffer) {
		this.writeBuffer = writeBuffer;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public SelectionKey getCurrSelkey() {
		return currSelkey;
	}

	public MysqlIoStateInf getCurrState() {
		return currState;
	}

	public void setCurrState(MysqlIoStateInf currState) {
		this.currState = currState;
	}

	public int getReadPostion() {
		return readPostion;
	}

	public void setReadPostion(int readPostion) {
		this.readPostion = readPostion;
	}

	public int getWritePostion() {
		return writePostion;
	}

	public void setWritePostion(int writePostion) {
		this.writePostion = writePostion;
	}

	/**
	 * 进行数据的读取操作
	 * 
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             异常信息
	 */
	public boolean doRead() throws Exception {
		return this.currState.doRead(this);
	}

	/**
	 * 进行数据写入操作
	 * 
	 * @throws Exception
	 *             连接的异常信息
	 */
	public boolean doWrite() throws Exception {
		return this.currState.doWrite(this);
	}

}
