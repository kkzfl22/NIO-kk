package com.kk.nio.socket.httpserver.pervlet.seq.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.kk.nio.socket.httpserver.pervlet.PvRequest;
import com.kk.nio.socket.httpserver.pervlet.PvResponse;
import com.kk.nio.socket.httpserver.pervlet.seq.ParamConfig;
import com.kk.nio.socket.httpserver.pervlet.seq.SeqLinkedList;
import com.kk.nio.socket.httpserver.pervlet.seq.ServiceExecInf;

/**
 * 进行文件的编译操作
 * 
 * @author liujun
 *
 */
public class FlowCompilerPervlet implements ServiceExecInf {

	@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
	@Override
	public boolean invoke(SeqLinkedList seqList) throws Exception {

		String fileComp = (String) seqList.getValue(ParamConfig.CONFIG_STR.PERVLET_COMPILER_FILE.getKey());

		InputStream input = (InputStream) seqList.getValue(ParamConfig.CONFIG_STR.PERVLET_SOCKET_INPUT.getKey());
		OutputStream output = (OutputStream) seqList.getValue(ParamConfig.CONFIG_STR.PERVLET_SOCKET_OUTPUT.getKey());

		Map<String, String> map = (Map<String, String>) seqList
				.getValue(ParamConfig.CONFIG_STR.PERVLET_PARAM_MAP.getKey());

		File comFile = new File(fileComp);

		String classpathPath = FlowCompilerPervlet.class.getClassLoader().getResource(".").getPath();

		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		// JavaCompiler最核心的方法是run, 通过这个方法编译java源文件, 前3个参数传null时,
		// 分别使用标准输入/输出/错误流来 处理输入和编译输出. 使用编译参数-d指定字节码输出目录.
		int compileResult = javac.run(null, null, null, "-d", classpathPath, comFile.getAbsolutePath());
		// run方法的返回值: 0-表示编译成功, 否则表示编译失败
		if (compileResult != 0) {
			System.err.println("编译失败!!");
		}

		PvRequest req = new PvRequest();
		req.setInput(input);
		req.setParam(map);

		PvResponse rsp = new PvResponse();
		rsp.setOutput(output);

		Class corePerClass = Class.forName("com.kk.nio.socket.httpserver.pervlet." + CreatePervletFile.FileName);
		Method evalMethod = corePerClass.getMethod("process", PvRequest.class, PvResponse.class);
		evalMethod.invoke(corePerClass.newInstance(), req, rsp);

		return seqList.nextExec();
	}

	@Override
	public boolean rollBackInvoke(SeqLinkedList seqList) throws Exception {
		return seqList.rollExec();
	}

}
