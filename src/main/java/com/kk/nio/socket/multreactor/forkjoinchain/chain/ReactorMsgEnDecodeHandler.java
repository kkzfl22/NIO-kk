package com.kk.nio.socket.multreactor.forkjoinchain.chain;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 进行消息的编码以及解码处理
 * 
 * @since 2017年3月29日 下午5:31:05
 * @version 0.0.1
 * @author liujun
 */
public class ReactorMsgEnDecodeHandler implements MsgEnDecodeInf<String> {

	/**
	 * 消息处理的接口
	 */
	private final MsgBaseInf msgBase;

	public ReactorMsgEnDecodeHandler(MsgBaseInf msgBase) {
		super();
		this.msgBase = msgBase;
	}

	@Override
	public void msgEncode(Context context) throws IOException {
		// 取得当前数据，对消息进行编码
		String msg = String.valueOf(context.getWriteData());

		byte[] value = msg.getBytes("GBK");

		context.getWriteBuffer().put(value);

		// 进行消息的发送
		msgBase.writeData(context);

	}

	@Override
	public String msgDecode(Context context) throws IOException {

		// 进行消息的解码操作,首先进行消息的读取
		ByteBuffer readerBuffer = msgBase.readData(context);

		int lastModPositon = context.getLastModPositon();

		int readOpts = readerBuffer.position();

		String line = null;

		// System.out.println("lastModPositon:" + lastModPositon +
		// ",readerBuffer.position():" + readerBuffer.position());
		// 对消息按回车进行解码操作
		// 2,将数据按行进行分隔,得到一行记录
		for (int i = lastModPositon; i < readOpts; i++) {
			// 找到换行符
			if (readerBuffer.get(i) == 13) {
				byte[] byteValue = new byte[i - lastModPositon];
				// 标识位置，然后开始读取
				readerBuffer.position(lastModPositon);
				readerBuffer.get(byteValue);

				lastModPositon = i;
				context.setLastModPositon(i);

				line = new String(byteValue);
				System.out.println("收到解码后的数据msg :" + line);

				break;
			}
		}

		return line;
	}

}
