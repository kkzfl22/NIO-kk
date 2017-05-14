package com.kk.nio.mysql.servicehandler.flow.queryResultSet;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.FlowKeyEnum;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.ColumnPackageBean;
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
public class MysqlQueryRspStateColumnHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void pkgRead(MysqlStateContext context) throws IOException {

		// 获得列数
		int colNum = (int) context.getContext().getMapData(FlowKeyEnum.QUERY_RSP_HEADER_COUNT.getKey());

		ColumnPackageBean[] columnArray = new ColumnPackageBean[colNum];

		ColumnPackageBean item = null;

		boolean readOk = false;

		for (int i = 0; i < colNum; i++) {
			// 进行读取查询的响应的列信息
			item = (ColumnPackageBean) this.readDataDef(context);
			if (null != item) {
				columnArray[i] = item;
				readOk = true;
			} else {
				readOk = false;
				break;
			}
		}

		// 列读取完成，则继续下一个流程，即为进行eof包的读取
		if (readOk) {

			// 将列信息记录到流程中
			context.getContext().setMapData(FlowKeyEnum.QUERY_RSP_COLUMN_MSG.getKey(), columnArray);

			// 列读取完成，进行eof消息读取
			context.setCurrMysqlState(MysqlStateEnum.PKG_RSP_EOF.getState());
		}

	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先是解析服务端响应的头
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_QUERY_REP_COLUMN.getPkgRead());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
