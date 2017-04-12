package com.kk.nio.mysql.packhandler.endecode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 进行mysql数据的写入操作
 * 
 * @since 2017年4月11日 下午8:53:22
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlPackageWriteInf<T extends PackageHeader> {

	/**
	 * 得到数据写入的package 方法描述
	 * 
	 * @param buffer
	 * @创建日期 2016年12月9日
	 */
	public ByteBuffer packageToBuffer(T msg);

	/**
	 * 获取数据包大小 方法描述
	 * 
	 * @return
	 * @创建日期 2016年12月9日
	 */
	public int getpackageSize(T bean);

}
