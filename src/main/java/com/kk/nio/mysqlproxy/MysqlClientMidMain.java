package com.kk.nio.mysqlproxy;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kk.nio.mysql.bean.MysqlConnBean;
import com.kk.nio.mysql.console.PropertiesKeyEnum;
import com.kk.nio.mysql.util.PropertiesUtils;
import com.kk.nio.mysqlproxy.mysql.NioAcceptor;
import com.kk.nio.mysqlproxy.mysql.MysqlClientMultReactor;

public class MysqlClientMidMain {

	public static void main(String[] args) throws IOException {
		// 生成线程池
		ExecutorService executeService = Executors.newFixedThreadPool(1);

		// 获取计算机的核数
		//int coreNum = Runtime.getRuntime().availableProcessors() / 2;
		int coreNum = 1;
		// int coreNum = 1;
		MysqlClientMultReactor[] multReactor = new MysqlClientMultReactor[coreNum];

		for (int i = 0; i < multReactor.length; i++) {
			multReactor[i] = new MysqlClientMultReactor(executeService);
			multReactor[i].start();
		}

		// 进行连接的修改
		Thread acceptor = new Thread(new NioAcceptor(multReactor));
		acceptor.start();

		MysqlConnBean conn = new MysqlConnBean();

		conn.setServerIP(PropertiesUtils.getInstance().getValue(PropertiesKeyEnum.MYSQL_SERVER_IP));
		conn.setPort(Integer.parseInt(PropertiesUtils.getInstance().getValue(PropertiesKeyEnum.MYSQL_SERVER_PORT)));

		// 进行连接的注册
		NioAcceptor.AddConnection(conn);
	}

}
