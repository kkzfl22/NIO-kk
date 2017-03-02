package com.kk.nio.socket.httpserver.pervlet.impl;

import com.kk.nio.socket.httpserver.pervlet.Pervlet;
import com.kk.nio.socket.httpserver.pervlet.PvRequest;
import com.kk.nio.socket.httpserver.pervlet.PvResponse;

/**
 * 进行用户处理的servlet
 * @author Think
 *
 */
public class UserProcessPervlet implements Pervlet {

	@Override
	public PvResponse process(PvRequest req) {
		String userName = req.getParam().get("username");
		
		String passwd = req.getParam().get("passwd");
		
		if("KK".equals(userName) && "123456".equals(passwd))
		{
			
		}
		
		return null;
	}

}
