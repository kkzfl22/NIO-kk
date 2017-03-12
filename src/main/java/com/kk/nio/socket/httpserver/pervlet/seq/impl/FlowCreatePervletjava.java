package com.kk.nio.socket.httpserver.pervlet.seq.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.kk.nio.socket.httpserver.pervlet.seq.ParamConfig;
import com.kk.nio.socket.httpserver.pervlet.seq.SeqLinkedList;
import com.kk.nio.socket.httpserver.pervlet.seq.ServiceExecInf;
import com.kk.nio.socket.util.IOUtils;

/**
 * 创建核心的类文件信息
 * 
 * @author liujun
 *
 */
public class FlowCreatePervletjava implements ServiceExecInf {

	@Override
	public boolean invoke(SeqLinkedList seqList) throws Exception {

		File file = (File) seqList.getValue(ParamConfig.CONFIG_STR.PERVLET_CORE_FILE.getKey());
		String filePath = (String) seqList.getValue(ParamConfig.CONFIG_STR.PERVLET_CREATE_FILEPATH.getKey());

		FileReader read = null;
		BufferedReader bufferRead = null;

		FileOutputStream output = null;
		try {
			read = new FileReader(file);
			bufferRead = new BufferedReader(read);

			String compPervletFile = filePath + "/" + CreatePervletFile.FileName + ".java";

			seqList.putParam(ParamConfig.CONFIG_STR.PERVLET_COMPILER_FILE.getKey(), compPervletFile);

			// 用来进行动态编译的文件的生成
			output = new FileOutputStream(compPervletFile);

			String line = null;

			CreatePervletFile javaFile = new CreatePervletFile();
			javaFile.writeHead();
			while ((line = bufferRead.readLine()) != null) {
				javaFile.writeLine(line);
			}
			javaFile.writeEnd();

			output.write(javaFile.getMsg().getBytes());

		} finally {
			IOUtils.colse(output);
			IOUtils.colse(bufferRead);
			IOUtils.colse(read);
		}

		return seqList.nextExec();
	}

	@Override
	public boolean rollBackInvoke(SeqLinkedList seqList) throws Exception {
		return seqList.rollExec();
	}

}
