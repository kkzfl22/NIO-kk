package com.kk.nio.socket.httpserver.fileupload.comm.flowxml;

import java.lang.reflect.Method;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 相关的bean的信息
 * 
 * @author kk
 * @time 2017年3月4日
 * @version 0.0.1
 */
@XStreamAlias("pervlet")
public class PervletBean {

	/**
	 * 文件路径信息
	 */
	private String name;

	/**
	 * url信息
	 */
	private String url;

	/**
	 * 对应的java路径
	 */
	private String javaClass;
	
	
	/**
	 * 初始化的对象
	 */
	private Object ObjInit;
	
	
	/**
	 * 处理方法
	 */
	private Method perMethod;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}
	

	public Object getObjInit() {
		return ObjInit;
	}

	public void setObjInit(Object objInit) {
		ObjInit = objInit;
	}

	public Method getPerMethod() {
		return perMethod;
	}

	public void setPerMethod(Method perMethod) {
		this.perMethod = perMethod;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PervletBean [name=");
		builder.append(name);
		builder.append(", url=");
		builder.append(url);
		builder.append(", javaClass=");
		builder.append(javaClass);
		builder.append("]");
		return builder.toString();
	}

	
}
