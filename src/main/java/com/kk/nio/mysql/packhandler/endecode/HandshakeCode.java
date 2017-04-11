package com.kk.nio.mysql.packhandler.endecode;

import java.nio.ByteBuffer;

import com.kk.nio.mysql.packhandler.MysqlDataPackageReadInf;
import com.kk.nio.mysql.packhandler.bean.pkg.HandshakeBean;
import com.kk.nio.mysql.packhandler.common.MySQLMessage;

/**
 * 进行第一次的握手协议包 源文件名：
 * 
 * @since 2017年4月11日 下午8:47:18
 * @version 0.0.1
 * @author liujun
 */
public class HandshakeCode implements MysqlDataPackageReadInf<HandshakeBean> {

	@Override
	public HandshakeBean readPackage(ByteBuffer buffer) {

		HandshakeBean bean = new HandshakeBean();

		// 进行报文内容解析
		MySQLMessage msg = new MySQLMessage(buffer);

		// 1,首先读取长度
		bean.setLength(msg.readUB3());
		// 2,序列号
		bean.setSeq(msg.read());
		// 3，服务端版本号
		bean.setProtocolVersion(msg.read());
		// 4, 服务器版本号
		bean.setServerVersion(msg.readBytesWithNull());
		// 5 线程的id
		bean.setServerThreadId(msg.readUB4());
		// 6 挑战随机数
		bean.setChallengeRandom(msg.readBytesWithNull());
		// 7 服务器权能标志
		bean.setServerCapabilities(msg.readUB2());
		// 8 字符编码
		bean.setServerCharsetIndex(msg.read());
		// 9 服务器状态
		bean.setServerStatus(msg.readUB2());
		// 10 其他数据
		msg.move(13);
		bean.setRestOfScrambleBuff(msg.readBytesWithNull());

		return bean;
	}

	@Override
	public boolean checkpackageOver(ByteBuffer buffer) {
		// 进行包结束的检查
		if (buffer.position() != 0 && buffer.get(buffer.position()) == 0x00) {
			return true;
		}

		return false;
	}

}
