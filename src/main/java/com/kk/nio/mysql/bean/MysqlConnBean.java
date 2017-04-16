package com.kk.nio.mysql.bean;

/**
 * 
 * 进行mysql连接的对象信息
 * 
 * @since 2017年4月16日 下午6:38:46
 * @version 0.0.1
 * @author kk
 */
public class MysqlConnBean {

	/**
	 * mysql的ip地址
	 */
	private String serverIP;

	/**
	 * 端口信息
	 */
	private int port;

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MysqlConnBean [serverIP=");
		builder.append(serverIP);
		builder.append(", port=");
		builder.append(port);
		builder.append("]");
		return builder.toString();
	}

}
