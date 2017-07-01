package com.kk.nio.mysqlproxy.proc.mysqlpkg;

/**
 * 包信息
 * @since 2017年6月24日 下午6:15:00
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlPkgEnum {
	
	/**
	 * 成功标识
	 */
	PKG_OK_RSP(0);

	/**
	 * 包类型信息
	 */
	private int pkgType;

	private MysqlPkgEnum(int pkgType) {
		this.pkgType = pkgType;
	}

	public int getPkgType() {
		return pkgType;
	}

	public void setPkgType(int pkgType) {
		this.pkgType = pkgType;
	}
	
	/**
	 * 进行mysql包类型的获取
	 * @param code 包类型信息
	 * @return 结果枚举
	 */
	public static MysqlPkgEnum getPkgType(int code)
	{
		MysqlPkgEnum[] values = values();
		
		for (MysqlPkgEnum mysqlPkgEnum : values) {
			if(code == mysqlPkgEnum.getPkgType())
			{
				return mysqlPkgEnum;
			}
		}
		
		return null;
	}

}
