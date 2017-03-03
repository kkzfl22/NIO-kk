package com.kk.nio.socket.httpserver.pervlet.seq.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 编译的hander处理类
 * 
 * @author kk
 * @time 2017年3月3日
 * @version 0.0.1
 */
public class CompilerHander implements InvocationHandler {

	private Object targerObj;

	public CompilerHander(Object targerObj) {
		this.targerObj = targerObj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(targerObj, args);
	}

}
