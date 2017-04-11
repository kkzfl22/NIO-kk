package com.kk.nio.mysql.packhandler.endecode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.MysqlDataPackageWriteInf;
import com.kk.nio.mysql.packhandler.bean.pkg.QueryPackageBean;
import com.kk.nio.mysql.packhandler.common.BufferUtil;

/**
 * 进宪查询消息 的解码
 * 
 * @since 2017年4月11日 下午8:51:25
 * @version 0.0.1
 * @author liujun
 */
public class QueryPackageCode implements MysqlDataPackageWriteInf<QueryPackageBean> {

	@Override
	public int getpackageSize(QueryPackageBean pkgBean) {
		int size = 0;

		// 1,第1位，发送包的类型信息
		size = size + 1;
		// 第二位为查询的SQL语句
		size = size + (pkgBean.getQueryStr() == null ? 1 : pkgBean.getQueryStr().length);

		return size;
	}

	@Override
	public ByteBuffer PackageToBuffer(QueryPackageBean pkgBean) {

		int pkgSize = this.getpackageSize(pkgBean);

		ByteBuffer buffer = ByteBuffer.allocate(pkgSize + 4);

		// 进行包大小的数据写入
		BufferUtil.writeUB3(buffer, pkgSize);
		// 写入包在id
		buffer.put(pkgBean.getSeq());
		// 进行查询操作
		buffer.put(pkgBean.getFlag());
		// 写入查询字符
		buffer.put(pkgBean.getQueryStr());

		return buffer;
	}

}
