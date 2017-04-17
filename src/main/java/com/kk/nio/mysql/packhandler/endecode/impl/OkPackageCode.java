package com.kk.nio.mysql.packhandler.endecode.impl;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.OkPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;

/**
 * 鉴权成功的消息报文
 * 
 * @since 2017年4月11日 下午8:48:29
 * @version 0.0.1
 * @author liujun
 */
public class OkPackageCode implements MysqlPackageReadInf {

	@Override
	public OkPackageBean readPackage(MysqlContext context) {

		OkPackageBean pkgBean = new OkPackageBean();

		MySQLMessage mm = new MySQLMessage(context.getReadBuffer());

		// 读取包大小
		pkgBean.setLength(mm.readUB3());
		// 读取响应包的 id
		pkgBean.setSeq(mm.read());
		// 响应结果
		pkgBean.setRspCode(mm.read());
		// 读取长度信息
		pkgBean.setAffectedRows(mm.readLength());
		// 插入的id
		pkgBean.setInsertId(mm.readLength());
		// 服务状态
		pkgBean.setServerStatus(mm.readUB2());

		pkgBean.setWarningCount(mm.readUB2());
		// 检查在当前位置和限制之间是否有元素。
		if (mm.hasRemaining()) {
			pkgBean.setMessage(mm.readBytesWithLength());
		}

		return pkgBean;
	}

	@Override
	public boolean checkpackageOver(MysqlContext context) {
		return true;
	}

}
