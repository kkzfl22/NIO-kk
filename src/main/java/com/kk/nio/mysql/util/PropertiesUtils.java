package com.kk.nio.mysql.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.kk.nio.mysql.console.PropertiesKeyEnum;

/**
 * 进行资源文件的公共查找
 * 
 * @author liujun
 * 
 * @date 2014年5月1日
 * @vsersion 0.0.1
 */
public class PropertiesUtils {

	private Logger log = Logger.getLogger(PropertiesUtils.class);

	private static final Map<String, String> PROPER_MAP = new HashMap<String, String>();

	private static final PropertiesUtils PROINSTANCE = new PropertiesUtils();

	static {
		// 加载数据资源文件信息
		PROINSTANCE.loadProperties("/mysql/mysqlconn.properties");
	}

	private PropertiesUtils() {

	}

	public static PropertiesUtils getInstance() {

		return PROINSTANCE;
	}

	private void loadProperties(String fileName) {
		// 进行加载数据
		Properties pro = new Properties();

		try {
			pro.load(PropertiesUtils.class.getResourceAsStream("/" + fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("PropertiesUtils load properties not exception:", e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("PropertiesUtils load properties IOException:", e);
		}

		if (null != pro.entrySet()) {
			Iterator<Entry<Object, Object>> entryIter = pro.entrySet().iterator();

			while (entryIter.hasNext()) {
				Entry<Object, Object> entry = entryIter.next();
				PROPER_MAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
			}
		}
	}

	public String getValue(PropertiesKeyEnum key) {
		return PROPER_MAP.get(key.getKey());
	}

}
