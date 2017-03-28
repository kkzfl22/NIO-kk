package com.kk.nio.socket.multreactor.endecoder;

import java.io.IOException;

/**
 * 进行消息的编解码处理接口
 * 
 * @since 2017年3月28日 下午4:58:25
 * @version 0.0.1
 * @author liujun
 */
public interface CoderProcess<M> {

	/**
	 * 
	 * 进行消息的编码
	 * 
	 * @param msg
	 *            编码信息
	 * @return 编码后信息
	 * @throws IOException
	 */
	public EnDecoderBean encode(M msg) throws IOException;

	/**
	 * 进行消息的解码操作
	 * 
	 * @param decode
	 *            解码的源信息
	 * @return 解码后的信息
	 * @throws IOException
	 *             异常
	 */
	public M decoder(EnDecoderBean bean) throws IOException;

}
