package com.kk.nio.mysql.servicehandler.flow.query;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.PkgWriteProcessEnum;
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

	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先是解析服务端响应的头
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_RUERY_RSP_HEADER.getPkgRead());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
