package com.kk.nio.socket.multreactor.procchain.chain;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.socket.multreactor.procchain.ContextChain;
import com.kk.nio.socket.multreactor.procchain.MsgProcessInf;

public class Bytecompact implements MsgProcessInf {

	@Override
	public boolean invoke(ContextChain seqList) throws IOException {

		ByteBuffer readerBuffer = seqList.getReaderBuffer();

		// 4,当容量超过总容量的2分之一大小则需要压缩
		if (readerBuffer.position() > readerBuffer.capacity() / 2) {
			// 重新标识position,将当前的容量标识为buffer中已有数据的容量
			readerBuffer.limit(readerBuffer.position());
			// 标识当前pos为上一次已经读取到的数据的pos
			readerBuffer.position(seqList.getLastModPositon());

			// 压缩数据,即将上一次读取之前的记录丢弃
			readerBuffer.compact();

			seqList.setLastModPositon(0);
		}

		return seqList.nextDoInvoke();
	}


}
