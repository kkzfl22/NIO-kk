package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;

/**
 * 消息处理接口
 * 
 * @since 2017年3月28日 下午6:59:37
 * @version 0.0.1
 * @author liujun
 */
public interface MsgProcessInf {

	/**
	 * 进行正常流程执行的代码
	 * 
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	public boolean invoke(ContextChain seqList) throws IOException;

}
