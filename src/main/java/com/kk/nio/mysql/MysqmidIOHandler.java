package com.kk.nio.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.mysql.chain.MsgBaseInf;
import com.kk.nio.mysql.chain.MsgEnDecodeInf;
import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.chain.ReactorMysqlEnDecodeHandler;
import com.kk.nio.mysql.chain.ReactorMysqlHandlerBase;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

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

	public MysqmidIOHandler(Selector select, SocketChannel socket) throws IOException {

		super(select, socket);

		this.readBuffer = ByteBuffer.allocateDirect(256);

		this.writeBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 3);

		context = new MysqlContext(this.socketChannel, this.selectKey, this.writeBuffer, this.readBuffer);
	}

	@Override
	protected void doConnection() throws IOException {

		context.setReadPkgHandler(PkgReadProcessEnum.PKG_READ_HANDSHAKE.getPkgRead());

		// 进行握手消息的读取
		// msgDataService.readData(context);

	}

	@Override
	protected void doHandler() throws IOException {
		// System.out.println("当前读取操作");
		// Context context = new Context(this.socketChannel, this.selectKey,
		// this.writeBuffer, this.readBuffer);
		// context.setLastModPositon(lastPosition);

		//msgDataService.readData(context);

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
		//msgDataService.writeData(context);
	}

}
