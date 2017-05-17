package com.kk.nio.mysql.servicehandler.flow.queryResultSet;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.console.ServerStatusEnum;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.EofPackageBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * 进行eof包的解析处理,代表当前的行结束
 * 
 * @since 2017年4月20日 上午12:09:41
 * @version 0.0.1
 * @author kk
 */
public class MysqlQueryRspStateEofRowOverHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void pkgRead(MysqlStateContext context) throws IOException {

		EofPackageBean bean = (EofPackageBean) this.readDataDef(context);

		// eof包解析完成，解析下一个数据信息
		if (null != bean) {
			// 如果当前的状态中，标识出还有更多的查询结果集，则进行下一次的解析
			if (ServerStatusEnum.StatusCheck(bean.getStatusFlag(), ServerStatusEnum.MULT_QUERY)) {
				// 将进行下一次的结果解析
				context.setCurrMysqlState(MysqlStateEnum.PKG_PROC_RSP_OK_CHECK.getState());
			}
		}
	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先是解析服务端响应的头
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_RSP_EOF.getPkgRead());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
