package com.kk.nio.mysql.console;

import com.kk.nio.mysql.servicehandler.flow.MysqlCommStateHandler;
import com.kk.nio.mysql.servicehandler.flow.MysqlRspOkCheckStateHandler;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;
import com.kk.nio.mysql.servicehandler.flow.auth.MysqlErrorStateHandler;
import com.kk.nio.mysql.servicehandler.flow.auth.MysqlLoginStateHandler;
import com.kk.nio.mysql.servicehandler.flow.auth.MysqlOkStateHandler;
import com.kk.nio.mysql.servicehandler.flow.procedure.MysqlProcSetParamReqStateHandler;
import com.kk.nio.mysql.servicehandler.flow.procedure.MysqlProcSetParamRspStateHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryReqStateHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryRspCommStateHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryRspStateColumnHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryRspStateEofColomOverHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryRspStateEofRowOverHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryRspStateHearderHandler;
import com.kk.nio.mysql.servicehandler.flow.queryResultSet.MysqlQueryStateRspRowDataHandler;

/**
 * mysql的状态信息
 * 
 * @since 2017年4月14日 下午4:12:45
 * @version 0.0.1
 * @author liujun
 */
public enum MysqlStateEnum {

	/**
	 * 进行mysql登录的状态处理
	 */
	LOGIN_AUTH((byte) 1, new MysqlLoginStateHandler()),

	/**
	 * 进行mysql登录结果的处理
	 */
	PGK_COMM((byte) 1, new MysqlCommStateHandler()),

	/**
	 * 进行成功的状态的处理
	 */
	PKG_OK((byte) 0x00, new MysqlOkStateHandler()),

	/**
	 * 错误的处理
	 */
	PKG_ERROR((byte) 0xff, new MysqlErrorStateHandler()),

	/**
	 * 查询请求和状态处理
	 */
	PKG_QUERY_REQ((byte) 1, new MysqlQueryReqStateHandler()),

	/**
	 * 响应的正确性进行验证
	 */
	PKG_QUERY_RSP_CHECK((byte) 1, new MysqlQueryRspCommStateHandler()),

	/**
	 * 查询响应的状态头处理
	 */
	PKG_QUERY_RSP_HEADER((byte) 1, new MysqlQueryRspStateHearderHandler()),

	/**
	 * 查询响应的消息列处理
	 */
	PKG_QUERY_RSP_COLUMN((byte) 1, new MysqlQueryRspStateColumnHandler()),

	/**
	 * 进行消息响应的eof包的处理
	 */
	PKG_RSP_EOF((byte) 0xfe, new MysqlQueryRspStateEofColomOverHandler()),

	/**
	 * 进行消息的解码
	 */
	PKG_QUERY_RSP_ROWDATA_MSG((byte) 1, new MysqlQueryStateRspRowDataHandler()),
	
	
	/**
	 * 进行eof行数据结束处理
	 */
	PKG_QUERY_RSP_EOF_ROW_OVER((byte)1,new MysqlQueryRspStateEofRowOverHandler()),

	/**
	 * 存储过程中首要的参数设置结果解析
	 */
	PKG_PROC_PARAM_SET_REQ((byte) 1, new MysqlProcSetParamReqStateHandler()),
	
	/**
	 * 存储过程中首要的参数设置结果解析
	 */
	PKG_PROC_PARAM_SET_RSP((byte) 1, new MysqlProcSetParamRspStateHandler()),

	/**
	 * 进行响应包的完成验证
	 */
	PKG_PROC_RSP_OK_CHECK((byte) 0, new MysqlRspOkCheckStateHandler())

	;

	/**
	 * 报文标识
	 */
	private byte flag;

	/**
	 * 状态信息
	 */
	private MysqlStateInf state;

	private MysqlStateEnum(byte flag, MysqlStateInf state) {
		this.flag = flag;
		this.state = state;
	}

	public MysqlStateInf getState() {
		return state;
	}

	public void setState(MysqlStateInf state) {
		this.state = state;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public static MysqlStateInf getState(byte flag) {
		MysqlStateEnum[] vals = values();

		for (MysqlStateEnum stateEnum : vals) {
			if (stateEnum.flag == flag) {
				return stateEnum.getState();
			}
		}

		return null;
	}

}
