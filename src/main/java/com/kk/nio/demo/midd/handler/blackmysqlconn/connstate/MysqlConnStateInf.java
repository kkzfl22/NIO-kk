package com.kk.nio.demo.midd.handler.blackmysqlconn.connstate;

/**
 * 进行mysql连接的状态处理
 * 
 * @since 2017年6月20日 下午1:35:50
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlConnStateInf {

	/**
	 * 进行连接的数据的读取操作
	 * 
	 * @param mysqlConnContext
	 *            连接的上下文信息
	 * @return true 读取结束，false 结束
	 * @throws Exception
	 *             异常信息
	 */
	public void doRead(BlackMysqlConnStateContext mysqlConnContext) throws Exception;

	/**
	 * 进行连接的数据写入操作
	 * 
	 * @param mysqlConnContext
	 *            连接的上下文信息
	 * @return true 写入结束，false 结束
	 * @throws Exception
	 *             连接的异常信息
	 */
	public void doWrite(BlackMysqlConnStateContext mysqlConnContext) throws Exception;
}
