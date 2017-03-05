package com.kk.nio.socket.httpserver.fileupload;

import java.io.IOException;
import java.net.Socket;

/**
 * 用来进行处理具体的事情
 * 
 * @author Think
 *
 */
public interface PervletSocketInf {

	/**
	 * 进行相关业务的处理
	 * 
	 * @param req
	 *            请求信息
	 * @return 响应信息
	 */
	public void process(PervletUploadBean bean, String line) throws IOException;
}
