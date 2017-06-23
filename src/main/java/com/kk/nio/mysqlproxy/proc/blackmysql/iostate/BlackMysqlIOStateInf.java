package com.kk.nio.mysqlproxy.proc.blackmysql.iostate;

import java.io.IOException;

/**
 * 进行mysql后端的io的状态处理
 * 
 * @since 2017年6月23日 下午1:59:13
 * @version 0.0.1
 * @author liujun
 */
public interface BlackMysqlIOStateInf {

	/**
	 * 进行读取操作
	 * 
	 * @param handler
	 *            连接信息
	 * @exception IOException
	 *                数据异常问题
	 */
	public void doRead(BlackMysqlIOStateContext handler) throws IOException;

	/**
	 * 进行写入操作
	 * 
	 * @param handler
	 *            连接信息
	 */
	public void doWrite(BlackMysqlIOStateContext handler) throws IOException;

}
