package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
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
	PKG_WRITE_AUTH("pgk_write_auth", new AuthPackageCode()),

	;

	/**
	 * 处理编号
	 */
	private String pkgString;

	/**
	 * 进行mysql包的写入
	 */
	private MysqlPackageWriteInf<? extends PackageHeader> pkgWrite;

	private PkgWriteProcessEnum(String pkgString, MysqlPackageWriteInf<? extends PackageHeader> pkgWrite) {
		this.pkgString = pkgString;
		this.pkgWrite = pkgWrite;
	}

	public String getPkgString() {
		return pkgString;
	}

	public void setPkgString(String pkgString) {
		this.pkgString = pkgString;
	}

	public MysqlPackageWriteInf<? extends PackageHeader> getPkgWrite() {
		return pkgWrite;
	}

	public void setPkgWrite(MysqlPackageWriteInf<? extends PackageHeader> pkgWrite) {
		this.pkgWrite = pkgWrite;
	}

}
