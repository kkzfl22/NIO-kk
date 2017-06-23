package com.kk.nio.mysqlproxy.proc.blackmysql.connstate;

import java.io.IOException;

import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlConnHandStateInf;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.MysqIOStateEnum;

/**
 * 首先进行后端连接的创建操作
 * 
 * @since 2017年6月23日 下午2:03:18
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnIOStateCreate implements BlackMysqlConnHandStateInf {

	@Override
	public void doRead(BlackMysqlClientHandler handler) throws IOException {

		// 检查当前连接处理的io状态是否设置
		if (handler.getIostateContext().getCurrState() == null) {
			handler.getIostateContext().setCurrState(MysqIOStateEnum.BLACLMYSQLIOSTATE_HANDSHAKE.getMysqlIOState());
		}

		// 进行数据读取操作
		handler.getIostateContext().doRead();

	}

	@Override
	public void doWrite(BlackMysqlClientHandler handler) throws IOException {
		handler.getIostateContext().doWrite();
	}

}
