package com.kk.nio.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.connection.MysqlConnContext;
import com.kk.nio.mysql.console.MysqlConnStateEnum;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class MysqmidIOHandler extends MysqlIOHandlerBase {

	/**
	 * 数据读取的buffer
	 */
	private ByteBuffer readBuffer;

	/**
	 * 数据写入的 buffer
	 */
	private ByteBuffer writeBuffer;

	/**
	 * 上下文对象信息
	 */
	private final MysqlContext context;

	/**
	 * 初始化mysql连接的上下文信息
	 */
	private MysqlConnContext mysqlConnContext = new MysqlConnContext();

	public MysqmidIOHandler(Selector select, SocketChannel socket) throws IOException {

		super(select, socket);

		this.readBuffer = ByteBuffer.allocateDirect(256);

		this.writeBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 3);

		context = new MysqlContext(this.socketChannel, this.selectKey, this.writeBuffer, this.readBuffer);

		// 设置处理数据的上下文对象信息
		mysqlConnContext.setContext(context);

		// 初始化为连接为创建连接的状态处理
		mysqlConnContext.setMysqlConnState(MysqlConnStateEnum.MYSQL_CONN_STATE_CREATE.getConnState());
	}

	@Override
	protected void doConnection() throws IOException {

		// 进行握手消息的读取
		// msgDataService.readData(context);

	}

	@Override
	protected void doHandler() throws IOException {
		// System.out.println("当前读取操作");

		// msgDataService.readData(context);

		// 进行数据读取
		mysqlConnContext.stateReadProcess();

	}

	@Override
	protected void onError() {
		System.out.println("curr handler process error");
	}

	@Override
	protected void onClose() {
		if (null != this.selectKey) {
			try {
				this.selectKey.channel().close();
				this.selectKey.cancel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void writeData() throws IOException {
		// 进行消息的发送
		mysqlConnContext.stateWriteProcess();
	}

}
