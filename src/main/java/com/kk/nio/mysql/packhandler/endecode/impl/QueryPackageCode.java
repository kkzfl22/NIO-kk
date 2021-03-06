package com.kk.nio.mysql.packhandler.endecode.impl;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.bean.pkg.QueryPackageBean;
import com.kk.nio.mysql.packhandler.common.BufferUtil;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;

/**
 * 进宪查询消息 的解码
 * 
 * @since 2017年4月11日 下午8:51:25
 * @version 0.0.1
 * @author liujun
 */
public class QueryPackageCode implements MysqlPackageWriteInf {

	@Override
	public int getpackageSize(PackageHeader pkgParam) {

		QueryPackageBean pkgBean = (QueryPackageBean) pkgParam;

		int size = 0;

		// 1,第1位，发送包的类型信息
		size = size + 1;
		// 第二位为查询的SQL语句
		size = size + (pkgBean.getQueryStr() == null ? 1 : pkgBean.getQueryStr().length);

		return size;
	}

	@Override
	public void packageToBuffer(MysqlContext context) {

		QueryPackageBean pkgBean = (QueryPackageBean) context.getWriteData();

		int pkgSize = this.getpackageSize(pkgBean);

		ByteBuffer buffer = context.getWriteBuffer();

		// 进行包大小的数据写入
		BufferUtil.writeUB3(buffer, pkgSize);
		// 写入包在id
		buffer.put(pkgBean.getSeq());
		// 进行查询操作
		buffer.put(pkgBean.getFlag());
		// 写入查询字符
		buffer.put(pkgBean.getQueryStr());

	}

}
