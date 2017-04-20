package com.kk.nio.mysql.servicehandler.flow.query;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
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

		int colNum = (int) context.getResult();
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
			context.setCurrMysqlState(null);
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
