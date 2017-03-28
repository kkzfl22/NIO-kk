package com.kk.nio.socket.multreactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.kk.nio.socket.multreactor.procchain.ContextChain;
import com.kk.nio.socket.multreactor.procchain.MsgProcessInf;
import com.kk.nio.socket.multreactor.procchain.chain.Bytecompact;
import com.kk.nio.socket.multreactor.procchain.chain.MsgCommandProc;
import com.kk.nio.socket.multreactor.procchain.chain.MsgDecode;

/**
 * 使用链式处理
 * 
 * @since 2017年3月28日 下午5:06:56
 * @version 0.0.1
 * @author liujun
 */
public class TelnetChainIOHandler extends MultChainIOHandler {

	/**
	 * 换行符
	 */
	private static final String LINE = "\r\n";

	ContextChain contextChain = new ContextChain(this.socketChannel, selectKey);

	private MsgProcessInf[] runProx = null;

	public TelnetChainIOHandler(Selector select, SocketChannel socket) throws IOException {

		super(select, socket);

		// 设置任务执行链
		runProx = new MsgProcessInf[3];
		// 1，执行解码任务
		runProx[0] = new MsgDecode();
		// 2，运行命令
		runProx[1] = new MsgCommandProc();
		// 3，进行压缩
		runProx[2] = new Bytecompact();

		// 进行数据的首次写入
		this.doConnection();

	}

	@Override
	protected void doConnection() throws IOException {
		StringBuilder msg = new StringBuilder();

		msg.append("welcome come to kk telnet server,please input command !").append(LINE);
		msg.append("1,input command ").append(LINE);
		msg.append("2,exit ").append(LINE);

		contextChain.writeData(msg.toString().getBytes());
	}

	@Override
	protected void doHandler() throws IOException {
		// 将处理流程交给处理链处理
		
		ContextChain contextChain = new ContextChain(this.socketChannel, selectKey);
		
		contextChain.addExec(runProx);
		contextChain.nextDoInvoke();
	}

	@Override
	protected void onError() {
		System.out.println("curr handler process error");
	}

	@Override
	protected void onClose() {
		this.selectKey.cancel();
		try {
			this.selectKey.channel().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void writeData() throws IOException {
		contextChain.writeData();
	}

}
