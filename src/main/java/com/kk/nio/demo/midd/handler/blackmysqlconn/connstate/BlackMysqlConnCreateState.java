package com.kk.nio.demo.midd.handler.blackmysqlconn.connstate;

import com.kk.nio.demo.midd.handler.blackmysqlconn.MysqlIoStateEnum;

/**
 * 进行连接的初始化创建
 * 
 * @since 2017年6月20日 下午2:25:34
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnCreateState implements MysqlConnStateInf {

	@Override
	public void doRead(BlackMysqlConnStateContext mysqlConnContext) throws Exception {
		// 首先进行设置当前io处理为读取后端的数据
		 mysqlConnContext.getIostateContext().doRead();

	}

	@Override
	public void doWrite(BlackMysqlConnStateContext mysqlConnContext) throws Exception {
		boolean rsp = mysqlConnContext.getIostateContext().doWrite();

		// 当操作完成，需要将状态切换为连接结果获取
		if (rsp) {
			mysqlConnContext.getIostateContext().setCurrState(MysqlIoStateEnum.IOSTATE_AUTHRSP.getIoState());
		}
	}

}
