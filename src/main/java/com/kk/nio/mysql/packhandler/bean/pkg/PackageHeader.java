package com.kk.nio.mysql.packhandler.bean.pkg;

/**
 * 协议头信息部分
 */
public class PackageHeader {

	/**
	 * 1获取长度信息, 占3位,此消息的长度信息,此消息的最大长度为255的3次方，即为16M
	 * 
	 * @字段说明 length
	 */
	protected int length;

	/**
	 * 获取到通讯序号,第四位
	 * 
	 * @字段说明 se
	 */
	protected byte seq;

	/**
	 * 数据具体内容,在第一次通讯的时候，需要解读响应的报文类型信息,首次为10，####
	 * 
	 * @字段说明 msgtype
	 */
	protected int msgtype;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte getSeq() {
		return seq;
	}

	public void setSeq(byte seq) {
		this.seq = seq;
	}

	public int getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}

}
