package com.kk.nio.socket.httpserver.pervlet.seq;

import java.io.File;

import com.kk.nio.socket.httpserver.pervlet.seq.impl.FlowCreatePervletjava;

/**
 * 用于进行核心的pervlet的流程
 * 
 * @author liujun
 *
 */
public class PervletCoreFlow {

	private static ServiceExecInf[] EXEC = new ServiceExecInf[1];

	static {
		// 创建pervlet的java文件
		EXEC[0] = new FlowCreatePervletjava();
	}

	public static void runFlow(File file,String filePath) {
		SeqLinkedList list = new SeqLinkedList();

		list.addExec(EXEC);

		list.putParam(ParamConfig.CONFIG_STR.PERVLET_CORE_FILE.getKey(), file);
		list.putParam(ParamConfig.CONFIG_STR.PERVLET_CREATE_FILEPATH.getKey(), filePath);

		boolean exec = false;

		try {
			exec = list.nextExec();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("当前的结果:" + exec);
	}

}
