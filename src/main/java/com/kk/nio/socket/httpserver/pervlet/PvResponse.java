package com.kk.nio.socket.httpserver.pervlet;

import java.io.OutputStream;

/**
 * 进行servlet的响应
 * @author kk
 * @time 2017年3月2日
 * @version 0.0.1
 */
public class PvResponse {
	
	private StringBuilder msg = new StringBuilder();
	
	
	/**
	 * 输出流信息
	 */
	private OutputStream output ;
	
	public PvResponse(String initMsg)
	{
		this.msg.append(initMsg);
	}

	public String getMsg() {
		return msg.toString();
	}

	public void setMsg(String msg) {
		this.msg.append(msg);
	}

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public void setMsg(StringBuilder msg) {
		this.msg = msg;
	}
	
	
	
	
}
