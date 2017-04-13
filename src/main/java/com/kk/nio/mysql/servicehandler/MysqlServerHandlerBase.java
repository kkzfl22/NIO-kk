package com.kk.nio.mysql.servicehandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.kk.nio.mysql.chain.MsgEnDecodeInf;
import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.PackageHeader;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageWriteInf;

/**
 * 用来进行mysql的数据的包的处理
 * 
 * @since 2017年4月13日 下午8:16:29
 * @version 0.0.1
 * @author liujun
 */
public abstract class MysqlServerHandlerBase implements MsgDataServiceInf {

	/**
	 * 消息编解码对象信息
	 */
	private final MsgEnDecodeInf<PackageHeader> msgEndecode;

	/**
	 * 抽象类的构造，用于指定消息的解码与编码类
	 * 
	 * @param msgEndecode
	 */
	public MysqlServerHandlerBase(MsgEnDecodeInf<PackageHeader> msgEndecode) {
		this.msgEndecode = msgEndecode;
	}

	/**
	 * 获取读取package并解析的处理吕
	 * 
	 * @return
	 */
	public abstract MysqlPackageReadInf<? extends PackageHeader> getReadPackage();

	/**
	 * 获取写入的package
	 * 
	 * @return
	 */
	public abstract MysqlPackageWriteInf<? extends PackageHeader> getWritePackage();

	public PackageHeader readDataBase(MysqlContext context) throws IOException {
		// 进行消息的解码操作,即为服务器首次写入的消息
		return this.msgEndecode.msgDecode(context);

	}

	@Override
	public void writeData(MysqlContext context) throws IOException {
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
