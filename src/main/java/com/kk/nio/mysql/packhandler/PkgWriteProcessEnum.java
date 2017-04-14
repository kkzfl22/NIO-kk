package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;
import com.kk.nio.mysql.packhandler.endecode.impl.AuthPackageCode;

/**
 * 进行程序包的处理
 * 
 * @since 2017年4月12日 下午3:11:09
 * @version 0.0.1
 * @author liujun
 */
public enum PkgWriteProcessEnum {

	/**
	 * 鉴权消息，客户端向服务端发起
	 */
	PKG_WRITE_AUTH(new AuthPackageCode()),

	;

	/**
	 * 进行mysql包的写入
	 */
	private MysqlPackageWriteInf pkgWrite;

	private PkgWriteProcessEnum(MysqlPackageWriteInf pkgWrite) {
		this.pkgWrite = pkgWrite;
	}

	public MysqlPackageWriteInf getPkgWrite() {
		return pkgWrite;
	}

	public void setPkgWrite(MysqlPackageWriteInf pkgWrite) {
		this.pkgWrite = pkgWrite;
	}

}
