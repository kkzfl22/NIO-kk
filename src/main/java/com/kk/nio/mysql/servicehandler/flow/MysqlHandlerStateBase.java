package com.kk.nio.mysql.servicehandler.flow;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.kk.nio.mysql.chain.MsgEnDecodeInf;
import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 进行mysql各种流程的实现
 * 
 * @since 2017年4月13日 下午8:16:29
 * @version 0.0.1
 * @author liujun
 */
public abstract class MysqlHandlerStateBase {

	/**
	 * 消息编解码对象信息,在状态机所有内部状态被激活时，进行设置
	 */
	private MsgEnDecodeInf<PackageHeader> msgEndecode;

	public void setMsgEndecode(MsgEnDecodeInf<PackageHeader> msgEndecode) {
		this.msgEndecode = msgEndecode;
	}

	/**
	 * 提供默认的数据流读取
	 * 
	 * @param context
	 * @return
	 * @throws IOException
	 */
	protected PackageHeader readDataDef(MysqlContext context) throws IOException {
		// 进行消息的解码操作,即为服务器首次写入的消息
		return this.msgEndecode.msgDecode(context);

	}

	/**
	 * 默认的数据写的实现
	 * 
	 * @param context
	 * @throws IOException
	 */
	protected void writeDataDef(MysqlContext context) throws IOException {
		// 如果当前需要写入的数据不为空，则进行编码
		if (context.getWriteData() != null) {
			msgEndecode.msgEncode(context);
		}
		// 需要取消录写入事件，进行读取操作
		else {
			context.getSelectKey()
					.interestOps(context.getSelectKey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		}
	}

}
