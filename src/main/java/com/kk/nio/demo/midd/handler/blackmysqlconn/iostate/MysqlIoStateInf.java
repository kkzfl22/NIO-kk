package com.kk.nio.demo.midd.handler.blackmysqlconn.iostate;

/**
 * 进行mysql连接的状态处理
 * 
 * @since 2017年6月20日 下午1:35:50
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlIoStateInf {

	/**
	 * 进行连接的数据的读取操作
	 * 
	 * @param iostateContext
	 *            进行io读取处理的上下文信息
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             异常信息
	 */
	public void doRead(BlackMysqlIostateContext iostateContext) throws Exception;

	/**
	 * 进行连接的数据写入操作
	 * 
	 * @param iostateContext
	 *            进行io写入处理的上下文信息
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             连接的异常信息
	 */
	public void doWrite(BlackMysqlIostateContext iostateContext) throws Exception;
}
