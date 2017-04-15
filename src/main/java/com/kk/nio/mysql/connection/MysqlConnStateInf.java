package com.kk.nio.mysql.connection;

import java.io.IOException;

/**
 * 进行mysql的连接的状态处理
 * 
 * @since 2017年4月15日 下午12:10:54
 * @version 0.0.1
 * @author kk
 */
public interface MysqlConnStateInf {

	/**
	 * 进行mysql的连接状态处理
	 * 
	 * @param context 上下文对象信息
	 * @exception 异常信息
	 */
	public void stateProcess(MysqlConnContext context) throws IOException;

}
