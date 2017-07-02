package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.console;

/**
 * 业务流程的参数配制
 * 
 * @since 2017年7月2日 下午11:35:10
 * @version 0.0.1
 * @author liujun
 */
public enum ServFlowEnum {

	/**
	 * 查询结果集中返回的列数
	 */
	STATE_RESULTSET_FIELDNUM("state_result_fieldnum"),;

	/**
	 * 参数配制信息
	 */
	private String key;

	private ServFlowEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
