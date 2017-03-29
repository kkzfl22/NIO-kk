package com.kk.nio.socket.multreactor.procchain.chain;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Context {

	/**
	 * 操作的socketchannel信息
	 */
	private SocketChannel socketChannel;

	/**
	 * 操作的selectkey信息
	 */
	private SelectionKey selectKey;

	/**
	 * 当前待写入的数据
	 */
	private volatile ByteBuffer writeBuffer;

	/**
	 * 当前待读取的数据的buffer
	 */
	private volatile ByteBuffer readBuffer;

	/**
	 * 待发送的数据信息
	 */
	private Object writeData;

	/**
	 * 上一次操作的位置信息
	 */
	private volatile int lastModPositon;


	public Context(SocketChannel socketChannel, SelectionKey selectKey, ByteBuffer writeBuffer, ByteBuffer readBuffer) {
		super();
		this.socketChannel = socketChannel;
		this.selectKey = selectKey;
		this.writeBuffer = writeBuffer;
		this.readBuffer = readBuffer;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public SelectionKey getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(SelectionKey selectKey) {
		this.selectKey = selectKey;
	}

	public ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	public void setWriteBuffer(ByteBuffer writeBuffer) {
		this.writeBuffer = writeBuffer;
	}

	public Object getWriteData() {
		return writeData;
	}

	public void setWriteData(Object writeData) {
		this.writeData = writeData;
	}

	public int getLastModPositon() {
		return lastModPositon;
	}

	public void setLastModPositon(int lastModPositon) {
		this.lastModPositon = lastModPositon;
	}


	public ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(ByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}

}
