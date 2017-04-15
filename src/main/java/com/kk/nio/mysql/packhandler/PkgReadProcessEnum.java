package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.impl.ErrorPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.HandshakeCode;
import com.kk.nio.mysql.packhandler.endecode.impl.OkPackageCode;

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
	PKG_READ_ERROR(new ErrorPackageCode())

	;

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
