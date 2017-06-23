package com.kk.nio.mysqlproxy.proc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.mysqlproxy.nio.MultRector;

/**
 * 进行mysql连接处理的基础类
 * 
 * @since 2017年6月22日 下午11:18:30
 * @version 0.0.1
 * @author liujun
 */
public abstract class ConnectHandler {

	/**
	 * 绑定的select对象信息
	 */
	private Selector bindSelect;

	/**
	 * 绑定的rector信息
	 */
	private MultRector bindRector;

	/**
	 * 注册的key的信息
	 */
	private SelectionKey selKey;

	/**
	 * 通道信息
	 */
	private SocketChannel channel;

	/**
	 * 读取的buffer信息
	 */
	private ByteBuffer readBuffer;

	/**
	 * 写入的buffer信息
	 */
	private ByteBuffer writeBuffer;

	/**
	 * 读取的buffer的位置信息
	 */
	private int readPostion;

	/**
	 * 写入的buffer的位置信息
	 */
	private int writePosition;

	/**
	 * 当前打开读取事件
	 */
	public void eventRigOpenRead() {
		selKey.interestOps(selKey.interestOps() | SelectionKey.OP_READ);
	}

	/**
	 * 当前打开写入事件
	 */
	public void eventRigOpenWrite() {
		selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
	}

	/**
	 * 当前取消读取事件
	 */
	public void eventRigCancelRead() {
		selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_READ);
	}

	/**
	 * 当前取消写入事件
	 */
	public void eventRigCancelWrite() {
		selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_WRITE);
	}

	/**
	 * 当前注册写入事件取消读取事件
	 */
	public void eventRigCancelReadOpenWrite() {
		selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

	/**
	 * 当前取消写入事件，注册读取事件
	 */
	public void eventRigCancelWriteOpenRead() {
		selKey.interestOps(selKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
	}

	public Selector getBindSelect() {
		return bindSelect;
	}

	public void setBindSelect(Selector bindSelect) {
		this.bindSelect = bindSelect;
	}

	public MultRector getBindRector() {
		return bindRector;
	}

	public void setBindRector(MultRector bindRector) {
		this.bindRector = bindRector;
	}

	public SelectionKey getSelKey() {
		return selKey;
	}

	public void setSelKey(SelectionKey selKey) {
		this.selKey = selKey;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
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

	public int getReadPostion() {
		return readPostion;
	}

	public void setReadPostion(int readPostion) {
		this.readPostion = readPostion;
	}

	public int getWritePosition() {
		return writePosition;
	}

	public void setWritePosition(int writePosition) {
		this.writePosition = writePosition;
	}

	/**
	 * 进行数据读取
	 * 
	 * @throws IOException
	 *             中间的数据异常问题
	 */
	public abstract void doRead() throws IOException;

	/**
	 * 进行数据写入
	 * 
	 * @throws IOException
	 *             中间的数据异常问题
	 */
	public abstract void doWrite() throws IOException;

}
