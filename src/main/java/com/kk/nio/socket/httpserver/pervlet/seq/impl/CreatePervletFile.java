package com.kk.nio.socket.httpserver.pervlet.seq.impl;

import com.kk.nio.socket.httpserver.pervlet.PvRequest;
import com.kk.nio.socket.httpserver.pervlet.PvResponse;

/**
 * 用来进行生成相关的pervlet文件信息
 * 
 * @author liujun
 *
 */
public class CreatePervletFile {

	private static final String LINE = "\r\n";

	/**
	 * 文件名称
	 */
	public static final String FileName = "CorePervletProcess";

	/**
	 * 文件内容信息
	 */
	private StringBuilder fileMsg = new StringBuilder();

	/**
	 * 写入文件头
	 */
	public void writeHead() {
		fileMsg.append("package com.kk.nio.socket.httpserver.pervlet;").append(LINE);
		fileMsg.append(LINE);
		fileMsg.append("import java.io.InputStream;").append(LINE);
		fileMsg.append("import java.io.OutputStream;").append(LINE);
		fileMsg.append("import java.io.IOException;").append(LINE);
		fileMsg.append(LINE);

		fileMsg.append("public class " + FileName + " implements Pervlet { ").append(LINE);
		fileMsg.append(outTab(1)).append("public void process(PvRequest req,PvResponse rsp) throws IOException{")
				.append(LINE);
		fileMsg.append(outTab(2)).append("OutputStream output = rsp.getOutput();").append(LINE);
	}

	/**
	 * 行中的数据信息
	 * 
	 * @param linemsg
	 *            每行数据
	 */
	public void writeLine(String linemsg) {
		// 对数据中的引号做处理
		linemsg = linemsg.replace("\"", "\\\"");

		fileMsg.append(outTab(2)).append("output.write(\"").append(linemsg).append("\".getBytes());").append(LINE);
	}

	/**
	 * 写入文件尾
	 */
	public void writeEnd() {
		fileMsg.append(outTab(1)).append("}").append(LINE);
		fileMsg.append("}").append(LINE);
	}

	public String getMsg() {
		return fileMsg.toString();
	}

	private String outTab(int num) {
		String msg = "";
		for (int i = 0; i < num; i++) {
			msg += "\t";
		}
		return msg;
	}

	public static void main(String[] args) {
		String msg = "\"";

	}

}
