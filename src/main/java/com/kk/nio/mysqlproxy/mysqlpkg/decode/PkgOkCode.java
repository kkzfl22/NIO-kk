package com.kk.nio.mysqlproxy.mysqlpkg.decode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgOkBean;

/**
 * 鉴权成功的消息报文
 * 
 * @since 2017年4月11日 下午8:48:29
 * @version 0.0.1
 * @author liujun
 */
public class PkgOkCode extends DeCodeBase implements MysqlPkgDecodeInf {

	@Override
	public PkgOkBean readPackage(ByteBuffer context, int readPos) {

		PkgOkBean pkgBean = new PkgOkBean();

		// ok包消息
		ByteBuffer buffOk = this.readLength(context);

		MySQLMessage mm = new MySQLMessage(buffOk);

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

}
