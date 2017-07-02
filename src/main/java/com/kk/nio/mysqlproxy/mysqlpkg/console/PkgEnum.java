package com.kk.nio.mysqlproxy.mysqlpkg.console;

import com.kk.nio.mysqlproxy.mysqlpkg.decode.MysqlPkgDecodeInf;
import com.kk.nio.mysqlproxy.mysqlpkg.decode.PkgEofCode;
import com.kk.nio.mysqlproxy.mysqlpkg.decode.PkgOkCode;
import com.kk.nio.mysqlproxy.mysqlpkg.decode.PkgResultSetHeaderCode;

public enum PkgEnum {

	/**
	 * 进行eof包的解析操作
	 */
	PKG_EOF(new PkgEofCode()),
	
	/**
	 * 进行ok包的解析
	 */
	PKG_OK(new PkgOkCode()),
	
	
	/**
	 * 进行响应包头列的解析
	 */
	PKG_RESULTSET_HEAD(new PkgResultSetHeaderCode());

	/**
	 * 包解析
	 */
	private MysqlPkgDecodeInf pkgDecode;

	private PkgEnum(MysqlPkgDecodeInf pkgDecode) {
		this.pkgDecode = pkgDecode;
	}

	public MysqlPkgDecodeInf getPkgDecode() {
		return pkgDecode;
	}

	public void setPkgDecode(MysqlPkgDecodeInf pkgDecode) {
		this.pkgDecode = pkgDecode;
	}

}
