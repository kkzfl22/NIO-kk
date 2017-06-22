package com.kk.nio.demo.midd.handler.multmidconn;

import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateAuthRsp;
import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateAuthSelUser;
import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateAuthSelVersion;
import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateHandshake;
import com.kk.nio.demo.midd.handler.multmidconn.state.MultMidStateInf;

/**
 * 具体的io事件处理的枚举类
 * 
 * @since 2017年6月20日 下午3:07:23
 * @version 0.0.1
 * @author liujun
 */
public enum MultMidStateEnum {

	/**
	 * 进行握手协议的消息处理状态
	 */
	MULTMIDSTATE_HANDSHAKE(new MultMidStateHandshake()),

	/**
	 * 进行握手消息的结果处理
	 */
	MULTMIDSTATE_AUTHRSP(new MultMidStateAuthRsp()),
	
	/**
	 * 进行握手消息的成功之后的版本信息查询
	 */
	MULTMIDSTATE_AUTH_SELECT_VERSION(new MultMidStateAuthSelVersion()),
	
	/**
	 * 进行握手消息成功之后的一次心跳消息
	 */
	MULTMIDSTATE_AUTH_SELECT_USER(new MultMidStateAuthSelUser()),

	;

	/**
	 * 中间件接收数据状态
	 */
	private MultMidStateInf multMidState;

	private MultMidStateEnum(MultMidStateInf multMidState) {
		this.multMidState = multMidState;
	}

	public MultMidStateInf getMultMidState() {
		return multMidState;
	}

	public void setMultMidState(MultMidStateInf multMidState) {
		this.multMidState = multMidState;
	}

}
