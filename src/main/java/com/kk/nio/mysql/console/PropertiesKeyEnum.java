package com.kk.nio.mysql.console;

/**
 * 属性文件写义的key信息
 * 
 * @since 2017年4月13日 下午8:11:46
 * @version 0.0.1
 * @author liujun
 */
public enum PropertiesKeyEnum {

	/**
	 * 数据库的ip
	 */
	MYSQL_SERVER_IP("mysql.server.ip"),

	/**
	 * 数据库的端口
	 */
	MYSQL_SERVER_PORT("mysql.server.port"),

	/**
	 * 数据库用户名
	 */
	MYSQL_USERNAME("mysql.username"),

	/**
	 * 数据库的密码
	 */
	MYSQL_PASSWORD("mysql.passwd"),

	/**
	 * 连接的数据库
	 */
	MYSQL_DATABASE("mysql.database"),;

	/**
	 * 参数key信息
	 */
	private String key;

	private PropertiesKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
