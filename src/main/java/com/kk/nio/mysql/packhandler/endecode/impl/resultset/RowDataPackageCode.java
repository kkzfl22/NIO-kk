package com.kk.nio.mysql.packhandler.endecode.impl.resultset;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.FlowKeyEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.RowDataPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.BaseCode;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量
 * 
 * [Field] 列信息（多个） [EOF] 列结束
 * 
 * 
 * [Row Data] 行数据（多个） [EOF] 数据结束
 */
public class RowDataPackageCode extends BaseCode implements MysqlPackageReadInf {

	@Override
	public RowDataPackageBean readPackage(MysqlContext context) {

		RowDataPackageBean result = null;

		// 读取消息头
		ByteBuffer buffRow = this.readLength(context.getReadBuffer());

		// 如果当前不为eof消息结束，则进行读取
		if (!checkEofAackage(buffRow)) {

			int columnNum = (int) context.getMapData(FlowKeyEnum.QUERY_RSP_HEADER_COUNT.getKey());

			// 提取列数量
			result = new RowDataPackageBean(columnNum);

			MySQLMessage mm = new MySQLMessage(buffRow);

			try {

				// 包大小
				result.setLength(mm.readUB3());
				// 序列值
				result.setSeq(mm.read());

				// 从前面的结果中提取已经得到的查询列的信息

				// 填充列数据的值信息
				for (int i = 0; i < columnNum; i++) {
					result.getFieldValue().add(mm.readBytesWithLength());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public boolean checkpackageOver(MysqlContext context) {

		// 检查是否已经读取到eof包信息，如果检查到，则开始读取，否则继续读取流
		ByteBuffer readBuff = context.getReadBuffer();

		long oldPositon = 0;

		// 检查上一次的position是否为0，说明是第一次，则需要进行记录以检查 ,以当前的position位置进行记录
		if (context.getLastPosition() == 0) {
			oldPositon = readBuff.position();
		} else {
			oldPositon = context.getLastPosition();
		}

		int currLimit = readBuff.limit();

		for (int i = (int) oldPositon; i < currLimit; i++) {
			// 当前检查到第一个列结束的eof包，则开始读取列的数据
			if (readBuff.get(i) == ((byte) 0xfe)) {
				return true;
			}
		}

		// 完成后，设置当前检查过的值
		context.setLastPosition(currLimit - 1);

		return true;
	}

}
