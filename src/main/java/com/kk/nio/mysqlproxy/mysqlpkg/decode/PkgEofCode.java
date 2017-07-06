package com.kk.nio.mysqlproxy.mysqlpkg.decode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgEofBean;

/**
 * * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束
 * [Row Data] 行数据（多个） [EOF] 数据结束
 */
public class PkgEofCode extends DeCodeBase implements MysqlPkgDecodeInf {

	@Override
	public PkgEofBean readPackage(ByteBuffer buffer, int readPos) {

		PkgEofBean result = new PkgEofBean();

		buffer.position(readPos);

		// eof消息
		ByteBuffer buffEof = this.readLength(buffer);

		MySQLMessage mm = new MySQLMessage(buffEof);
		// 包大小
		result.setLength(mm.readUB3());
		// 序号
		result.setSeq(mm.read());
		// EOF值
		result.setEofValue(mm.read());
		// 告警计数
		result.setWarnCount(mm.readUB2());
		// 状态标识
		result.setStatusFlag(mm.readUB2());

		return result;
	}

}
