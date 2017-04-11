package com.kk.nio.mysql.packhandler.bean.pkg;

import java.nio.ByteBuffer;

/**
 * 进行数据解包及封包的上下对象信息
 * 
 * @since 2017年4月12日 上午1:10:58
 * @version 0.0.1
 * @author kk
 */
public class PkgContext {

	/**
	 * buffer信息
	 */
	private ByteBuffer buffer;

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

}
