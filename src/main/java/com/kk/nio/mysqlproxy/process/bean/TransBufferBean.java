package com.kk.nio.mysqlproxy.process.bean;

import java.nio.ByteBuffer;

/**
 * 用来进行传输的对象信息
 * 
 * @since 2017年6月9日 下午5:02:36
 * @version 0.0.1
 * @author liujun
 */
public class TransBufferBean {

	/**
	 * 读取的字节数
	 */
	private volatile int readPosition;

	/**
	 * 写入的字节数
	 */
	private volatile int writePosition;

	/**
	 * 进行操作的buffer对象信息
	 */
	private ByteBuffer buffer;

	/**
	 * 当前的状态是读取还是写入
	 */
	private int state;

}
