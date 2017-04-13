package com.kk.nio.mysql.chain;

import java.io.IOException;

import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;

/**
 * 进行消息的编码以及解码处理
 * 
 * @since 2017年3月29日 下午5:31:05
 * @version 0.0.1
 * @author liujun
 */
public class ReactorMysqlEnDecodeHandler implements MsgEnDecodeInf<PackageHeader> {

	/**
	 * 消息处理的接口
	 */
	private final MsgBaseInf msgBase;

	public ReactorMysqlEnDecodeHandler(MsgBaseInf msgBase) {
		super();
		this.msgBase = msgBase;
	}

	@Override
	public void msgEncode(MysqlContext context) throws IOException {
		// 将消息进行编码再发送,将按 mysql的消息体进行组包
		if (context.getWritePkgHandler().getpackageSize(context.getWriteData()) > 0) {
			context.getWritePkgHandler().packageToBuffer(context.getWriteData());
		}

		// 进行消息的发送
		msgBase.writeData(context);

	}

	@Override
	public PackageHeader msgDecode(MysqlContext context) throws IOException {
		// 进行消息的解码操作,首先进行消息的读取
		msgBase.readData(context);

		// 进行当前的包我检查
		if (context.getReadPkgHandler().checkpackageOver(context)) {
			// 如果检查完成，则进行包的解析,并返回
			return context.getReadPkgHandler().readPackage(context);
		}

		return null;

	}

}
