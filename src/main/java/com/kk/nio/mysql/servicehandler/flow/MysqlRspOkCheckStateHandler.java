package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.util.BufferPrint;

/**
 * 进行存储过程的响应的解析
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlRspOkCheckStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	@Override
	public void setRWPkgHandler(MysqlStateContext context) {

	}

	@Override
	public void pkgRead(MysqlStateContext mysqlContext) throws IOException {

		MysqlContext context = mysqlContext.getContext();

		// 进行清理
		if (mysqlContext.getContext().getReadBuffer().limit() == mysqlContext.getContext().getReadBuffer().capacity()
				|| mysqlContext.getContext().getReadBuffer().position() == mysqlContext.getContext().getReadBuffer()
						.limit()) {
			mysqlContext.getContext().getReadBuffer().clear();
		}

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

		// 如果当前是成功响应包，则进行数据解析完毕操作
		if (MysqlStateEnum.PKG_OK.getFlag() == flag) {
			// 从解析程序中找到运行的流程
			MysqlStateInf mysqlState = MysqlStateEnum.getState(flag);
		}
		// 否则进行数据的查询结果解析
		else {
			// 进行查询结果头的解析操作
			mysqlContext.setCurrMysqlState(MysqlStateEnum.PKG_QUERY_RSP_HEADER.getState());
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
