package com.kk.nio.mysqlproxy.proc.blackmysql.connstate;

import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlClientHandler;
import com.kk.nio.mysqlproxy.proc.blackmysql.BlackMysqlConnHandStateInf;

/**
 * 首先进行后端连接的创建操作
 * 
 * @since 2017年6月23日 下午2:03:18
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlConnIOStateUse implements BlackMysqlConnHandStateInf {
	

	@Override
	public void doRead(BlackMysqlClientHandler handler) {
		
		//检查当前连接处理的io状态是否设置
		if(handler.getIostateContext().getCurrState() == null)
		{
			
		}
		
	}

	@Override
	public void doWrite(BlackMysqlClientHandler handler) {

	}

}
