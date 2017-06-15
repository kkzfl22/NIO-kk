package com.kk.nio.demo.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MysqlClientMain {

	public static void main(String[] args) throws IOException {

		ExecutorService execut = Executors.newFixedThreadPool(4);

		MysqlClientRectorNio[] reactor = new MysqlClientRectorNio[4];

		for (int i = 0; i < reactor.length; i++) {
			reactor[i] = new MysqlClientRectorNio(execut);
			reactor[i].start();
		}

		// 启动连接处理线程
		new Thread(new MysqlClientAcctor("localhost",3306, reactor)).start();
	}

}
