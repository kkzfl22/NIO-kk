package com.kk.nio.demo.midd.handler.blackmysqlconn.connstate;

/**
 * 进行连接的初始化创建
 * 
 * @since 2017年6月20日 下午2:25:34
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnUseState implements MysqlConnStateInf {

	@Override
	public void doRead(BlackMysqlConnStateContext mysqlConnContext) throws Exception {
		// 首先进行设置当前io处理为读取后端的数据
		boolean rsp = mysqlConnContext.getIostateContext().doRead();

		// 读取后端的数据成功之后
		if (rsp) {
		}

	}

	@Override
	public void doWrite(BlackMysqlConnStateContext mysqlConnContext) throws Exception {
	}

}
