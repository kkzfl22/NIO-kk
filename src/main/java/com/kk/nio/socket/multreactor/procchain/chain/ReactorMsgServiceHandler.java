package com.kk.nio.socket.multreactor.procchain.chain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.socket.util.CmdUtils;

/**
 * 进行消息的业务处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class ReactorMsgServiceHandler implements MsgDataServiceInf {

	/**
	 * 消息编解码对象信息
	 */
	private final MsgEnDecodeInf<String> msgEndecode;

	public ReactorMsgServiceHandler(MsgEnDecodeInf<String> msgEndecode) {
		super();
		this.msgEndecode = msgEndecode;
	}

	@Override
	public void readData(Context context) throws IOException {
		// 进行消息的解码操作
		String msg = this.msgEndecode.msgDecode(context);

		ByteBuffer readerBuffer = context.getReadBuffer();

		// 进行压缩
		// 4,当容量超过总容量的2分之一大小则需要压缩
		if (readerBuffer.position() > readerBuffer.capacity() / 2) {
			// 重新标识position,将当前的容量标识为buffer中已有数据的容量
			readerBuffer.limit(readerBuffer.position());
			// 标识当前pos为上一次已经读取到的数据的pos
			readerBuffer.position(context.getLastModPositon());

			// 压缩数据,即将上一次读取之前的记录丢弃
			readerBuffer.compact();

			context.setLastModPositon(0);
		}

		if (null != msg && !msg.isEmpty()) {
			// 取消事件注册，因为要应答数据
			context.getSelectKey().interestOps(context.getSelectKey().interestOps() & ~SelectionKey.OP_READ);
			// 执行命令
			String cmdRsp = CmdUtils.runCmd(msg);

			// 设置数据响应结果
			context.setWriteData(cmdRsp);

			// 将结果进行写入
			this.writeData(context);
		}
	}

	@Override
	public void writeData(Context context) throws IOException {

		// 如果当前需要写入的数据不为空，则进行编码
		if (context.getWriteData() != null) {
			msgEndecode.msgEncode(context);
		}

	}

}
