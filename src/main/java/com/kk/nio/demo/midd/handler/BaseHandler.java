package com.kk.nio.demo.midd.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 最基本的后台处理handler
 * 
 * @since 2017年6月19日 下午6:01:20
 * @version 0.0.1
 * @author liujun
 */
public abstract class BaseHandler implements Runnable {

	/**
	 * 通道信息
	 */
	protected SocketChannel channel;

	/**
	 * 当前注册的key信息
	 */
	protected SelectionKey currSelkey;

	/**
	 * 选择器的信息
	 */
	protected Selector select;

	/**
	 * 注册的事件信息
	 */
	protected int registEvent;

	/**
	 * 读取的缓冲队列
	 */
	protected volatile ByteBuffer readBuffer;

	/**
	 * 写入的队列
	 */
	protected volatile ByteBuffer writeBuffer;

	/**
	 * 读取的指针
	 */
	protected volatile int readPostion;

	/**
	 * 写入的指针
	 */
	protected volatile int writePostion;

	public BaseHandler(SocketChannel channel, int event) throws IOException {
		this.channel = channel;
		// 设置为非阻塞模式
		channel.configureBlocking(false);
		this.registEvent = event;
	}

	@Override
	public void run() {

		// 如果当前是读取事件，交给读取方法
		if (currSelkey.isReadable()) {
			doRead();
		}
		// 如果是写入事件，交给写入方法处理
		else if (currSelkey.isWritable()) {
			doWrite();
		}
	}

	/**
	 * 进行数据读取操作
	 */
	protected abstract void doRead();

	/**
	 * 进行数据写入
	 */
	protected abstract void doWrite();

	public SelectionKey getCurrSelkey() {
		return currSelkey;
	}

	public void setCurrSelkey(SelectionKey currSelkey) {
		this.currSelkey = currSelkey;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setSelect(Selector select) {
		this.select = select;
	}

	public Selector getSelect() {
		return select;
	}

	public int getRegistEvent() {
		return registEvent;
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

	public int getWritePostion() {
		return writePostion;
	}

	public void setWritePostion(int writePostion) {
		this.writePostion = writePostion;
	}

}
