package com.kk.nio.mysql.packhandler.bean.pkg.resultset;

import java.util.ArrayList;
import java.util.List;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * MySQL 4.1 及之后的版本 结构 说明 [Result Set Header] 列数量 [Field] 列信息（多个） [EOF] 列结束 [Row
 * Data] 行数据（多个） [EOF] 数据结束
 */
public class RowDataPackageBean extends PackageHeader {

	/**
	 * 列数量
	 * 
	 * @字段说明 columnNum
	 */
	private int columnNum;

	/**
	 * 列的值
	 * 
	 * @字段说明 fieldValue
	 */
	private final List<byte[]> fieldValue;

	public RowDataPackageBean() {
		fieldValue = new ArrayList<>();
	}

	public RowDataPackageBean(int columnNum) {
		super();
		this.columnNum = columnNum;
		// 固定列长度
		this.fieldValue = new ArrayList<>(columnNum);
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public List<byte[]> getFieldValue() {
		return fieldValue;
	}
	
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RowDataPackageBean [columnNum=");
		builder.append(columnNum);
		builder.append(", fieldValue=");
		builder.append(fieldValue);
		builder.append("]");
		return builder.toString();
	}

}
