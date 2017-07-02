package com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state.auth;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.demo.midd.handler.blackmysqlconn.MysqlIoStateEnum;
import com.kk.nio.demo.midd.util.ByteBufferTools;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.BlackMysqlIOStateContext;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.BlackMysqlIOStateInf;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.MysqIOStateEnum;

/**
 * 进行登录的结果检查
 * 
 * @since 2017年6月23日 下午3:04:35
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIoStateAuthRsp implements BlackMysqlIOStateInf {

	@Override
	public void doRead(BlackMysqlIOStateContext handler) throws IOException {

		ByteBuffer buffer = handler.getMysqlConnStateContext().getReadBuffer();

		int readPos = handler.getMysqlConnStateContext().getChannel().read(buffer);

		// 当前读取成功
		if (readPos > 0) {
			// 进行检查当前登录包信息
			boolean check = ByteBufferTools.checkLength(buffer, 0);

			// 如果当前数据检查成功，则进行前端流程的写入操作
			if (check) {
				handler.getMysqlConnStateContext().setReadPostion(buffer.position());
				// 将后端的读取事件取消
				handler.getMysqlConnStateContext().eventRigCancelRead();
				// 进行中间件的前端写入事件注册
				handler.getMysqlConnStateContext().getMidConnHandler().eventRigOpenWrite();

				// 完成，将当前的切换为查询版本状态处理
				handler.setCurrState(MysqIOStateEnum.BLACLMYSQLIOSTATE_SELVERSION.getMysqlIOState());
			}

		}

	}

	@Override
	public void doWrite(BlackMysqlIOStateContext handler) throws IOException {
	}

}