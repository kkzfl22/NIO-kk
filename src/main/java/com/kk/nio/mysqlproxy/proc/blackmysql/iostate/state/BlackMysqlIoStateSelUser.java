package com.kk.nio.mysqlproxy.proc.blackmysql.iostate.state;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.kk.nio.demo.midd.util.ByteBufferTools;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.BlackMysqlIOStateContext;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.BlackMysqlIOStateInf;
import com.kk.nio.mysqlproxy.proc.blackmysql.iostate.MysqIOStateEnum;

/**
 * 进行握手包协议的操作
 * 
 * @since 2017年6月23日 下午3:04:35
 * @version 0.0.1
 * @author liujun
 */
public class BlackMysqlIoStateSelUser implements BlackMysqlIOStateInf {

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
				handler.getMysqlConnStateContext().getMidConnHandler().eventRigCancelReadOpenWrite();
			}

		}

	}

	@Override
	public void doWrite(BlackMysqlIOStateContext handler) throws IOException {
		if (handler.getMysqlConnStateContext().getWritePosition() > 0) {
			// 检查当前前端写入的信息
			ByteBuffer writeBuffer = handler.getMysqlConnStateContext().getWriteBuffer();

			int pos = writeBuffer.position();
			writeBuffer.position(0);
			writeBuffer.limit(pos);

			int writePos = handler.getMysqlConnStateContext().getChannel().write(writeBuffer);

			// 如果当前写入成功，则将后端进行切换为鉴权结果检查
			if (writePos == writeBuffer.position()) {
				writeBuffer.clear();
				handler.getMysqlConnStateContext().setWritePosition(0);

				// 将当前的写入事件取消，切换为读取事件
				handler.getMysqlConnStateContext().eventRigCancelWriteOpenRead();
			}
		}
	}

}
