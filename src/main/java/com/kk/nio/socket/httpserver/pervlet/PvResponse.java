package com.kk.nio.socket.httpserver.pervlet;


/**
 * ����servlet����Ӧ
 * @author kk
 * @time 2017��3��2��
 * @version 0.0.1
 */
public class PvResponse {
	
	private StringBuilder msg = new StringBuilder();
	
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
	
	
	
	
}
