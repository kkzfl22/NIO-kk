package com.kk.nio.socket.multreactor.forkjoinchain;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChainMain {

	public static void main(String[] args) throws IOException {
		// 生成线程池
		ExecutorService executeService = Executors.newFixedThreadPool(8);

		// 获取计算机的核数
		int coreNum = Runtime.getRuntime().availableProcessors() / 2;
		// int coreNum = 1;
		ChainMultReactor[] multReactor = new ChainMultReactor[coreNum];

		for (int i = 0; i < multReactor.length; i++) {
			multReactor[i] = new ChainMultReactor(executeService);
			multReactor[i].start();
		}

		// 进行连接的修改
		Thread acceptor = new Thread(new ChainMultNioAcceptor(94, multReactor));
		acceptor.start();
	}

}
