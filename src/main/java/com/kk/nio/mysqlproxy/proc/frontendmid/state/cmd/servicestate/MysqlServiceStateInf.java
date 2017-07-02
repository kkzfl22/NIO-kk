package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate;

import java.io.IOException;

/**
 * 进行业务状态的接口定义
 * 
 * @since 2017年7月2日 上午10:50:36
 * @version 0.0.1
 * @author liujun
 */
public interface MysqlServiceStateInf {

	/**
	 * 进行业务的执行操作
	 * 
	 * @param mysqlService
	 *            连接信息
	 * @throws IOException
	 *             中间的数据异常问题
	 */
	public void serviceDoInvoke(MysqlServiceContext mysqlService) throws IOException;

}
