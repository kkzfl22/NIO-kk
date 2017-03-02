package com.kk.nio.socket.httpserver.pervlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * ģ���servlet������
 * @author Think
 *
 */
public class PvRequest {
	
	/**
	 * url��Я���Ĳ��� 
	 */
	private Map<String,String> param;
	
	
	/**
	 * URL��Ϣ
	 */
	private String url;
	
	/**
	 * ��������Ϣ
	 */
	private InputStream input;
	
	
	/**
	 * �������Ϣ
	 */
	private OutputStream output ;


	public Map<String, String> getParam() {
		return param;
	}


	public void setParam(Map<String, String> param) {
		this.param = param;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public InputStream getInput() {
		return input;
	}


	public void setInput(InputStream input) {
		this.input = input;
	}


	public OutputStream getOutput() {
		return output;
	}


	public void setOutput(OutputStream output) {
		this.output = output;
	}

	
	
	
}
