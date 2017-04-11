package com.kk.nio.mysql.packhandler.endecode.result;

import com.kk.nio.mysql.packhandler.MysqlDataPackageReadInf;
import com.kk.nio.mysql.packhandler.bean.pkg.PkgContext;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ResultSetHanderBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;

/**
 * Result Set 消息体
 * 
 * 
 * [Result Set Header]列数量 当前消息体 [Field]列信息（多个） [EOF]列结束 [Row Data]行数据（多个）
 * [EOF]数据结束
 */
public class ResultSetHanderCode implements MysqlDataPackageReadInf<ResultSetHanderBean> {

	@Override
	public ResultSetHanderBean readPackage(PkgContext context) {
		ResultSetHanderBean result = new ResultSetHanderBean();

		MySQLMessage message = new MySQLMessage(context.getBuffer());
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
	public boolean checkpackageOver(PkgContext context) {
		return false;
	}

}
