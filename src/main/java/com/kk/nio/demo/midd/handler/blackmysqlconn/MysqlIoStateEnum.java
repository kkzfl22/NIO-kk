package com.kk.nio.demo.midd.handler.blackmysqlconn;

import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIoStateAuthRsp;
import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIoStateHandshake;
import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIoStateSelUser;
import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.BlackMysqlIoStateSelVersion;
import com.kk.nio.demo.midd.handler.blackmysqlconn.iostate.MysqlIoStateInf;

/**
 * 具体的io事件处理的枚举类
 * 
 * @since 2017年6月20日 下午3:07:23
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlIoStateEnum {

	/**
	 * 进行握手协议的消息处理状态
	 */
	IOSTATE_HANDSHAKE(new BlackMysqlIoStateHandshake()),

	/**
	 * 进行登录的结果处理
	 */
	IOSTATE_AUTHRSP(new BlackMysqlIoStateAuthRsp()),

	/**
	 * 进行登录成功之后版本号查询
	 */
	IOSTATE_SELVERSION(new BlackMysqlIoStateSelVersion()),

	/**
	 * 进行登录成功之后的用户查询
	 */
	IOSTATE_SELUSER(new BlackMysqlIoStateSelUser()),

	;

	/**
	 * 状态接口信息
	 */
	private MysqlIoStateInf ioState;

	private MysqlIoStateEnum(MysqlIoStateInf ioState) {
		this.ioState = ioState;
	}

	public MysqlIoStateInf getIoState() {
		return ioState;
	}

	public void setIoState(MysqlIoStateInf ioState) {
		this.ioState = ioState;
	}

}
