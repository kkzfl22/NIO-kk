package com.kk.nio.mysql.servicehandler.flow.auth;

import java.io.IOException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.ErrorPackageBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;

/**
 * 进行失败的报文处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlErrorStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {

		// 解析出来成功的连接信息
		ErrorPackageBean msg = (ErrorPackageBean) this.readDataDef(mysqlContext);

		if (null != msg) {
			System.out.println("error msg:"+new String(msg.getServerStatus()));
			System.out.println("error msg:"+new String(msg.getMessage(),"UTF-8"));
		}
	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先接收到服务器端的握手包
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_READ_ERROR.getPkgRead());
	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
