package com.kk.nio.mysqlproxy.proc.frontendmid;

import java.io.IOException;

/**
 * 进行前端接收数据的IO的状态处理
 * 
 * @since 2017年6月23日 下午1:59:13
 * @version 0.0.1
 * @author liujun
 */
public interface FrontendIOHandStateInf {

	/**
	 * 进行读取操作
	 * 
	 * @param handler
	 *            连接信息
	 * @throws IOException
	 *             中间的数据异常问题
	 */
	public void doRead(FrontendMidConnnectHandler handler) throws IOException;

	/**
	 * 进行写入操作
	 * 
	 * @param handler
	 *            连接信息
	 * @throws IOException
	 *             数据异常问题
	 */
	public void doWrite(FrontendMidConnnectHandler handler) throws IOException;

}
