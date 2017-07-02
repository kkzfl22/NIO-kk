package com.kk.nio.mysqlproxy.util;

import java.nio.ByteBuffer;

/**
 * buffer公用类信息
 * 
 * @since 2017年6月20日 下午2:39:40
 * @version 0.0.1
 * @author liujun
 */
public class BufferTools {

	/**
	 * 进行包中的消息按长度读取
	 * 
	 * @param mapBuf
	 * @return
	 */
	public static ByteBuffer readLength(ByteBuffer mapBuf) {

		int currPosition = mapBuf.position();
		// HandshakeBean handshake = new HandshakeBean();
		int offset = currPosition;
		// 1获取长度信息, 占3位
		int length = getLength(mapBuf, offset);

		mapBuf.position(currPosition);
		ByteBuffer copyBuf = mapBuf.slice();
		copyBuf.limit(length);
		mapBuf.position(currPosition + length);

		return copyBuf;
	}

	/**
	 * 进行包中的消息按长度读取
	 * 
	 * @param mapBuf
	 *            原始buffer信息
	 * @return 截取读取
	 */
	public static ByteBuffer readLength(ByteBuffer mapBuf, int offset) {

		int currPosition = mapBuf.position();
		// 1获取长度信息, 占3位
		int length = getLength(mapBuf, offset);

		mapBuf.position(currPosition);
		ByteBuffer copyBuf = mapBuf.slice();
		copyBuf.limit(length);
		mapBuf.position(currPosition + length);

		return copyBuf;
	}

	/**
	 * 读取消息头长度，并跳过当前包信息
	 * 
	 * @param mapBuf
	 *            原始buffer信息
	 * @return 截取读取
	 */
	public static boolean readBufferSkip(ByteBuffer mapBuf) {

		int currPosition = mapBuf.position();
		int offset = currPosition;
		// 1获取长度信息, 占3位
		int length = getLength(mapBuf, offset);

		// 检查包长度，然后设置
		if (currPosition + length > mapBuf.limit()) {
			return false;
		} else {
			mapBuf.position(currPosition);
			mapBuf.position(currPosition + length);
			return true;
		}
	}
	
	
	/**
	 * 读取消息头长度，并跳过当前包信息
	 * 
	 * @param mapBuf
	 *            原始buffer信息
	 * @return 截取读取
	 */
	public static boolean readBufferSkip(ByteBuffer mapBuf,int offset) {
		
		int currPosition = mapBuf.position();
		// 1获取长度信息, 占3位
		int length = getLength(mapBuf, offset);
		
		// 检查包长度，然后设置
		if (currPosition + length > mapBuf.limit()) {
			return false;
		} else {
			mapBuf.position(currPosition);
			mapBuf.position(currPosition + length);
			return true;
		}
	}

	/**
	 * 进行byteBuffer包的长度检查
	 * 
	 * @param mapBuf
	 *            buffer信息
	 * @return true 长度检查正确 false 检查失败
	 */
	public static boolean checkLength(ByteBuffer mapBuf, int checkPosition) {

		// 1获取长度信息, 占3位
		int length = getLength(mapBuf, checkPosition);

		if (mapBuf.position() >= length) {
			return true;
		}
		return false;

	}

	/**
	 * 获取消息的长度
	 * 
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static int getLength(ByteBuffer buffer, int offset) {
		// mysql包头的长度
		int msyql_packetHeaderSize = 4;
		int length = buffer.get(offset) & 0xff;
		length |= (buffer.get(++offset) & 0xff) << 8;
		length |= (buffer.get(++offset) & 0xff) << 16;
		return length + msyql_packetHeaderSize;
	}

}
