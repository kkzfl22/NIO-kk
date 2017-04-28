package com.kk.nio.mysql.console;

/**
 * 进行流程中所使用的key的标识
 * @since 2017年4月23日 下午5:58:11
 * @version 0.0.1
 * @author kk
 */
public enum FlowKeyEnum {
	
	
	/**
	 * 进行查询响应时，标识出的列数
	 */
	QUERY_RSP_HEADER_COUNT("query_rsp_header_count"),
	
	
	/**
	 * 解析列出来的列信息
	 */
	QUERY_RSP_COLUMN_MSG("query_rsp_column_msg"),
	
	
	/**
	 * 行数据
	 */
	QUERY_RSP_ROWDATA_MSG("query_rsp_rowdata_msg"),
	
	
	/**
	 * 列检查标识
	 */
	QUERY_RSP_COLUMN_CHECK_FLAG("query_rsp_column_check_flag"),
	
	;
	
	
	/**
	 * 流程中的key的标识
	 */
	private String key;

	
	
	private FlowKeyEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	

}
