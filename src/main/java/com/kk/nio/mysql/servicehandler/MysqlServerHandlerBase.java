package com.kk.nio.mysql.servicehandler;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;

/**
 * 用来进行mysql的数据的包的处理
 * 
 * @since 2017年4月13日 下午8:16:29
 * @version 0.0.1
 * @author liujun
 */
public abstract class MysqlServerHandlerBase {

	/**
	 * 获取读取的package
	 * 
	 * @return
	 */
	public abstract MysqlPackageReadInf<? extends PackageHeader> getReadPackage();

	/**
	 * 获取写入的package
	 * 
	 * @return
	 */
	public abstract MysqlPackageWriteInf<? extends PackageHeader> getWritePackage();

}
