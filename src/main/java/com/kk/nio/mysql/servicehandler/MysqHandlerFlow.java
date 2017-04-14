package com.kk.nio.mysql.servicehandler;

import com.kk.nio.mysql.chain.MsgBaseInf;
import com.kk.nio.mysql.chain.MsgEnDecodeInf;
import com.kk.nio.mysql.chain.ReactorMysqlEnDecodeHandler;
import com.kk.nio.mysql.chain.ReactorMysqlHandlerBase;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 对数据处理流程化
 * 
 * @since 2017年4月14日 下午1:50:20
 * @version 0.0.1
 * @author liujun
 */
public class MysqHandlerFlow {

	/**
	 * 消息最基本的操作接口,用来发送与接收数据
	 */
	protected MsgBaseInf msgBase = new ReactorMysqlHandlerBase();

	/**
	 * 进行消息的编解码信息
	 */
	protected MsgEnDecodeInf<PackageHeader> msgEnDecode = new ReactorMysqlEnDecodeHandler(msgBase);


}
