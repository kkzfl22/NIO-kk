package com.kk.nio.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.multreactor.forkjoinchain.chain.Context;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.MsgBaseInf;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.MsgDataServiceInf;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.MsgEnDecodeInf;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.ReactorMsgEnDecodeHandler;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.ReactorMsgHandlerBase;
import com.kk.nio.socket.multreactor.forkjoinchain.chain.ReactorMsgServiceHandler;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class MysqmidIOHandler extends MysqlIOHandlerBase {

	/**
	 * 换行符
	 */
	private static final String LINE = "\r\n";

	/**
	 * 消息最基本的操作接口,用来发送与接收数据
	 */
	protected MsgBaseInf msgBase = new ReactorMsgHandlerBase();

	/**
	 * 进行消息的编解码信息
	 */
	protected MsgEnDecodeInf<String> msgEnDecode = new ReactorMsgEnDecodeHandler(msgBase);

	/**
	 * 进行消息的业务处理
	 */
	protected MsgDataServiceInf msgDataService = new ReactorMsgServiceHandler(msgEnDecode);

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
	private final Context context;

	public MysqmidIOHandler(Selector select, SocketChannel socket) throws IOException {

		super(select, socket);

		this.readBuffer = ByteBuffer.allocateDirect(256);

		this.writeBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 3);

		context = new Context(this.socketChannel, this.selectKey, this.writeBuffer, this.readBuffer);

		// 进行数据的首次写入
		this.doConnection();

	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !").append(LINE);
		msg.append("1: find keyword in files ").append(LINE);
		msg.append("2, quit ").append(LINE);

		// 设置需要发送的消息信息
		context.setWriteData(msg.toString());

		// 进行消息的发送
		msgDataService.writeData(context);

	}

	@Override
	protected void doHandler() throws IOException {
		// System.out.println("当前读取操作");
		// Context context = new Context(this.socketChannel, this.selectKey,
		// this.writeBuffer, this.readBuffer);
		// context.setLastModPositon(lastPosition);

		msgDataService.readData(context);

		// lastPosition = context.getLastModPositon();

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
		msgDataService.writeData(context);
	}

}
