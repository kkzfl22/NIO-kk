package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;

/**
 * 进行消息处理的状态接口
 * 
 * @since 2017年4月14日 下午2:41:40
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlStateInf {

	/**
	 * 设置通道中的包消息的处理程序
	 * 
	 * @return
	 */
	public void setRWPkgHandler(MysqlStateContext mysqlContext);

	/**
	 * 进行数据读取
	 * 
	 * @param context
	 *            数据操作上下文对象
	 * @throws IOException
	 *             异常
	 */
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException;

	/**
	 * 进行数据写入
	 * 
	 * @param context
	 *            数据操作上下文对象
	 * @throws IOException
	 *             异常
	 */
	public void pkgWrite(MysqlStateContext mysqlContext) throws IOException;

}
