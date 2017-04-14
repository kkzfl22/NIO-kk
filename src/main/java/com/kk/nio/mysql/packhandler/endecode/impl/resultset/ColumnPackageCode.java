package com.kk.nio.mysql.packhandler.endecode.impl.resultset;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ColumnPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束 [Row
 * Data] 行数据（多个） [EOF] 数据结束 源文件名：
 */
public class ColumnPackageCode implements MysqlPackageReadInf {

	@Override
	public ColumnPackageBean readPackage(MysqlContext context) {

		ColumnPackageBean result = new ColumnPackageBean();

		MySQLMessage mm = new MySQLMessage(context.getReadBuffer());
		// 包大小
		result.setLength(mm.readUB3());
		// 序列值
		result.setSeq(mm.read());
		// 当前目录名称
		result.setDirName(mm.readBytesWithLength());
		// 数据库名称
		result.setDataBaseName(mm.readBytesWithLength());
		// 数据表名称
		result.setTableAsName(mm.readBytesWithLength());
		// 数据表原始名称：数据表的原始名称（AS之前的名称）。
		result.setTableBeforeName(mm.readBytesWithLength());
		// 列（字段）名称：列（字段）的别名（AS之后的名称）。
		result.setColumnAsName(mm.readBytesWithLength());
		// 列（字段）原始名称：列（字段）的原始名称（AS之前的名称）。
		result.setColumnBeforeName(mm.readBytesWithLength());
		// 跳过填充值
		mm.move(1);
		// 字符编码：列（字段）的字符编码值。
		result.setCharSet(mm.readUB2());
		// 列（字段）长度：列（字段）的长度值，真实长度可能小于该值，例如VARCHAR(2)类型的字段实际只能存储1个字符。
		result.setColumnLength(mm.readUB4());
		// 列（字段）类型：列（字段）的类型值
		result.setColumnType(mm.read() & 0xff);
		// 列（字段）标志
		result.setColumnFlag(mm.readUB2());
		// 数值精度
		result.setDecimalNum(mm.read());
		// 跳过填充值
		mm.move(2);
		// 默认值
		if (mm.hasRemaining()) {
			result.setDefValue(mm.readBytesWithLength());
		}

		return result;
	}

	@Override
	public boolean checkpackageOver(MysqlContext context) {
		return false;
	}

}
