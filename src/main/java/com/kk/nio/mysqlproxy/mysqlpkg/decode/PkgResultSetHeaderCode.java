package com.kk.nio.mysqlproxy.mysqlpkg.decode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgResultSetHander;

/**
 * Result Set 消息体
 * 
 * 
 * [Result Set Header]列数量 当前消息体 [Field]列信息（多个） [EOF]列结束 [Row Data]行数据（多个）
 * [EOF]数据结束
 */
public class PkgResultSetHeaderCode extends DeCodeBase implements MysqlPkgDecodeInf {

	@Override
	public PkgResultSetHander readPackage(ByteBuffer context, int readPos) {
		PkgResultSetHander result = new PkgResultSetHander();

		int oldPos = context.position();

		context.position(readPos);

		// 读取消息头
		ByteBuffer headBuff = this.readLength(context);

		context.position(oldPos);

		MySQLMessage message = new MySQLMessage(headBuff);
		// 包大小
		result.setLength(message.readUB3());
		// 序列
		result.setSeq(message.read());
		// Field结构计数：
		result.setFieldCount((int) message.readLength());
		// 额外的信息
		if (message.hasRemaining()) {
			result.setOtherData(message.readLength());
		}

		return result;
	}

}
