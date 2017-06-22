package com.kk.nio.mysqlproxy.proc;

import java.nio.channels.Selector;

/**
 * 进行mysql连接处理的基础类
 * 
 * @since 2017年6月22日 下午11:18:30
 * @version 0.0.1
 * @author liujun
 */
public class ConnectHandler {

	/**
	 * 绑定的select对象信息
	 */
	private Selector bindSelect;

	/**
	 * 中间件的连接信息
	 */
	private MidConnnectHandler midConn;

	/**
	 * mysql连接的信息
	 */
	private MysqlClientHandler mysqlClient;

}
