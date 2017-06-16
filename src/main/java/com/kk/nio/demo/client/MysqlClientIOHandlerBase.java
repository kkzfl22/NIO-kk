package com.kk.nio.demo.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.client.queue.RegBean;

/**
 * 进行io事情处理的基本类
 * 
 * @since 2017年6月14日 下午4:00:24
 * @version 0.0.1
 * @author liujun
 */
public abstract class MysqlClientIOHandlerBase implements Runnable {


	/**
	 * 当前注册的key信息
	 */
	protected SelectionKey currSelectKey;

	/**
	 * 读取的buffer信息
	 */
	protected ByteBuffer readBuffer;

	/**
	 * 进行写入的buffer信息
	 */
	protected ByteBuffer writeBuffer;

	/**
	 * 读取到的字节数
	 */
	protected int readOption = 0;

	public MysqlClientIOHandlerBase() {
		// 定义一个512字节大小的缓冲区
		writeBuffer = ByteBuffer.allocate(1024 * 512);
		readBuffer = ByteBuffer.allocate(1024 * 512);
	}

	public void setSelectKey(SelectionKey currSelectKey) {
		this.currSelectKey = currSelectKey;
	}

	/**
	 * 注册连接对象信息
	 * 
	 * @param select
	 * @param channel
	 * @throws IOException
	 */
	public RegBean registObj(Selector select, SocketChannel channel) throws IOException {

		return new RegBean(channel, select, this);

		// his.channel = channel;
		// // 设置为非阻塞模式
		// channel.configureBlocking(false);
		// // 开始注册
		// System.out.println("开始注册读取事件...");
		// long startTime = System.currentTimeMillis();
		// // 首先注册读取事件
		// currSelectKey = channel.register(select, SelectionKey.OP_READ, this);
		// System.out.println("结束注册读取事件...");
		// long end = System.currentTimeMillis();
		// System.out.println("用时:" + (end - startTime));
	}

	@Override
	public void run() {

		// 如果当前是读取事件，交给读取方法
		if (currSelectKey.isReadable()) {
			doRead();
		}
		// 如果是写入事件，交给写入方法处理
		else if (currSelectKey.isWritable()) {
			doWrite();
		}
	}

	/**
	 * 进行数据读取操作
	 */
	protected abstract void doRead();

	/**
	 * 进行数据写入
	 */
	protected abstract void doWrite();

}
