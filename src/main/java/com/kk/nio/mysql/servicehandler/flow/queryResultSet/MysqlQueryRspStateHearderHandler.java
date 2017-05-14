package com.kk.nio.mysql.servicehandler.flow.queryResultSet;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.FlowKeyEnum;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ResultSetHanderBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * 进行查询结果的头的响应解析
 * 
 * @since 2017年4月20日 上午12:09:41
 * @version 0.0.1
 * @author kk
 */
public class MysqlQueryRspStateHearderHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void pkgRead(MysqlStateContext context) throws IOException {
		// 进行读取查询的响应结果头
		ResultSetHanderBean handler = (ResultSetHanderBean) this.readDataDef(context);

		// 检查列头是否已经读取完整如果完整，则继教读取
		int fieldcount = handler.getFieldCount();

		// 当前已经读取成功，则可以切换状态到读取消息列信息
		if (0 != fieldcount) {
			// 将读取结果设置到上下文中，以供列读取使用
			context.getContext().setMapData(FlowKeyEnum.QUERY_RSP_HEADER_COUNT.getKey(), fieldcount);

			context.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_RSP_COLUMN.getState());
		}

	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先是解析服务端响应的头
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_QUERY_RSP_HEADER.getPkgRead());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
