package com.kk.nio.mysqlproxy.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import com.kk.nio.mysqlproxy.proc.ConnectHandler;

public class MultRector extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 线程池对象
	 */
	private ExecutorService executePool;
	
	/**
	 * 注册队列信息
	 */
	private ConcurrentLinkedQueue<ConnectHandler> queue = new ConcurrentLinkedQueue<>();

	public MultRector(ExecutorService executePool) throws IOException {
		super();
		select = Selector.open();
		this.executePool = executePool;
	}
	
	public void registHandler(ConnectHandler conn)
	{
		queue.offer(conn);
	}
	
	public void eventProc()
	{
		ConnectHandler handler = null;
		while((handler = queue.poll()) != null)
		{
		}
	}

	@Override
	public void run() {

		Set<SelectionKey> selKeySet = null;
		while (true) {
			try {
				int selKey = select.select(200);

				if (selKey > 0) {
					selKeySet = select.selectedKeys();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null != selKeySet) {
				for (SelectionKey selectionKey : selKeySet) {
					Runnable run = (Runnable) selectionKey.attachment();

					executePool.submit(run);
				}

				selKeySet.clear();
			}
		}
	}

}
