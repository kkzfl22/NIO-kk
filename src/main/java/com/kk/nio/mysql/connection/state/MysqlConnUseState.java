package com.kk.nio.mysql.connection.state;

import java.io.IOException;

import com.kk.nio.mysql.connection.MysqlConnContext;
import com.kk.nio.mysql.connection.MysqlConnStateInf;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.QueryPackageBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;

/**
 * 当前mysql的状态为使用中
 * 
 * @since 2017年4月15日 下午12:17:27
 * @version 0.0.1
 * @author kk
 */
public class MysqlConnUseState implements MysqlConnStateInf {

	/**
	 * 上下文对象
	 */
	private MysqlStateContext mysqlContext = new MysqlStateContext();

	@Override
	public void stateReadProcess(MysqlConnContext context) throws IOException {

	}

	@Override
	public void stateWriteProcess(MysqlConnContext context) throws IOException {
		// 设置mysql的上下文处理对象
		mysqlContext.setContext(context.getContext());
		
		//进行当前的mysql查询的请求
		QueryPackageBean queryReq = new QueryPackageBean();
		

		// 继续进行数据的写入
		mysqlContext.pkgWrite();

	}

}
