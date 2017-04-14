package com.kk.nio.mysql.packhandler.bean.pkg;

import java.util.Arrays;

/**
 * 
 * 错误响应包
 * 
 * @since 2017年4月11日 下午8:36:34
 * @version 0.0.1
 * @author liujun
 */
public class ErrorPackageBean extends PackageHeader {

	/**
	 * Error报文，值恒为0xFF
	 * 
	 * @字段说明 code
	 */
	private byte rspFlag = (byte) 0xff;

	/**
	 * 错误编号（小字节序）
	 */
	private int errorNo;

	/**
	 * 服务器状态标志，恒为'#'字符
	 */
	private byte mark;

	/**
	 * 
	 * 
	 * 服务器状态（5个字符）
	 * 
	 * @字段说明 serverStatus
	 */
	private byte[] serverStatus;

	/**
	 * 响应信息
	 * 
	 * @字段说明 message
	 */
	public byte[] message;

	public byte getRspFlag() {
		return rspFlag;
	}

	public void setRspFlag(byte rspFlag) {
		this.rspFlag = rspFlag;
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}

	public byte getMark() {
		return mark;
	}

	public void setMark(byte mark) {
		this.mark = mark;
	}

	public byte[] getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(byte[] serverStatus) {
		this.serverStatus = serverStatus;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorPackageBean [rspFlag=");
		builder.append(rspFlag);
		builder.append(", errorNo=");
		builder.append(errorNo);
		builder.append(", mark=");
		builder.append(mark);
		builder.append(", serverStatus=");
		builder.append(Arrays.toString(serverStatus));
		builder.append(", message=");
		builder.append(Arrays.toString(message));
		builder.append("]");
		return builder.toString();
	}

}
