package com.kk.nio.socket.httpserver.fileupload;

import java.io.InputStream;
import java.net.Socket;

/**
 * 进行上传的实体对象信息
 * 
 * @author kk
 * @time 2017年3月5日
 * @version 0.0.1
 */
public class PervletUploadBean {

	/**
	 * 分隔字符串,用于文件上传的段的区分
	 */
	private String spitFlag;

	/**
	 * 请求地址
	 */
	private String reqUrl;

	/**
	 * 连接操作
	 */
	private Socket socket;
	
	/**
	 * 文件名称 
	 */
	private String fileName;
	
	/**
	 * 类弄信息
	 */
	private String type;
	
	

	public String getSpitFlag() {
		return spitFlag;
	}

	public void setSpitFlag(String spitFlag) {
		this.spitFlag = spitFlag;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	

}
