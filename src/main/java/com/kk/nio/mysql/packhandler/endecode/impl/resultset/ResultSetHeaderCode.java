package com.kk.nio.mysql.packhandler.endecode.impl.resultset;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ResultSetHanderBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.BaseCode;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * Result Set 消息体
 * 
 * 
 * [Result Set Header]列数量 当前消息体 [Field]列信息（多个） [EOF]列结束 [Row Data]行数据（多个）
 * [EOF]数据结束
 */
public class ResultSetHeaderCode extends BaseCode implements MysqlPackageReadInf {

	@Override
	public ResultSetHanderBean readPackage(MysqlContext context) {
		ResultSetHanderBean result = new ResultSetHanderBean();

		//仅在首次读取到的时候，需要进行flip方法
		if (context.getReadBuffer().limit() == context.getReadBuffer().capacity()) {
			// 将标识打为0，即为从0开始读取
			context.getReadBuffer().flip();
		}

		// 读取消息头
		ByteBuffer headBuff = this.readLength(context.getReadBuffer());

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

	@Override
	public boolean checkpackageOver(MysqlContext context) {

		// 首先只是返回的列数量，直接进行读取
		return true;
	}

}
