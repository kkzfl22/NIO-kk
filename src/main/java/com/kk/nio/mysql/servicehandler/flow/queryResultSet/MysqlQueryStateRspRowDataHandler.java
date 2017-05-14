package com.kk.nio.mysql.servicehandler.flow.queryResultSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.FlowKeyEnum;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.resultset.RowDataPackageBean;
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
		// 进行协议消息的解析
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_RSP_QUERY_ROW_DATA.getPkgRead());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {
		// 进行读取结果
		RowDataPackageBean dataMsg = (RowDataPackageBean) this.readDataDef(mysqlContext);

		if (null != dataMsg) {
			List<RowDataPackageBean> list = (List<RowDataPackageBean>) mysqlContext.getContext()
					.getMapData(FlowKeyEnum.QUERY_RSP_ROWDATA_MSG.getKey());
			// 将读取结果设置到上下文中，以供列读取使用
			if (null == list) {
				list = new ArrayList<>();
			}

			list.add(dataMsg);

			mysqlContext.getContext().setMapData(FlowKeyEnum.QUERY_RSP_ROWDATA_MSG.getKey(), list);
		}
		
	}

	@Override
	public void pkgWrite(MysqlStateContext mysqlContext) throws IOException {

	}

}
