package com.kk.nio.demo.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoNioMain {

	public static void main(String[] args) throws IOException {

		ExecutorService execut = Executors.newFixedThreadPool(4);

		RectorNio[] reactor = new RectorNio[4];

		for (int i = 0; i < reactor.length; i++) {
			reactor[i] = new RectorNio(execut);
			reactor[i].start();
		}

		// 启动连接处理线程
		new Thread(new NioAcctor(210, reactor)).start();
	}

}
