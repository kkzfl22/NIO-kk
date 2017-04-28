package com.kk.nio.mysql.servicehandler.flow.query;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;
import com.kk.nio.mysql.util.BufferPrint;

/**
 * 进行登录的状态的消息处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlQueryRspCommStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

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
			// 检查当前是否为公共解析,则首先进行头的解析，后面依次进行解析
			if (mysqlContext.getCurrMysqlState().equals(MysqlStateEnum.PKG_QUERY_RSP_CHECK.getState())) {
				// 进行正常的结果流程解析
				mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_RSP_HEADER.getState());
			}
			// 设置数据的解析程序
			mysqlContext.getCurrMysqlState().setRWPkgHandler(mysqlContext);
			// 进行运行流程
			mysqlContext.getCurrMysqlState().pkgRead(mysqlContext);

		}

	}

	@Override
	public void pkgWrite(MysqlStateContext context) throws IOException {

	}

}
