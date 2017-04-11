package com.kk.nio.mysql.packhandler.bean.pkg;

import java.util.Arrays;

/**
 * 
 * 进行第一次的握手协议包
 * 
 * @since 2017年4月11日 下午8:36:34
 * @version 0.0.1
 * @author liujun
 */
public class OkPackageBean extends PackageHeader {

	/**
	 * 返回包的结果,握手包的结果
	 * 
	 * @字段说明 code
	 */
	private byte rspCode;

	/**
	 * 行数信息
	 * 
	 * @字段说明 affectedRows
	 */
	private long affectedRows;

	/**
	 * 插入的id
	 * 
	 * @字段说明 insertId
	 */
	private long insertId;

	/**
	 * 服务状态
	 * 
	 * @字段说明 serverStatus
	 */
	private int serverStatus;

	/**
	 * 警告的条数
	 * 
	 * @字段说明 warningCount
	 */
	private int warningCount;

	/**
	 * 响应信息
	 * 
	 * @字段说明 message
	 */
	public byte[] message;

	public byte getRspCode() {
		return rspCode;
	}

	public void setRspCode(byte rspCode) {
		this.rspCode = rspCode;
	}

	public long getAffectedRows() {
		return affectedRows;
	}

	public void setAffectedRows(long affectedRows) {
		this.affectedRows = affectedRows;
	}

	public long getInsertId() {
		return insertId;
	}

	public void setInsertId(long insertId) {
		this.insertId = insertId;
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}

	public int getWarningCount() {
		return warningCount;
	}

	public void setWarningCount(int warningCount) {
		this.warningCount = warningCount;
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
		builder.append("OkPackageBean [rspCode=");
		builder.append(rspCode);
		builder.append(", affectedRows=");
		builder.append(affectedRows);
		builder.append(", insertId=");
		builder.append(insertId);
		builder.append(", serverStatus=");
		builder.append(serverStatus);
		builder.append(", warningCount=");
		builder.append(warningCount);
		builder.append(", message=");
		builder.append(Arrays.toString(message));
		builder.append("]");
		return builder.toString();
	}

}
