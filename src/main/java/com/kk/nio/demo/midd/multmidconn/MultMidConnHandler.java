package com.kk.nio.demo.midd.multmidconn;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 进行中间件的多路连接处理
 * 
 * @since 2017年6月16日 下午3:02:31
 * @version 0.0.1
 * @author liujun
 */
public class MultMidConnHandler extends MultMidConnHandlerBase {

	public MultMidConnHandler(Selector select, SocketChannel channel) throws IOException {
		super(select, channel);
	}

	@Override
	protected void doMultRead() {

	}

	@Override
	protected void doMultWrite() {

	}

}
