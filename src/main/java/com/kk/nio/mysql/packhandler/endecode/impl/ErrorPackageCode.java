package com.kk.nio.mysql.packhandler.endecode.impl;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.packhandler.bean.pkg.ErrorPackageBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;
import com.kk.nio.mysql.packhandler.endecode.MysqlPackageReadInf;
import com.kk.nio.mysql.util.BufferPrint;

/**
 * 进行第一次的握手协议包
 * 
 * @since 2017年4月11日 下午8:48:29
 * @version 0.0.1
 * @author liujun
 */
public class ErrorPackageCode implements MysqlPackageReadInf {

	/**
	 * 状态标识值
	 */
	private static final byte SQLSTATE_MARKER = (byte) '#';

	@Override
	public ErrorPackageBean readPackage(MysqlContext context) {

		ErrorPackageBean pkgBean = new ErrorPackageBean();
		
		context.getReadBuffer().flip();
		
		MySQLMessage mm = new MySQLMessage(context.getReadBuffer());

		pkgBean.setLength(mm.readUB3());
		pkgBean.setSeq(mm.read());
		pkgBean.setRspFlag(mm.read());
		pkgBean.setErrorNo(mm.readUB2());
		if (mm.hasRemaining() && (mm.read(mm.position()) == SQLSTATE_MARKER)) {
			mm.read();
			pkgBean.setServerStatus(mm.readBytes(5));
		}
		pkgBean.setMessage(mm.readBytes());

		return pkgBean;
	}

	@Override
	public boolean checkpackageOver(MysqlContext context) {

		ByteBuffer buffer = context.getReadBuffer();
		
		BufferPrint.print(buffer);

		// 预读取buffer中的长度，与position做比较，进行完整性的检查
		int length = buffer.get(0) & 0xff;
		length |= (buffer.get(1) & 0xff) << 8;
		length |= (buffer.get(2) & 0xff) << 16;

		if (length+4 == buffer.position()) {
			return true;
		}

		return false;
	}
	
	public static void main(String[] args) {
			byte value = 0x33;
			System.out.println(value);
	}

}
