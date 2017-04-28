package com.kk.nio.mysql.chain;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;

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
	 * 进行数据的读取处理
	 */
	private MysqlPackageReadInf readPkgHandler;

	/**
	 * 进行数据的写入处理
	 */
	private MysqlPackageWriteInf writePkgHandler;

	/**
	 * 是否需要进行消息的解码
	 */
	private boolean isdecoder = true;

	/**
	 * 数据解析或者编码的部分数据
	 */
	private Map<String, Object> dataMap = new HashMap<>();
	
	/**
	 * 上一次最后验证的位置
	 */
	private int lastPosition;

	public MysqlContext() {

	}

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

	public void setWriteData(PackageHeader writeData) {
		this.writeData = writeData;
	}

	public ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(ByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}

	public MysqlPackageReadInf getReadPkgHandler() {
		return readPkgHandler;
	}

	public void setReadPkgHandler(MysqlPackageReadInf readPkgHandler) {
		this.readPkgHandler = readPkgHandler;
	}

	public MysqlPackageWriteInf getWritePkgHandler() {
		return writePkgHandler;
	}

	public void setWritePkgHandler(MysqlPackageWriteInf writePkgHandler) {
		this.writePkgHandler = writePkgHandler;
	}

	public boolean isIsdecoder() {
		return isdecoder;
	}

	public void setIsdecoder(boolean isdecoder) {
		this.isdecoder = isdecoder;
	}

	public void setWriteData(Object writeData) {
		this.writeData = writeData;
	}

	public Object getMapData(String key) {
		return dataMap.get(key);
	}

	public void setMapData(String key, Object value) {
		this.dataMap.put(key, value);
	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}
	

}
