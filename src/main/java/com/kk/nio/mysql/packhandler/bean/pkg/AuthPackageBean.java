package com.kk.nio.mysql.packhandler.bean.pkg;

import java.util.Arrays;

/**
 * 进行握手的报文
 * 
 * @since 2017年4月11日 下午8:14:08
 * @version 0.0.1
 * @author liujun
 */
public class AuthPackageBean extends PackageHeader {

	/**
	 * 客户端权能标志：用于与客户端协商通讯方式， 标志位含义与握手初始化报文中的相同。客户端收到服务器发来的初始化报文后，
	 * 会对服务器发送的权能标志进行修改，保留自身所支持的功能，然后将权能标返回给服务器， 从而保证服务器与客户端通讯的兼容性。
	 * 
	 * @字段说明 protocolVersion
	 */
	private long clientFlag;

	/**
	 * 最大消息长度：客户端发送请求报文时所支持的最大消息长度值。一般为16M
	 * 
	 * @字段说明 maxPackageLong
	 */
	private long maxPackageLong = 16 * 1024 * 1024;

	/**
	 * 字符编码：标识通讯过程中使用的字符编码，与服务器在认证初始化报文中发送的相同。
	 * 
	 * @字段说明 charSetIndex
	 */
	private int charSetIndex;

	/**
	 * 填充值,一般长度23
	 * 
	 * @字段说明 fillbyte
	 */
	private byte[] fillbyte;

	/**
	 * 填充值值信息
	 * 
	 * @字段说明 FILLER
	 */
	private final byte[] FILLER = new byte[23];

	/**
	 * 用户名：客户端登陆用户的用户名称。
	 * 
	 * @字段说明 userName
	 */
	private String userName;

	/**
	 * 挑战认证数据：客户端用户密码使用服务器发送的挑战随机数进行加密后， 生成挑战认证数据，然后返回给服务器，用于对用户身份的认证。
	 * 
	 * @字段说明 passwd
	 */
	private byte[] passwd;

	/**
	 * 数据库名称：当客户端的权能标志位 CLIENT_CONNECT_WITH_DB 被置位时，该字段必须出现。
	 * 
	 * @字段说明 dataBase
	 */
	private String dataBase;

	public long getClientFlag() {
		return clientFlag;
	}

	public void setClientFlag(long clientFlag) {
		this.clientFlag = clientFlag;
	}

	public long getMaxPackageLong() {
		return maxPackageLong;
	}

	public void setMaxPackageLong(long maxPackageLong) {
		this.maxPackageLong = maxPackageLong;
	}

	public int getCharSetIndex() {
		return charSetIndex;
	}

	public void setCharSetIndex(int charSetIndex) {
		this.charSetIndex = charSetIndex;
	}

	public byte[] getFillbyte() {
		return fillbyte;
	}

	public void setFillbyte(byte[] fillbyte) {
		this.fillbyte = fillbyte;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getPasswd() {
		return passwd;
	}

	public void setPasswd(byte[] passwd) {
		this.passwd = passwd;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public byte[] getFILLER() {
		return FILLER;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthPackageBean [clientFlag=");
		builder.append(clientFlag);
		builder.append(", maxPackageLong=");
		builder.append(maxPackageLong);
		builder.append(", charSetIndex=");
		builder.append(charSetIndex);
		builder.append(", fillbyte=");
		builder.append(Arrays.toString(fillbyte));
		builder.append(", FILLER=");
		builder.append(Arrays.toString(FILLER));
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", passwd=");
		builder.append(Arrays.toString(passwd));
		builder.append(", dataBase=");
		builder.append(dataBase);
		builder.append("]");
		return builder.toString();
	}

}
