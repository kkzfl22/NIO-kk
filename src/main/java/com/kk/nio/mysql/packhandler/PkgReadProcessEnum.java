package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.impl.HandshakeCode;

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
	PKG_READ_HANDSHAKE("pkg_shandshark", new HandshakeCode()),

	;

	/**
	 * 处理编号
	 */
	private String pkgStr;

	/**
	 * 进行mysql包的读取
	 */
	private MysqlPackageReadInf<? extends PackageHeader> pkgRead;

	private PkgReadProcessEnum(String pkgStr, MysqlPackageReadInf<? extends PackageHeader> pkgRead) {
		this.pkgStr = pkgStr;
		this.pkgRead = pkgRead;
	}

	public String getPkgStr() {
		return pkgStr;
	}

	public void setPkgStr(String pkgStr) {
		this.pkgStr = pkgStr;
	}

	public MysqlPackageReadInf<? extends PackageHeader> getPkgRead() {
		return pkgRead;
	}

	public void setPkgRead(MysqlPackageReadInf<? extends PackageHeader> pkgRead) {
		this.pkgRead = pkgRead;
	}

}
