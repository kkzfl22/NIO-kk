package com.kk.nio.mysqlproxy.mysqlpkg.decode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.bean.pkg.resultset.EofPackageBean;

/**
 * 基本的消息的操作
 * 
 * @since 2017年4月28日 下午4:14:54
 * @version 0.0.1
 * @author liujun
 */
public class DeCodeBase {

	/**
	 * 进行包中的消息按长度读取
	 * 
	 * @param mapBuf
	 * @return
	 */
	protected ByteBuffer readLength(ByteBuffer mapBuf) {

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
	 * 获取消息的长度
	 * 
	 * @param buffer
	 * @param offset
	 * @return
	 */
	protected int getLength(ByteBuffer buffer, int offset) {
		// mysql包头的长度
		int msyql_packetHeaderSize = 4;
		int length = buffer.get(offset) & 0xff;
		length |= (buffer.get(++offset) & 0xff) << 8;
		length |= (buffer.get(++offset) & 0xff) << 16;
		return length + msyql_packetHeaderSize;
	}

	/**
	 * 检查是否为eof的标识 方法描述
	 * 
	 * @param copyBuf
	 * @return
	 * @创建日期 2016年12月14日
	 */
	protected boolean checkEofAackage(ByteBuffer copyBuf) {
		// 以当前的position坐标标，向后读取4个
		int currPositon = copyBuf.position();
		// 读取新坐标
		if (copyBuf.limit() >= currPositon + 4) {
			copyBuf.position(currPositon + 4);
			byte bufValue = copyBuf.get();
			if (bufValue == EofPackageBean.DEFVALUE) {
				// 重新设置为老坐标
				copyBuf.position(currPositon);
				return true;
			}

			copyBuf.position(currPositon);
		}

		return false;
	}

}
