package com.kk.nio.demo.midd.handler.multmidconn.state;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import com.kk.nio.demo.midd.util.ByteBufferTools;

/**
 * 进行mysql的握手协议包的操作
 * 
 * @since 2017年6月20日 下午2:34:00
 * @version 0.0.1
 * @author liujun
 */
public class MultMidStateHandshake implements MultMidStateInf {

	@Override
	public boolean doRead(MultMidStateContext iostateContext) throws Exception {

		// 1,读取到后端的读取流
		ByteBuffer byteBuffer = iostateContext.getMysqlConn().getReadBuffer();
		
		iostateContext.getChannel().write(byteBuffer);

		return false;

	}

	@Override
	public boolean doWrite(MultMidStateContext iostateContext) throws Exception {

		iostateContext.getChannel().write(iostateContext.getWriteBuffer());

		// 检查当前是否已经写入完成,则切换状态为读取监听
		if (iostateContext.getWriteBuffer().hasRemaining()) {
			// 进行压缩
			iostateContext.getWriteBuffer().compact();
			return false;

		} else {
			iostateContext.getCurrSelkey().interestOps(
					iostateContext.getCurrSelkey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			return true;
		}
	}

}
