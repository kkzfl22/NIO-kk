package com.kk.nio.mysql.packhandler;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;
import com.kk.nio.mysql.packhandler.endecode.impl.AuthPackageCode;
import com.kk.nio.mysql.packhandler.endecode.impl.HandshakeCode;

/**
 * 进行程序包的处理
 * 
 * @since 2017年4月12日 下午3:11:09
 * @version 0.0.1
 * @author liujun
 */
public enum PkgProcessEnum {

	/**
	 * 握手消息,即服务端向客户端写入消息
	 */
	PKG_HANDSHAKE(1, new HandshakeCode(), null),

	/**
	 * 进行鉴权消息处理
	 */
	PKG_AUTH(2, null, new AuthPackageCode()),

	;

	/**
	 * 处理编号
	 */
	private int pkgNum;

	/**
	 * 进行mysql包的读取
	 */
	private MysqlPackageReadInf<? extends PackageHeader> pkgRead;

	/**
	 * 进行mysql包的写入
	 */
	private MysqlPackageWriteInf<? extends PackageHeader> pkgWrite;

	private PkgProcessEnum(int pkgNum, MysqlPackageReadInf<?> pkgRead, MysqlPackageWriteInf<?> pkgWrite) {
		this.pkgNum = pkgNum;
		this.pkgRead = pkgRead;
		this.pkgWrite = pkgWrite;
	}

	public int getPkgNum() {
		return pkgNum;
	}

	public void setPkgNum(int pkgNum) {
		this.pkgNum = pkgNum;
	}

	public MysqlPackageReadInf<?> getPkgRead() {
		return pkgRead;
	}

	public void setPkgRead(MysqlPackageReadInf<?> pkgRead) {
		this.pkgRead = pkgRead;
	}

	public MysqlPackageWriteInf<?> getPkgWrite() {
		return pkgWrite;
	}

	public void setPkgWrite(MysqlPackageWriteInf<?> pkgWrite) {
		this.pkgWrite = pkgWrite;
	}

}
