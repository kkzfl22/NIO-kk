package com.kk.nio.mysqlproxy.mysqlpkg.decode;

import java.nio.ByteBuffer;

import com.kk.nio.mysqlproxy.mysqlpkg.bean.PkgHeader;

/**
 * 进行mysql包的解析
 * 
 * @since 2017年4月11日 下午8:58:06
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlPkgDecodeInf {

	/**
	 * 将协议数据转换为javaBean信息 方法描述
	 * 
	 * @param context
	 *            解析的上下文信息
	 * @param readPos
	 *            读取的指针位置
	 * @return
	 * @创建日期 2016年12月9日
	 */
	public PkgHeader readPackage(ByteBuffer context, int readPos);

}
