package com.kk.nio.socket.multreactor.endecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 进行按回车符进行编码及解码信息
 * 
 * @since 2017年3月28日 下午5:03:13
 * @version 0.0.1
 * @author liujun
 */
public class LineMsgCoderProcess implements CoderProcess<String> {

	@Override
	public EnDecoderBean encode(String bean) throws IOException {

		return null;
	}

	@Override
	public String decoder(EnDecoderBean bean) throws IOException {
		ByteBuffer readerBuffer = bean.getBuffer();
		// 取得当前的位置
		int readOpts = readerBuffer.position();

		String line = null;

		int lastModPositon = bean.getLastModPositon();

		// 2,将数据按行进行分隔,得到一行记录
		for (int i = lastModPositon; i < readOpts; i++) {
			// 找到换行符
			if (readerBuffer.get(i) == 13) {
				byte[] byteValue = new byte[i - lastModPositon];
				// 标识位置，然后开始读取
				readerBuffer.position(lastModPositon);
				readerBuffer.get(byteValue);

				// lastModPositon = i;
				// 重新设置最后的读取的位置
				bean.setLastModPositon(i);

				line = new String(byteValue);
				break;
			}
		}
		return line;
	}

}
