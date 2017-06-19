package com.kk.nio.demo.midd.handler.blackmysqlconn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.midd.handler.BaseHandler;

/**
 * 用来进行mysql的读写的相关处理
 * 
 * @since 2017年6月14日 下午4:00:24
 * @version 0.0.1
 * @author liujun
 */
public class BlackmysqlConnHandler extends BaseHandler {

	public BlackmysqlConnHandler(SocketChannel channel, int event) throws IOException {
		super(channel, event);
	}

	@Override
	protected void doRead() {

	}

	@Override
	protected void doWrite() {

	}

}
