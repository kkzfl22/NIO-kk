package com.kk.nio.mysql.packhandler.endecode.impl.resultset;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.FlowKeyEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ColumnPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.BaseCode;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束 [Row
 * Data] 行数据（多个） [EOF] 数据结束 源文件名：
 */
public class ColumnPackageCode extends BaseCode implements MysqlPackageReadInf {

	@Override
	public ColumnPackageBean readPackage(MysqlContext context) {

		ColumnPackageBean result = new ColumnPackageBean();

		// 读取消息头
		ByteBuffer buffColumn = this.readLength(context.getReadBuffer());

		MySQLMessage mm = new MySQLMessage(buffColumn);
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

		Boolean check = (Boolean) context.getMapData(FlowKeyEnum.QUERY_RSP_COLUMN_CHECK_FLAG.getKey());

		if (null == check || !check) {

			check = false;

			// 检查是否已经读取到eof包信息，如果检查到，则开始读取，否则继续读取流
			ByteBuffer readBuff = context.getReadBuffer();

			long oldPositon = readBuff.position();

			long currLimit = readBuff.limit();

			for (int i = (int) oldPositon; i < currLimit; i++) {
				// 当前检查到第一个列结束的eof包，则开始读取列的数据
				if (readBuff.get(i) == ((byte) 0xfe)) {
					check = true;
				}
			}

			context.setMapData(FlowKeyEnum.QUERY_RSP_COLUMN_CHECK_FLAG.getKey(), check);
		}

		return check;

	}

}
