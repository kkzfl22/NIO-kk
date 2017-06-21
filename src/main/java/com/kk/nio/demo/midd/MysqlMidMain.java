package com.kk.nio.demo.midd;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MysqlMidMain {

	public static void main(String[] args) throws IOException {

		ExecutorService execut = Executors.newFixedThreadPool(2);

		MysqlMidRectorNio[] reactor = new MysqlMidRectorNio[4];

		for (int i = 0; i < reactor.length; i++) {
			reactor[i] = new MysqlMidRectorNio(execut);
			reactor[i].start();
		}

		MysqlMidAcctor midAcc = new MysqlMidAcctor(reactor);
		// 启动连接处理线程
		new Thread(midAcc).start();

		midAcc.regiectMultConn(9002);
	}

}
