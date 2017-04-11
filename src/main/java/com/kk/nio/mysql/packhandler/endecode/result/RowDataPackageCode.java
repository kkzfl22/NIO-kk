package com.kk.nio.mysql.packhandler.endecode.result;

import com.kk.nio.mysql.packhandler.MysqlDataPackageReadInf;
import com.kk.nio.mysql.packhandler.bean.pkg.PkgContext;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.RowDataPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;

/**
 * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量
 * 
 * [Field] 列信息（多个） [EOF] 列结束
 * 
 * 
 * [Row Data] 行数据（多个） [EOF] 数据结束
 */
public class RowDataPackageCode implements MysqlDataPackageReadInf<RowDataPackageBean> {

	@Override
	public RowDataPackageBean readPackage(PkgContext context) {

		RowDataPackageBean result = new RowDataPackageBean();

		MySQLMessage mm = new MySQLMessage(context.getBuffer());
		// 包大小
		result.setLength(mm.readUB3());
		// 序列值
		result.setSeq(mm.read());

		int columnNum = 0;
		// 填充列数据的值信息
		for (int i = 0; i < columnNum; i++) {
			result.getFieldValue().add(mm.readBytesWithLength());
		}

		return result;
	}

	@Override
	public boolean checkpackageOver(PkgContext context) {
		// TODO Auto-generated method stub
		return false;
	}

}
