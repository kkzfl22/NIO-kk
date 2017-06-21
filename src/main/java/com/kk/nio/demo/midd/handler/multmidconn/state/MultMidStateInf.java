package com.kk.nio.demo.midd.handler.multmidconn.state;

import com.kk.nio.demo.midd.handler.multmidconn.MultMidConnHandler;

/**
 * 进行中间件的多路状态处理接口
 * 
 * @since 2017年6月20日 下午1:35:50
 * @version 0.0.1
 * @author liujun
 */
public interface MultMidStateInf {

	/**
	 * 进行连接的数据的读取操作
	 * 
	 * @param multMidContext
	 *            进行io读取处理的上下文信息
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             异常信息
	 */
	public void doRead(MultMidConnHandler multMidContext) throws Exception;

	/**
	 * 进行连接的数据写入操作
	 * 
	 * @param multMidContext
	 *            进行io写入处理的上下文信息
	 * @return 返回是否结束标识 true 当前io操作结束 false 未结束
	 * @throws Exception
	 *             连接的异常信息
	 */
	public void doWrite(MultMidConnHandler multMidContext) throws Exception;
}
