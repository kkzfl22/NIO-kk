package com.kk.nio.mysql.packhandler.endecode.impl.resultset;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.EofPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束
 * [Row Data] 行数据（多个） [EOF] 数据结束
 */
public class EofPackageCode implements MysqlPackageReadInf {

	@Override
	public EofPackageBean readPackage(MysqlContext context) {

		EofPackageBean result = new EofPackageBean();

		MySQLMessage mm = new MySQLMessage(context.getReadBuffer());
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

	@Override
	public boolean checkpackageOver(MysqlContext context) {
		return false;
	}

}
