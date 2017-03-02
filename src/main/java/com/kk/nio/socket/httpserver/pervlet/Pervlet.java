package com.kk.nio.socket.httpserver.pervlet;

/**
 * 用来进行servlet的模拟
 * @author Think
 *
 */
public interface Pervlet {
	
	
	/**
	 * 进行相关业务的处理
	 * @param req 请求信息 
	 * @return 响应信息
	 */
	public PvResponse process(PvRequest req);
}
