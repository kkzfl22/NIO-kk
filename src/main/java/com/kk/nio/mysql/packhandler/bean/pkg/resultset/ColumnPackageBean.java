package com.kk.nio.mysql.packhandler.bean.pkg.resultset;

import java.util.Arrays;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * MySQL 4.1 及之后的版本 结构 说明 
 * [Result Set Header] 
 * 列数量 [Field] 列信息（多个） 
 * [EOF] 列结束 
 * [Row * Data] 行数据（多个） 
 * [EOF] 数据结束 
 */
public class ColumnPackageBean extends PackageHeader {

	/**
	 * 目录名称：在4.1及之后的版本中，该字段值为"def"。
	 * 
	 * @字段说明 dirName
	 */
	private byte[] dirName;

	/**
	 * 数据库名称（Length Coded String）数据库名称：数据库名称标识。
	 * 
	 * @字段说明 dataBaseName
	 */
	private byte[] dataBaseName;

	/**
	 * 数据表名称：数据表的别名（AS之后的名称）。
	 * 
	 * @字段说明 tableName
	 */
	private byte[] tableAsName;

	/**
	 * 数据表原始名称：数据表的原始名称（AS之前的名称）。
	 * 
	 * @字段说明 tableBeforeName
	 */
	private byte[] tableBeforeName;

	/**
	 * 列（字段）名称：列（字段）的别名（AS之后的名称）。
	 * 
	 * @字段说明 columnName
	 */
	private byte[] columnAsName;

	/**
	 * 列（字段）原始名称：列（字段）的原始名称（AS之前的名称）。
	 * 
	 * @字段说明 columnBeforeName
	 */
	private byte[] columnBeforeName;

	/**
	 * 字符编码：列（字段）的字符编码值。
	 * 
	 * @字段说明 charSet
	 */
	private int charSet;

	/**
	 * 列（字段）长度：列（字段）的长度值，真实长度可能小于该值，例如VARCHAR(2)类型的字段实际只能存储1个字符。
	 * 
	 * @字段说明 columnLength
	 */
	private long columnLength;

	/**
	 * 列（字段）类型：列（字段）的类型值，取值范围如下（参考源代码/include/mysql_com.
	 * h头文件中的enum_field_type枚举类型定义）：
	 * 
	 * @字段说明 columnType
	 */
	private int columnType;

	/**
	 * 列（字段）标志：各标志位定义如下（参考源代码/include/mysql_com.h头文件中的宏定义）：
	 * 
	 * @字段说明 columnFlag
	 */
	private int columnFlag;

	/**
	 * 数值精度：该字段对DECIMAL和NUMERIC类型的数值字段有效，用于标识数值的精度（小数点位置）。
	 * 
	 * @字段说明 decimal
	 */
	private byte decimalNum;

	/**
	 * 默认值：该字段用在数据表定义中，普通的查询结果中不会出现。
	 * 
	 * @字段说明 defValue
	 */
	private byte[] defValue;

	public byte[] getDirName() {
		return dirName;
	}

	public void setDirName(byte[] dirName) {
		this.dirName = dirName;
	}

	public byte[] getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(byte[] dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public byte[] getTableAsName() {
		return tableAsName;
	}

	public void setTableAsName(byte[] tableAsName) {
		this.tableAsName = tableAsName;
	}

	public byte[] getTableBeforeName() {
		return tableBeforeName;
	}

	public void setTableBeforeName(byte[] tableBeforeName) {
		this.tableBeforeName = tableBeforeName;
	}

	public byte[] getColumnAsName() {
		return columnAsName;
	}

	public void setColumnAsName(byte[] columnAsName) {
		this.columnAsName = columnAsName;
	}

	public byte[] getColumnBeforeName() {
		return columnBeforeName;
	}

	public void setColumnBeforeName(byte[] columnBeforeName) {
		this.columnBeforeName = columnBeforeName;
	}

	public int getCharSet() {
		return charSet;
	}

	public void setCharSet(int charSet) {
		this.charSet = charSet;
	}

	public long getColumnLength() {
		return columnLength;
	}

	public void setColumnLength(long columnLength) {
		this.columnLength = columnLength;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public int getColumnFlag() {
		return columnFlag;
	}

	public void setColumnFlag(int columnFlag) {
		this.columnFlag = columnFlag;
	}

	public byte getDecimalNum() {
		return decimalNum;
	}

	public void setDecimalNum(byte decimalNum) {
		this.decimalNum = decimalNum;
	}

	public byte[] getDefValue() {
		return defValue;
	}

	public void setDefValue(byte[] defValue) {
		this.defValue = defValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnPackageBean [dirName=");
		builder.append(Arrays.toString(dirName));
		builder.append(", dataBaseName=");
		builder.append(Arrays.toString(dataBaseName));
		builder.append(", tableAsName=");
		builder.append(Arrays.toString(tableAsName));
		builder.append(", tableBeforeName=");
		builder.append(Arrays.toString(tableBeforeName));
		builder.append(", columnAsName=");
		builder.append(Arrays.toString(columnAsName));
		builder.append(", columnBeforeName=");
		builder.append(Arrays.toString(columnBeforeName));
		builder.append(", charSet=");
		builder.append(charSet);
		builder.append(", columnLength=");
		builder.append(columnLength);
		builder.append(", columnType=");
		builder.append(columnType);
		builder.append(", columnFlag=");
		builder.append(columnFlag);
		builder.append(", decimalNum=");
		builder.append(decimalNum);
		builder.append(", defValue=");
		builder.append(Arrays.toString(defValue));
		builder.append("]");
		return builder.toString();
	}

}
