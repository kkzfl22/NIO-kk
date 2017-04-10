package com.kk.nio.mysql.chain;

import java.io.IOException;

/**
 * 数据的业务处理
 * 
 * @since 2017年3月29日 下午5:31:44
 * @version 0.0.1
 * @author liujun
 */
public interface MsgDataServiceInf {
	
	
	/**
	 * 进行业务数据读取操作
	 * @param context
	 * @throws IOException
	 */
	public void readData(MysqlContext context)throws IOException;
	
	/**
	 * 进行数据写入操作
	 * @param context
	 * @throws IOException
	 */
	public void writeData(MysqlContext context) throws IOException;

}
