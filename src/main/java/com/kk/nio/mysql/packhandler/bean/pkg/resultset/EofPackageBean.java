package com.kk.nio.mysql.packhandler.bean.pkg.resultset;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束
 * [Row Data] 行数据（多个） [EOF] 数据结束 
 */
public class EofPackageBean extends PackageHeader {

	/**
	 * 报文的结果
	 * 
	 * @字段说明 DEFVALUE
	 */
	public static final byte DEFVALUE = (byte) 0xfe;

	/**
	 * EOF值（0xFE）
	 * 
	 * @字段说明 eofValue
	 */
	private byte eofValue;

	/**
	 * 告警计数：服务器告警数量，在所有数据都发送给客户端后该值才有效。
	 * 
	 * @字段说明 warnCount
	 */
	private int warnCount;

	/**
	 * 状态标志位：包含类似SERVER_MORE_RESULTS_EXISTS这样的标志位。
	 * 
	 * @字段说明 statusFlag
	 */
	private int statusFlag;

	public byte getEofValue() {
		return eofValue;
	}

	public void setEofValue(byte eofValue) {
		this.eofValue = eofValue;
	}

	public int getWarnCount() {
		return warnCount;
	}

	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public static byte getDefvalue() {
		return DEFVALUE;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EofPackageBean [eofValue=");
		builder.append(eofValue);
		builder.append(", warnCount=");
		builder.append(warnCount);
		builder.append(", statusFlag=");
		builder.append(statusFlag);
		builder.append("]");
		return builder.toString();
	}

}
