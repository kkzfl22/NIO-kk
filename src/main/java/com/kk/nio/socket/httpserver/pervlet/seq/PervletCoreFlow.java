package com.kk.nio.socket.httpserver.pervlet.seq;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import com.kk.nio.socket.httpserver.pervlet.seq.impl.FlowCompilerPervlet;
import com.kk.nio.socket.httpserver.pervlet.seq.impl.FlowCreatePervletjava;

/**
 * 用于进行核心的pervlet的流程
 * 
 * @author liujun
 *
 */
public class PervletCoreFlow {

	private static ServiceExecInf[] EXEC = new ServiceExecInf[2];

	static {
		// 创建pervlet的java文件
		EXEC[0] = new FlowCreatePervletjava();
		// 进行动态编译运行
		EXEC[1] = new FlowCompilerPervlet();
	}

	public static void runFlow(File file, String filePath, Map<String, String> map, Socket socket) throws IOException {
		SeqLinkedList list = new SeqLinkedList();

		list.addExec(EXEC);

		list.putParam(ParamConfig.CONFIG_STR.PERVLET_CORE_FILE.getKey(), file);
		list.putParam(ParamConfig.CONFIG_STR.PERVLET_CREATE_FILEPATH.getKey(), filePath);
		list.putParam(ParamConfig.CONFIG_STR.PERVLET_SOCKET_INPUT.getKey(), socket.getInputStream());
		list.putParam(ParamConfig.CONFIG_STR.PERVLET_SOCKET_OUTPUT.getKey(), socket.getOutputStream());
		list.putParam(ParamConfig.CONFIG_STR.PERVLET_PARAM_MAP.getKey(), map);
		
		boolean exec = false;

		try {
			exec = list.nextExec();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("当前的结果:" + exec);
	}

}
