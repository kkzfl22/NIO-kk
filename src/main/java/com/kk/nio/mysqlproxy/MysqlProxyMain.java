package com.kk.nio.mysqlproxy;

import java.io.IOException;

import com.kk.nio.mysqlproxy.nio.MultAcceptor;
import com.kk.nio.mysqlproxy.nio.MultRector;

public class MysqlProxyMain {

	public static void main(String[] args) throws IOException {

		MultRector[] reactor = new MultRector[4];

		for (int i = 0; i < reactor.length; i++) {
			reactor[i] = new MultRector();
			reactor[i].start();
		}

		MultAcceptor midAcc = new MultAcceptor(reactor);
		// 启动连接处理线程
		new Thread(midAcc).start();

		midAcc.registMidConn(9003);

	}

}
