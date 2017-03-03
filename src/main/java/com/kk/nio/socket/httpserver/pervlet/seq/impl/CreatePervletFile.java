package com.kk.nio.socket.httpserver.pervlet.seq.impl;

/**
 * 用来进行生成相关的pervlet文件信息
 * 
 * @author liujun
 *
 */
public class CreatePervletFile {

	private static final String LINE = "\r\n";
	
	/**
	 * 内换行
	 */
	private static final String INNER_LINE = "\\r\\n";

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
		fileMsg.append("import java.util.Map;").append(LINE);
		fileMsg.append("import java.util.Iterator;").append(LINE);
		fileMsg.append("import java.util.Map;").append(LINE);
		fileMsg.append("import java.util.Map.Entry;").append(LINE);
		fileMsg.append(LINE);

		fileMsg.append("public class " + FileName + " implements Pervlet { ").append(LINE);
		fileMsg.append(outTab(1)).append("public void process(PvRequest req,PvResponse rsp) throws IOException{")
				.append(LINE);
		fileMsg.append(outTab(2)).append("StringBuilder result = new StringBuilder();").append(LINE);
		fileMsg.append(outTab(2)).append("result.append(\"HTTP/1.1 200 OK").append(INNER_LINE).append("\");").append(LINE);
		fileMsg.append(outTab(2)).append("result.append(\"Server: kk server/1.0").append(INNER_LINE).append("\");").append(LINE);
		fileMsg.append(outTab(2)).append("StringBuilder content = new StringBuilder();").append(LINE);
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
		fileMsg.append(outTab(2)).append("content.append(\"").append(linemsg).append(INNER_LINE).append("\");").append(LINE);
	}

	/**
	 * 写入文件尾
	 */
	public void writeEnd() {
		fileMsg.append(outTab(2)).append("String contentMsg = content.toString();").append(LINE);
		fileMsg.append(outTab(2)).append(" Map<String, String>  paramMap = req.getParam();").append(LINE);
		fileMsg.append(outTab(2)).append("Iterator<Entry<String, String>> itervals = paramMap.entrySet().iterator();").append(LINE);
		fileMsg.append(outTab(2)).append("while(itervals.hasNext())").append(LINE);
		fileMsg.append(outTab(2)).append("{").append(LINE);
		fileMsg.append(outTab(3)).append("Entry<String, String> itemValeu = itervals.next();").append(LINE);
		fileMsg.append(outTab(3)).append("contentMsg = contentMsg.replaceAll(\"\\\\$\\\\{\"+itemValeu.getKey()+\"\\\\}\", itemValeu.getValue());").append(LINE);
		fileMsg.append(outTab(2)).append("}").append(LINE);
		
		
		fileMsg.append(outTab(2)).append("result.append(\"Content-Length:").append("\"+(contentMsg.getBytes().length - 4)+\"").append(INNER_LINE).append("\");").append(LINE);
		fileMsg.append(outTab(2)).append("result.append(\"").append(INNER_LINE).append("\");").append(LINE);
		fileMsg.append(outTab(2)).append("result.append(contentMsg);").append(LINE);
		fileMsg.append(outTab(2)).append(LINE);
		fileMsg.append(outTab(2)).append("rsp.getOutput().write(result.toString().getBytes());").append(LINE);
		fileMsg.append(outTab(2)).append("rsp.getOutput().flush();").append(LINE);
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
