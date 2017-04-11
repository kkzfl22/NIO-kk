package com.kk.nio.mysql.packhandler.bean.pkg;

import java.util.Arrays;

/**
 * 进行第一次的握手协议包
 * 
 * @since 2017年4月11日 下午8:37:20
 * @version 0.0.1
 * @author liujun
 */
public class QueryPackageBean extends PackageHeader {

	/**
	 * 查询标识符
	 * 
	 * @字段说明 flag
	 */
	private byte flag;

	/**
	 * 查询的字符信息
	 * 
	 * @字段说明 queryStr
	 */
	private byte[] queryStr;

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte[] getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(byte[] queryStr) {
		this.queryStr = queryStr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryPackageBean [flag=");
		builder.append(flag);
		builder.append(", queryStr=");
		builder.append(Arrays.toString(queryStr));
		builder.append("]");
		return builder.toString();
	}

}
