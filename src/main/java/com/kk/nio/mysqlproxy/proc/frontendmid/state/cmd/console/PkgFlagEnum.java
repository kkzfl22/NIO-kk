package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console;

/**
 * 进行包的标识检查
 * 
 * @since 2017年7月2日 下午3:43:55
 * @version 0.0.1
 * @author liujun
 */
public enum PkgFlagEnum {

	/**
	 * eof包标识
	 */
	PKG_EOF_FLAG((byte) 0xfe),

	/**
	 * ok包标识
	 */
	PKG_OK_FLAG((byte) 0),
	
	
	/**
	 * 错误包标识
	 */
	PKG_ERROR_FLAG((byte) 0xff);
	;

	/**
	 * 包标识信息
	 */
	private byte pkgFlag;


	private PkgFlagEnum(byte pkgFlag) {
		this.pkgFlag = pkgFlag;
	}

	public byte getPkgFlag() {
		return pkgFlag;
	}

	public void setPkgFlag(byte pkgFlag) {
		this.pkgFlag = pkgFlag;
	}


}
