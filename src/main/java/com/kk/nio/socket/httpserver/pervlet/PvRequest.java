package com.kk.nio.socket.httpserver.pervlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 模拟的servlet的请求
 * @author Think
 *
 */
public class PvRequest {
	
	/**
	 * url中携带的参数 
	 */
	private Map<String,String> param;
	
	
	/**
	 * URL信息
	 */
	private String url;
	
	/**
	 * 输入流信息
	 */
	private InputStream input;
	
	
	/**
	 * 输出流信息
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

	
	public static void main(String[] args) {
		String ss = null;
		PvRequest req = new PvRequest();
		Iterator<Entry<String, String>> itervals = req.getParam().entrySet().iterator();
		while(itervals.hasNext())
		{
			Entry<String, String> itemValeu = itervals.next();
			ss = ss.replaceAll("\\$\\{"+itemValeu.getKey()+"\\}", itemValeu.getValue());
		}
	}
	
}
