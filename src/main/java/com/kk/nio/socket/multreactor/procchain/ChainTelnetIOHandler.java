package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.multreactor.procchain.chain.Context;
import com.kk.nio.socket.multreactor.procchain.chain.MsgBaseInf;
import com.kk.nio.socket.multreactor.procchain.chain.MsgDataServiceInf;
import com.kk.nio.socket.multreactor.procchain.chain.MsgEnDecodeInf;
import com.kk.nio.socket.multreactor.procchain.chain.ReactorMsgEnDecodeHandler;
import com.kk.nio.socket.multreactor.procchain.chain.ReactorMsgHandlerBase;
import com.kk.nio.socket.multreactor.procchain.chain.ReactorMsgServiceHandler;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class ChainTelnetIOHandler extends ChainMultIOHandler {

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

	public ChainTelnetIOHandler(Selector select, SocketChannel socket) throws IOException {
		super(select, socket);

		TimeColltion.addTime("2_reactor_start", System.currentTimeMillis());

		this.readBuffer = ByteBuffer.allocateDirect(256);

		this.writeBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 3);

		context = new Context(this.socketChannel, this.selectKey, this.writeBuffer, this.readBuffer);

		// 进行数据的首次写入
		this.doConnection();

		TimeColltion.addTime("3_reactor_over", System.currentTimeMillis());

		TimeColltion.print();

	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !").append(LINE);
		msg.append("1,input command ").append(LINE);
		msg.append("2,exit ").append(LINE);

		// 设置需要发送的消息信息
		context.setWriteData(msg.toString());

		// 进行消息的发送
		msgDataService.writeData(context);

	}

	@Override
	protected void doHandler() throws IOException {
		msgDataService.readData(context);
	}

	@Override
	protected void onError(Exception e) {
		System.out.println("curr handler process error");
		e.printStackTrace();
	}

	@Override
	protected void onClose() {
		// this.selectKey.cancel();
		try {
			this.selectKey.channel().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void writeData() throws IOException {
		Context context = new Context(this.socketChannel, this.selectKey, this.writeBuffer, this.readBuffer);
		// 进行消息的发送
		msgDataService.writeData(context);
	}

}
