package com.kk.nio.mysqlproxy.proc.frontendmid.state.cmd.servicestate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kk.nio.mysqlproxy.proc.frontendmid.FrontendMidConnnectHandler;

/**
 * 进行mysql业务流程的上下文定义
 * 
 * @since 2017年7月2日 上午10:51:10
 * @version 0.0.1
 * @author liujun
 */
public class MysqlServiceContext {

	/**
	 * 以前后端绑定连接对象作为参数
	 */
	private FrontendMidConnnectHandler frontedConn;

	/**
	 * 当前的状态信息
	 */
	private MysqlServiceStateInf currState;

	/**
	 * 读取到的位置信息
	 */
	private int readPosition;

	/**
	 * 写入的数据位置信息
	 */
	private int writePosition;

	/**
	 * 透传所引用的对象
	 */
	private Map<String, Object> transBeanMap = new HashMap<>();

	public FrontendMidConnnectHandler getFrontedConn() {
		return frontedConn;
	}

	public void setFrontedConn(FrontendMidConnnectHandler frontedConn) {
		this.frontedConn = frontedConn;
	}

	public void serviceDoInvoke() throws IOException {
		// 如果当前设置为状态，则进行状态的流程执行
		if (currState != null) {
			this.getCurrState().serviceDoInvoke(this);
		}

	}

	public MysqlServiceStateInf getCurrState() {
		return currState;
	}

	public void setCurrState(MysqlServiceStateInf currState) {
		this.currState = currState;
	}

	public int getReadPosition() {
		return readPosition;
	}

	public void setReadPosition(int readPosition) {
		this.readPosition = readPosition;
	}

	public Object getTransBean(String key) {
		return transBeanMap.get(key);
	}

	public void putTransBean(String key, Object value) {
		this.transBeanMap.put(key, value);
	}

	public int getWritePosition() {
		return writePosition;
	}

	public void setWritePosition(int writePosition) {
		this.writePosition = writePosition;
	}

}
