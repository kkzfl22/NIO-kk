package com.kk.nio.socket.httpserver.pervlet;

import java.io.IOException;

/**
 * 用来进行servlet的模拟
 * 
 * @author Think
 *
 */
public interface Pervlet {

	/**
	 * 进行相关业务的处理
	 * 
	 * @param req
	 *            请求信息
	 * @return 响应信息
	 */
	public void process(PvRequest req, PvResponse rsp) throws IOException;
}
