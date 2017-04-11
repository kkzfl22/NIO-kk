package com.kk.nio.mysql.packhandler;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 进行mysql包的解析
 * @since 2017年4月11日 下午8:58:06
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlDataPackageReadInf<T extends PackageHeader> {

	/**
	 * 将协议数据转换为javaBean信息 方法描述
	 * 
	 * @param buffer
	 * @return
	 * @创建日期 2016年12月9日
	 */
	public T readPackage(ByteBuffer buffer);

	/**
	 * 进行包的完整性的检查
	 * 
	 * @param buffer
	 * @return
	 */
	public boolean checkpackageOver(ByteBuffer buffer);

	

}
