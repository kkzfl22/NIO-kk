package com.kk.nio.mysql.packhandler.bean.pkg;

import java.util.Arrays;

/**
 * 进行第一次的握手协议包 源文件名：HandshakePackage.java 文件版本：1.0.0 创建作者：liujun 创建日期：2016年12月8日
 * 修改作者：liujun 修改日期：2016年12月8日 文件描述：TODO 版权所有：Copyright 2016 zjhz, Inc. All
 * Rights Reserved.
 */
public class HandshakeBean extends PackageHeader {

	/**
	 * 服务协议版本号：该值由 PROTOCOL_VERSION
	 * 宏定义决定（参考MySQL源代码/include/mysql_version.h头文件定义），抓包显示协议版本号为10
	 * 
	 * @字段说明 protocolVersion
	 */
	private byte protocolVersion;

	/**
	 * 服务版本信息：该值为字符串，由 MYSQL_SERVER_VERSION
	 * 宏定义决定（参考MySQL源代码/include/mysql_version.h头文件定义），抓包显示mysql服务器版本为5.6.22
	 * 
	 * @字段说明 serverVersion
	 */
	private byte[] serverVersion;

	/**
	 * 服务器线程ID：服务器为当前连接所创建的线程ID。因为客户端创连接到服务器，服务器分配给单独的进程，抓包显示进程号为5，可通过登陆键入show
	 * process list验证
	 * 
	 * @字段说明 serverThreadId
	 */
	private long serverThreadId;

	/**
	 * 挑战随机数：8位,MySQL数据库用户认证采用的是挑战/应答的方式，服务器生成该挑战数并发送给客户端，由客户端进行处理并返回相应结果，
	 * 然后服务器检查是否与预期的结果相同，从而完成用户认证的过程。
	 * 
	 * @字段说明 challengeRandom
	 */
	private byte[] challengeRandom;

	/**
	 * 服务器权能标志：用于与客户端协商通讯方式，各标志位含义如下（参考MySQL源代码/include/mysql_com.h中的宏定义）：
	 * 
	 * @字段说明 serverSeed
	 */
	private int serverCapabilities;

	/**
	 * 字符编码：标识服务器所使用的字符集。
	 * 
	 * @字段说明 serverCharsetIndex
	 */
	private int serverCharsetIndex;

	/**
	 * 服务器状态：状态值定义如下（参考MySQL源代码/include/mysql_com.h中的宏定义）：
	 * 
	 * @字段说明 serverStatus
	 */
	private int serverStatus;

	/**
	 * @字段说明 restOfScrambleBuff
	 */
	private byte[] restOfScrambleBuff;

	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(byte protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public byte[] getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(byte[] serverVersion) {
		this.serverVersion = serverVersion;
	}

	public long getServerThreadId() {
		return serverThreadId;
	}

	public void setServerThreadId(long serverThreadId) {
		this.serverThreadId = serverThreadId;
	}

	public byte[] getChallengeRandom() {
		return challengeRandom;
	}

	public void setChallengeRandom(byte[] challengeRandom) {
		this.challengeRandom = challengeRandom;
	}

	public int getServerCapabilities() {
		return serverCapabilities;
	}

	public void setServerCapabilities(int serverCapabilities) {
		this.serverCapabilities = serverCapabilities;
	}

	public int getServerCharsetIndex() {
		return serverCharsetIndex;
	}

	public void setServerCharsetIndex(int serverCharsetIndex) {
		this.serverCharsetIndex = serverCharsetIndex;
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}

	public byte[] getRestOfScrambleBuff() {
		return restOfScrambleBuff;
	}

	public void setRestOfScrambleBuff(byte[] restOfScrambleBuff) {
		this.restOfScrambleBuff = restOfScrambleBuff;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HandshakeBean [protocolVersion=");
		builder.append(protocolVersion);
		builder.append(", serverVersion=");
		builder.append(Arrays.toString(serverVersion));
		builder.append(", serverThreadId=");
		builder.append(serverThreadId);
		builder.append(", challengeRandom=");
		builder.append(Arrays.toString(challengeRandom));
		builder.append(", serverCapabilities=");
		builder.append(serverCapabilities);
		builder.append(", serverCharsetIndex=");
		builder.append(serverCharsetIndex);
		builder.append(", serverStatus=");
		builder.append(serverStatus);
		builder.append(", restOfScrambleBuff=");
		builder.append(Arrays.toString(restOfScrambleBuff));
		builder.append("]");
		return builder.toString();
	}

}
