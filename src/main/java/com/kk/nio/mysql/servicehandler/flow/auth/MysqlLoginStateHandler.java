package com.kk.nio.mysql.servicehandler.flow.auth;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.kk.nio.mysql.chain.MysqlContext;
import com.kk.nio.mysql.console.MysqlStateEnum;
import com.kk.nio.mysql.console.PropertiesKeyEnum;
import com.kk.nio.mysql.packhandler.PkgReadProcessEnum;
import com.kk.nio.mysql.packhandler.PkgWriteProcessEnum;
import com.kk.nio.mysql.packhandler.bean.pkg.AuthPackageBean;
import com.kk.nio.mysql.packhandler.bean.pkg.HandshakeBean;
import com.kk.nio.mysql.packhandler.common.SecurityUtil;
import com.kk.nio.mysql.packhandler.console.Capabilities;
import com.kk.nio.mysql.servicehandler.flow.MysqlHandlerStateBase;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateContext;
import com.kk.nio.mysql.servicehandler.flow.MysqlStateInf;
import com.kk.nio.mysql.util.PropertiesUtils;

/**
 * 进行登录的状态的消息处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class MysqlLoginStateHandler extends MysqlHandlerStateBase implements MysqlStateInf {

	public void pkgHandler(MysqlStateContext mysqlContext) throws IOException {

		MysqlContext context = mysqlContext.getContext();

		// 进行消息的解码操作,即为服务器首次写入的消息
		HandshakeBean msg = (HandshakeBean) this.readDataDef(context);

		// 组装用户消息
		AuthPackageBean auth = new AuthPackageBean();
		// 设置当前的包顺序为1
		auth.setSeq((byte) 1);
		// 设置客户端的标识
		auth.setClientFlag(initClientFlags());
		// 字符编码
		auth.setCharSetIndex(0);
		// 用户
		auth.setUserName(PropertiesUtils.getInstance().getValue(PropertiesKeyEnum.MYSQL_USERNAME));
		// 密码
		auth.setPasswd(this.passwd(PropertiesUtils.getInstance().getValue(PropertiesKeyEnum.MYSQL_PASSWORD), msg));
		// 设置数据库
		auth.setDataBase(PropertiesUtils.getInstance().getValue(PropertiesKeyEnum.MYSQL_DATABASE));

		// 设置写入数据库的信息
		context.setWriteData(auth);

		// 进行消息的写入操作
		this.writeDataDef(context);

		// 鉴权完成，进行设置状态为登录鉴权结果处理
		mysqlContext.setCurrMysqlState(MysqlStateEnum.PGK_COMM.getState());
	}

	/**
	 * 进行客户端的权能标识 方法描述
	 * 
	 * @return
	 * @创建日期 2016年12月13日
	 */
	private static long initClientFlags() {
		int flag = 0;
		flag |= Capabilities.CLIENT_LONG_PASSWORD;
		flag |= Capabilities.CLIENT_FOUND_ROWS;
		flag |= Capabilities.CLIENT_LONG_FLAG;
		flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
		// flag |= Capabilities.CLIENT_NO_SCHEMA;
		boolean usingCompress = false;
		if (usingCompress) {
			flag |= Capabilities.CLIENT_COMPRESS;
		}
		flag |= Capabilities.CLIENT_ODBC;
		flag |= Capabilities.CLIENT_LOCAL_FILES;
		flag |= Capabilities.CLIENT_IGNORE_SPACE;
		flag |= Capabilities.CLIENT_PROTOCOL_41;
		flag |= Capabilities.CLIENT_INTERACTIVE;
		// flag |= Capabilities.CLIENT_SSL;
		flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
		flag |= Capabilities.CLIENT_TRANSACTIONS;
		// flag |= Capabilities.CLIENT_RESERVED;
		flag |= Capabilities.CLIENT_SECURE_CONNECTION;
		// client extension
		flag |= Capabilities.CLIENT_MULTI_STATEMENTS;
		flag |= Capabilities.CLIENT_MULTI_RESULTS;
		return flag;
	}

	/**
	 * 进行密码的组装
	 * 
	 * @param pass
	 *            密码信息
	 * @param hs
	 *            连接信息
	 * @return 密码信息
	 * @throws IOException
	 */
	private byte[] passwd(String pass, HandshakeBean hs) throws IOException {
		if (pass == null || pass.length() == 0) {
			return null;
		}
		byte[] passwd = pass.getBytes();
		int sl1 = hs.getChallengeRandom().length;
		int sl2 = hs.getRestOfScrambleBuff().length;
		byte[] seed = new byte[sl1 + sl2];
		System.arraycopy(hs.getChallengeRandom(), 0, seed, 0, sl1);
		System.arraycopy(hs.getRestOfScrambleBuff(), 0, seed, sl1, sl2);
		try {
			return SecurityUtil.scramble411(passwd, seed);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void setRWPkgHandler(MysqlStateContext mysqlContext) {
		MysqlContext context = mysqlContext.getContext();
		// 首先接收到服务器端的握手包
		context.setReadPkgHandler(PkgReadProcessEnum.PKG_READ_HANDSHAKE.getPkgRead());
		// 设置向服务器的鉴权包
		context.setWritePkgHandler(PkgWriteProcessEnum.PKG_WRITE_AUTH.getPkgWrite());
	}

}
