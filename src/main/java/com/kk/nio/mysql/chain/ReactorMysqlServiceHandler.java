package com.kk.nio.mysql.chain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * 进行消息的业务处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class ReactorMysqlServiceHandler implements MsgDataServiceInf {

	/**
	 * 消息编解码对象信息
	 */
	private final MsgEnDecodeInf<String> msgEndecode;

	public ReactorMysqlServiceHandler(MsgEnDecodeInf<String> msgEndecode) {
		super();
		this.msgEndecode = msgEndecode;
	}

	@Override
	public void readData(MysqlContext context) throws IOException {
		// 进行消息的解码操作
		String msg = this.msgEndecode.msgDecode(context);

	}

	@Override
	public void writeData(MysqlContext context) throws IOException {
		// 如果当前需要写入的数据不为空，则进行编码
		if (context.getWriteData() != null) {
			msgEndecode.msgEncode(context);
		}
		// 需要取消录写入事件，进行读取操作
		else {
			context.getSelectKey()
					.interestOps(context.getSelectKey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		}

	}

}
