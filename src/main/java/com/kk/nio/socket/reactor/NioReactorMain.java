package com.kk.nio.socket.reactor;

public class NioReactorMain {

	public static void main(String[] args) {
		Thread thr = new Thread(new ReactorNio(93));
		thr.start();
	}

}
