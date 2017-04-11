package com.kk.nio.mysql.chain;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.kk.nio.mysql.packhandler.MysqlDataPackageReadInf;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 进行mysql处理的上下文信息
 * 
 * @since 2017年4月10日 下午8:20:40
 * @version 0.0.1
 * @author liujun
 */
public class MysqlContext {

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
	 * 进行的数据处理
	 */
	private MysqlDataPackageReadInf<PackageHeader> handlerProc;

	public MysqlContext(SocketChannel socketChannel, SelectionKey selectKey, ByteBuffer writeBuffer,
			ByteBuffer readBuffer) {
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

	public ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(ByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}

	public MysqlDataPackageReadInf<PackageHeader> getHandlerProc() {
		return handlerProc;
	}

	public void setHandlerProc(MysqlDataPackageReadInf<PackageHeader> handlerProc) {
		this.handlerProc = handlerProc;
	}

}
