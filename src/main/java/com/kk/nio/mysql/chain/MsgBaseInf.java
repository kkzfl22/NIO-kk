package com.kk.nio.mysql.chain;

import java.io.IOException;

/**
 * 数据最基层的发送与接收操作
 * 
 * @since 2017年3月29日 下午5:08:56
 * @version 0.0.1
 * @author liujun
 */
public interface MsgBaseInf {

	/**
	 * 进行数据的写入
	 * 
	 * @param data
	 *            写的数据
	 * @param context
	 *            上下文对象信息
	 * @throws IOException
	 *             异常信息
	 */
	public void writeData(MysqlContext context) throws IOException;

	/**
	 * 进行数据的读取
	 * 
	 * @param context
	 *            上下文对象信息
	 * @return 当前读取数据的结果
	 * @throws IOException
	 */
	public void readData(MysqlContext context) throws IOException;

}
