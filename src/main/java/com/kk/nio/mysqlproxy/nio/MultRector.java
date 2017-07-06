package com.kk.nio.mysqlproxy.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.kk.nio.mysqlproxy.proc.ConnectHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;
import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;

public class MultRector extends Thread {

	/**
	 * 选择器对象
	 */
	private Selector select;

	/**
	 * 注册队列信息
	 */
	private ConcurrentLinkedQueue<ConnectHandler> queue = new ConcurrentLinkedQueue<>();

	public MultRector() throws IOException {
		super();
		select = Selector.open();
	}

	/**
	 * 进行中间件处理类的注册
	 * 
	 * @param conn
	 */
	public void registMidHandler(FrontendMidConnnectHandler conn) {
		queue.offer(conn);
		// 放入对象之后立马唤醒不在等待
		this.select.wakeup();
	}

	/**
	 * 进行mysql连接的读写事件注册
	 * 
	 * @param conn
	 */
	public void registMysqlHandler(BlackMysqlClientHandler conn) {
		queue.offer(conn);
		// 放入对象之后立马唤醒不在等待
		this.select.wakeup();
	}

	public void eventProcRegist() {
		ConnectHandler handler = null;
		while ((handler = queue.poll()) != null) {

			try {
				handler.setBindSelect(this.select);
				handler.setSelKey(
						handler.getChannel().register(handler.getBindSelect(), SelectionKey.OP_READ, handler));
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {

		Set<SelectionKey> selKeySet = null;
		while (true) {
			try {
				int selKey = select.select(500);
				// 首先进行连接的处理操作
				eventProcRegist();
				if (selKey > 0) {
					selKeySet = select.selectedKeys();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null != selKeySet) {
				for (SelectionKey selectionKey : selKeySet) {

					if (selectionKey.isValid()) {
						// 检查数据的事件信息，进行对应的操作
						// 处理读取事件
						if (selectionKey.isReadable()) {
							ConnectHandler connHandler = (ConnectHandler) selectionKey.attachment();
							try {
								connHandler.doRead();
							} catch (IOException e) {
								e.printStackTrace();
								try {
									connHandler.getSelKey().cancel();
									connHandler.getChannel().close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						// 处理写入事件
						else if (selectionKey.isWritable()) {
							ConnectHandler connHandler = (ConnectHandler) selectionKey.attachment();
							try {
								connHandler.doWrite();
							} catch (IOException e) {
								e.printStackTrace();
								try {
									connHandler.getSelKey().cancel();
									connHandler.getChannel().close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}

					}
				}

				selKeySet.clear();
			}
		}
	}

	public Selector getSelect() {
		return select;
	}

	public void setSelect(Selector select) {
		this.select = select;
	}

}
