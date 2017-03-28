package com.kk.nio.socket.multreactor.procchain.chain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.multreactor.procchain.ChainEnum;
import com.kk.nio.socket.multreactor.procchain.ContextChain;
import com.kk.nio.socket.multreactor.procchain.MsgProcessInf;

/**
 * 首先进行消息的解码操作
 * 
 * @since 2017年3月28日 下午7:03:29
 * @version 0.0.1
 * @author liujun
 */
public class MsgDecode implements MsgProcessInf {

	@Override
	public boolean invoke(ContextChain seqList) throws IOException {

		SocketChannel socketChannel = seqList.getSocketChannel();
		ByteBuffer readerBuffer = seqList.getReaderBuffer();
		int lastModPositon = seqList.getLastModPositon();

		// 1,首先从将数据加载到bytebuffer中
		socketChannel.read(readerBuffer);

		// 取得当前的位置
		int readOpts = readerBuffer.position();

		String line = null;

		// 2,将数据按行进行分隔,得到一行记录
		for (int i = lastModPositon; i < readOpts; i++) {
			// 找到换行符
			if (readerBuffer.get(i) == 13) {
				byte[] byteValue = new byte[i - lastModPositon];
				// 标识位置，然后开始读取
				readerBuffer.position(lastModPositon);
				readerBuffer.get(byteValue);

				seqList.setLastModPositon(i);

				line = new String(byteValue);
				break;
			}
		}

		if (line == null || line.isEmpty()) {
			return true;
		}

		System.out.println("收到解码消息 :" + line);

		// 设置解码后的信息
		seqList.putParam(ChainEnum.CHAIN_DECODE_VALUE.getKey(), line);

		return seqList.nextDoInvoke();
	}

}
