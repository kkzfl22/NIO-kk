package com.kk.nio.socket.multreactor.forkjoinchain.chain;

import java.io.IOException;

/**
 * 消息处理编解码接口
 * 
 * @since 2017年3月29日 下午5:18:47
 * @version 0.0.1
 * @author liujun
 */
public interface MsgEnDecodeInf<T> {

	/**
	 * 进行消息编码操作
	 * 
	 * @param context
	 *            上下文对象信息
	 * @throws IOException
	 */
	public void msgEncode(Context context) throws IOException;

	/**
	 * 进行消息解码操作
	 * 
	 * @param context
	 *            上下文对象信息
	 * @return 解码后的结果
	 * @throws IOException
	 */
	public T msgDecode(Context context) throws IOException;

}
