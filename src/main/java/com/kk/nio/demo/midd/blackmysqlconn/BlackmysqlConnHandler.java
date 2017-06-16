package com.kk.nio.demo.midd.blackmysqlconn;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 用来进行mysql的读写的相关处理
 * 
 * @since 2017年6月14日 下午4:00:24
 * @version 0.0.1
 * @author liujun
 */
public class BlackmysqlConnHandler extends BlackmysqlConnHandlerBase {

	public BlackmysqlConnHandler(Selector select, SocketChannel channel) throws IOException {
		super(select, channel);
	}

	@Override
	protected void doBlackMysqlConnRead() {

	}

	@Override
	protected void doBlackMysqlConnWrite() {

	}

}
