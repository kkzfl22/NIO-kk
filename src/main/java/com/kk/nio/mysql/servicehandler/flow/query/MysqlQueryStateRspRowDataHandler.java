package com.kk.nio.mysql.servicehandler.flow.query;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * 进行数据查询的状态
 * 
 * @since 2017年4月23日 下午2:06:54
 * @version 0.0.1
 * @author kk
 */
public class MysqlQueryStateRspRowDataHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先是解析服务端响应的头
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_RSP_QUERY_ROW_DATA.getPkgRead());
	}

	@Override
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {
		
		
	}

	@Override
	public void pkgWrite(MysqlStateContext mysqlContext) throws IOException {

	}

}
