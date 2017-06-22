package com.kk.nio.demo.midd.handler.blackmysqlconn.connstate;

import com.kk.nio.demo.midd.handler.blackmysqlconn.BlackmysqlConnHandler;

/**
 * 进行连接的初始化创建
 * 
 * @since 2017年6月20日 下午2:25:34
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnUseState implements MysqlConnStateInf {

	@Override
	public void doRead(BlackmysqlConnHandler mysqlConnContext) throws Exception {
		// 首先进行设置当前io处理为读取后端的数据
		mysqlConnContext.getIostateContext().doRead();

	}

	@Override
	public void doWrite(BlackmysqlConnHandler mysqlConnContext) throws Exception {
	}

}
