package com.kk.nio.mysql.servicehandler.flow.queryResultSet;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.packhandler.PkgWriteProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.OkPackageBean;
import com.kk.nio.mysql.packhandler.bean.pkg.QueryPackageBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * 进行成功的报文处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlQueryReqStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {

		// 重置position为0开始进行读取
		mysqlContext.getContext().getReadBuffer().position(0);
		// 解析出来成功的连接信息
		OkPackageBean msg = (OkPackageBean) this.readDataDef(mysqlContext);

		if (null != msg) {
			// 当前成功，切换为使用状态
			mysqlContext.setResult(true);
		}
	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 进行向服务器编码的包设置
		context.setWritePkgHandler(PkgWriteProcessEnum.PKG_WRITE_QUERY.getPkgWrite());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

		// 组装查询包
		QueryPackageBean queryPkg = new QueryPackageBean();

		queryPkg.setSeq((byte) 0);
		queryPkg.setFlag((byte) 0x03);
		queryPkg.setQueryStr("select * from user;".getBytes());

		// 交给对应的流程去发送
		context.getContext().setWriteData(queryPkg);

		// 进行发送的流程
		this.writeDataDef(context);

		// 检查是否已经发送完成,如果发送完成，则设置查询结果的头解析
		if (context.getContext().getWriteBuffer().position() == 0) {
			context.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_RSP_CHECK.getState());
		}

	}

}
