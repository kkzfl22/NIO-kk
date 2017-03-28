package com.kk.nio.socket.multreactor.procchain;

/**
 * 责任链处理流程的参数定义
 * 
 * @since 2017年3月28日 下午7:05:38
 * @version 0.0.1
 * @author liujun
 */
public enum ChainEnum {

	/**
	 * 解码后的消息信息
	 */
	CHAIN_DECODE_VALUE("chain_decode_value"),

	;

	/**
	 * 
	 */
	private String key;

	private ChainEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
