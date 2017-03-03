package com.kk.nio.socket.httpserver.pervlet;

import java.io.OutputStream;

/**
 * 进行servlet的响应
 * 
 * @author kk
 * @time 2017年3月2日
 * @version 0.0.1
 */
public class PvResponse {

	/**
	 * 输出流信息
	 */
	private OutputStream output;

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}


}
