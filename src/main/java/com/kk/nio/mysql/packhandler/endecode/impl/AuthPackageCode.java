package com.kk.nio.mysql.packhandler.endecode.impl;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.AuthPackageBean;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.common.BufferUtil;
import com.kk.nio.mysql.packhandler.console.Capabilities;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;

/**
 * 进行握手的报文
 * 
 * @since 2017年4月11日 下午8:14:08
 * @version 0.0.1
 * @author liujun
 */
public class AuthPackageCode implements MysqlPackageWriteInf {

	/**
	 * 进行客户端的权能标识 方法描述
	 * 
	 * @return
	 * @创建日期 2016年12月13日
	 */
	private static long initClientFlags() {
		int flag = 0;
		flag |= Capabilities.CLIENT_LONG_PASSWORD;
		flag |= Capabilities.CLIENT_FOUND_ROWS;
		flag |= Capabilities.CLIENT_LONG_FLAG;
		flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
		// flag |= Capabilities.CLIENT_NO_SCHEMA;
		boolean usingCompress = false;
		if (usingCompress) {
			flag |= Capabilities.CLIENT_COMPRESS;
		}
		flag |= Capabilities.CLIENT_ODBC;
		flag |= Capabilities.CLIENT_LOCAL_FILES;
		flag |= Capabilities.CLIENT_IGNORE_SPACE;
		flag |= Capabilities.CLIENT_PROTOCOL_41;
		flag |= Capabilities.CLIENT_INTERACTIVE;
		// flag |= Capabilities.CLIENT_SSL;
		flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
		flag |= Capabilities.CLIENT_TRANSACTIONS;
		// flag |= Capabilities.CLIENT_RESERVED;
		flag |= Capabilities.CLIENT_SECURE_CONNECTION;
		// client extension
		flag |= Capabilities.CLIENT_MULTI_STATEMENTS;
		flag |= Capabilities.CLIENT_MULTI_RESULTS;
		return flag;
	}

	@Override
	public int getpackageSize(PackageHeader param) {

		AuthPackageBean bean = (AuthPackageBean) param;

		int size = 0;
		// 添加，客户端标识
		size = size + 4;
		// 包大小
		size = size + 4;
		// 1位字符编码
		size = size + 1;
		// 填充值
		size = size + 23;
		// 用户名
		size = size + ((bean.getUserName() == null) ? 1 : bean.getUserName().length() + 1);
		// 添加密码
		size = size + ((bean.getPasswd() == null) ? 1 : BufferUtil.getLength(bean.getPasswd()));
		// 数据库长度
		size = size + ((bean.getDataBase() == null) ? 1 : bean.getDataBase().length() + 1);
		return size;
	}

	@Override
	public void packageToBuffer(MysqlContext context) {
		AuthPackageBean packBean = (AuthPackageBean) context.getWriteData();

		// 设置当前的包顺序为1
		packBean.setSeq(packBean.getSeq());
		// 设置客户端的标识
		packBean.setClientFlag(initClientFlags());

		// 读取包大小
		int pkgSize = this.getpackageSize(packBean);
		// 4,字节为包头的大小
		ByteBuffer resultByte = context.getWriteBuffer();

		// 进行包大小的数据写入
		BufferUtil.writeUB3(resultByte, pkgSize);
		// 写入包在id
		resultByte.put(packBean.getSeq());
		// 写入客户端标识
		BufferUtil.writeUB4(resultByte, packBean.getClientFlag());
		// 写入最大包
		BufferUtil.writeUB4(resultByte, packBean.getMaxPackageLong());
		// 写入字符编码
		resultByte.put((byte) packBean.getCharSetIndex());
		// 写入填充值
		resultByte.put(packBean.getFILLER());
		// 写入用户名
		if (null == packBean.getUserName()) {
			resultByte.put((byte) 0);
		} else {
			byte[] userData = packBean.getUserName().getBytes();
			BufferUtil.writeWithNull(resultByte, userData);
		}

		// 写入密码
		if (null == packBean.getPasswd()) {
			resultByte.put((byte) 0);
		} else {
			BufferUtil.writeWithLength(resultByte, packBean.getPasswd());
		}

		// 写入dataBase
		if (null == packBean.getDataBase()) {
			resultByte.put((byte) 0);
		} else {
			byte[] dataBaseBytes = packBean.getDataBase().getBytes();
			BufferUtil.writeWithNull(resultByte, dataBaseBytes);
		}

	}

}
