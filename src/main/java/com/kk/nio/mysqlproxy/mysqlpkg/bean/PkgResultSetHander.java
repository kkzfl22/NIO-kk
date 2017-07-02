package com.kk.nio.mysqlproxy.mysqlpkg.bean;

/**
 * Result Set 消息体
 * 
 * 
 *[Result Set Header]列数量 当前消息体
 *[Field]列信息（多个）
 *[EOF]列结束
 *[Row Data]行数据（多个）
 *[EOF]数据结束
*/
public class PkgResultSetHander extends PkgHeader {

    /**
     * 列数量
    * @字段说明 fixCount
    */
    private int fieldCount;

    /**
     * 额外的数据
    * @字段说明 otherData
    */
    private long otherData;

	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public long getOtherData() {
		return otherData;
	}

	public void setOtherData(long otherData) {
		this.otherData = otherData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultSetHanderBean [fieldCount=");
		builder.append(fieldCount);
		builder.append(", otherData=");
		builder.append(otherData);
		builder.append("]");
		return builder.toString();
	}
    

}
