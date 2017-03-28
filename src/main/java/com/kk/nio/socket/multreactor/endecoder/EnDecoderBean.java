package com.kk.nio.socket.multreactor.endecoder;

import java.nio.ByteBuffer;

/**
 * 进行消息编解码信息的bean信息
 * 
 * @since 2017年3月28日 下午5:15:21
 * @version 0.0.1
 * @author liujun
 */
public class EnDecoderBean {

	/**
	 * buffer信息
	 */
	private ByteBuffer buffer;

	/**
	 * 位置信息
	 */
	private int lastModPositon;

	public EnDecoderBean(ByteBuffer buffer, int lastModPositon) {
		super();
		this.buffer = buffer;
		this.lastModPositon = lastModPositon;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public int getLastModPositon() {
		return lastModPositon;
	}

	public void setLastModPositon(int lastModPositon) {
		this.lastModPositon = lastModPositon;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EnDecoderBean [buffer=");
		builder.append(buffer);
		builder.append(", lastModPositon=");
		builder.append(lastModPositon);
		builder.append("]");
		return builder.toString();
	}

}
