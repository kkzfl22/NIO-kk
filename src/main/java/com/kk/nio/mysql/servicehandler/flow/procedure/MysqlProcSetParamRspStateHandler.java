package com.kk.nio.mysql.servicehandler.flow.procedure;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.QueryPackageBean;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;
import com.kk.nio.mysql.util.BufferPrint;

/**
 * 进行存储过程的设置参数的响应解析
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlProcSetParamRspStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void setRWPkgHandler(MysqlStateContext context) {

	}

	@Override
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {

		MysqlContext context = mysqlContext.getContext();

		// 进行清理
		mysqlContext.getContext().getReadBuffer().clear();

		// 设置上下文处理时不需要进行消息的解码,在当前操作完成，
		// 需要将解码器设置为true ,以上下一次可以进行解码,仅临时使用
		mysqlContext.getContext().setIsdecoder(false);
		// 进行消息流的读取
		mysqlContext.getMsgEndecode().msgDecode(context);
		// 解码完成，下次设置为需要解码
		mysqlContext.getContext().setIsdecoder(true);

		ByteBuffer buffer = context.getReadBuffer();

		// 取得消息响应的类型检查消息的类型
		byte flag = buffer.get(4);

		BufferPrint.print(buffer);

		if (MysqlStateEnum.PKG_ERROR.getFlag() == flag) {
			// 进行错误的读取
			mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_ERROR.getState());
			// 设置数据的解析程序
			mysqlContext.getCurrMysqlState().setRWPkgHandler(mysqlContext);
			// 进行运行流程
			mysqlContext.getCurrMysqlState().pkgRead(mysqlContext);
		}
		// 否则，进行查询解析的流程
		else {
			// 进行成功消息的读取
			mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_OK.getState());
			// 设置数据的解析程序
			mysqlContext.getCurrMysqlState().setRWPkgHandler(mysqlContext);
			// 进行运行流程
			mysqlContext.getCurrMysqlState().pkgRead(mysqlContext);
		}

		// 将进行存储过程的调用
		this.pkgWrite(mysqlContext);

	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {
		// 组装查mysql的存储过程调用的查询包
		QueryPackageBean queryPkg = new QueryPackageBean();

		queryPkg.setSeq((byte) 0);
		queryPkg.setFlag((byte) 0x03);
		queryPkg.setQueryStr("CALL demo_in_parameter(@p_in);".getBytes());

		// 交给对应的流程去发送
		context.getContext().setWriteData(queryPkg);

		// 进行发送的流程
		this.writeDataDef(context);

		// 检查是否已经发送完成,如果发送完成，则设置查询结果的头解析程序
		if (context.getContext().getWriteBuffer().position() == 0) {
			context.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_RSP_HEADER.getState());
		}
	}

}
