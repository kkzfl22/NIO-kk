package com.kk.nio.socket.httpserver.fileupload.comm.flowxml;

import java.io.File;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 配制文件转换
 * @author kk
 * @time 2017年3月4日
 * @version 0.0.1
 */
public class PervletXml {
	
	
	/**
	 * 配制文件路径
	 */
	private static final String PERVLET_PATH = "pervlet/pervlet.xml";
	
	private static final Map<String, PervletBean> PERVLET_MAP;
	
	
	static
	{
		//加载map信息
		PERVLET_MAP = XmlToPervletMap();
	}

	/**
	 * 将xml文档转换为pojo
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, PervletBean> XmlToPervletMap() {
		
		XStream xstream = new XStream(new DomDriver("utf-8"));
		
		//搜索资源路径
		String basePath = PervletXml.class.getClassLoader().getResource(".").getPath();
		
		xstream.alias("pervlets", Map.class);
		xstream.alias("pervlet", Map.Entry.class);
		xstream.alias("key", String.class);

		xstream.alias("value", PervletBean.class);
		//忽略未知的标签
		xstream.ignoreUnknownElements();
		
		
		Map<String, PervletBean> pervletMap = (Map<String, PervletBean>) xstream.fromXML(new File(basePath,PERVLET_PATH));

		return pervletMap;
	}
	
	/**
	 * 获取对应的配制信息
	 * @param key
	 * @return
	 */
	public static PervletBean GetPervlet(String key)
	{
		return PERVLET_MAP.get(key);
	}

}
