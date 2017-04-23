package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.impl.ErrorPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.HandshakeCode;
import com.kk.nio.mysql.packhandler.endecode.impl.OkPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.resultset.ColumnPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.resultset.EofPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.resultset.ResultSetHeaderCode;
import com.kk.nio.mysql.packhandler.endecode.impl.resultset.RowDataPackageCode;

/**
 * 进行程序包的处理
 * 
 * @since 2017年4月12日 下午3:11:09
 * @version 0.0.1
 * @author liujun
 */
public enum PkgReadProcessEnum {

	/**
	 * 握手消息,即服务端向客户端写入消息
	 */
	PKG_READ_HANDSHAKE(new HandshakeCode()),

	/**
	 * 成功的报文处理
	 */
	PKG_READ_OK(new OkPackageCode()),

	/**
	 * 失败的报文处理
	 */
	PKG_READ_ERROR(new ErrorPackageCode()),

	/**
	 * 查询结果的响应头报文处理
	 */
	PKG_QUERY_RSP_HEADER(new ResultSetHeaderCode()),

	/**
	 * 查询结果的响应的列信息处理
	 */
	PKG_QUERY_REP_COLUMN(new ColumnPackageCode()),

	/**
	 * 进行eof消息的解码
	 */
	PKG_RSP_EOF(new EofPackageCode()),

	/**
	 * 数据进行按行读取
	 */
	PKG_RSP_QUERY_ROW_DATA(new RowDataPackageCode());

	/**
	 * 进行mysql包的读取
	 */
	private MysqlPackageReadInf pkgRead;

	private PkgReadProcessEnum(MysqlPackageReadInf pkgRead) {
		this.pkgRead = pkgRead;
	}

	public MysqlPackageReadInf getPkgRead() {
		return pkgRead;
	}

	public void setPkgRead(MysqlPackageReadInf pkgRead) {
		this.pkgRead = pkgRead;
	}

}
