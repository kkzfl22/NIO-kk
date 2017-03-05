package com.kk.nio.socket.httpserver.pervlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kk.nio.socket.httpserver.pervlet.seq.PervletCoreFlow;

/**
 * 核心的servlet的处理
 * 
 * @author kk
 * @time 2017年3月2日
 * @version 0.0.1
 */
public class CorePervlet {

	/**
	 * 默认的端口信息
	 */
	private static final int PORT = 91;

	/**
	 * 固定的线程池的大小
	 */
	private static final Executor EXECUTEPOOL = Executors.newFixedThreadPool(10);

	public static void main(String[] args) {
		try {
			ServerSocket socket = new ServerSocket(PORT);
			System.out.println("启动成功，端口:" + PORT);

			CorePervlet pervlet = new CorePervlet();

			// 接收信息
			while (true) {
				// 获得同步的socket的socket信息
				Socket synsokcet = socket.accept();

				// 使用线程池来处理任务，
				EXECUTEPOOL.execute(() -> {
					try {
						pervlet.threadProcess(synsokcet);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void threadProcess(Socket synsokcet) throws IOException {

		System.out.println("请求信息:" + synsokcet.toString());

		LineNumberReader read = new LineNumberReader(new InputStreamReader(synsokcet.getInputStream()));

		String readLine = null;
		String reqPage = null;

		String cookieInfo = null;

		Map<String, String> map = new HashMap<>();

		while ((readLine = read.readLine()) != null) {
			System.out.println("line msg :" + readLine);

			if (read.getLineNumber() == 1) {
				// 检查是否存在?号
				if (readLine.indexOf("?") != -1) {
					reqPage = readLine.substring(readLine.indexOf('/') + 1, readLine.indexOf("?"));
					String lineValue = readLine.substring(readLine.indexOf("?") + 1, readLine.lastIndexOf(" "));
					getParam(lineValue, map);

				} else {
					reqPage = readLine.substring(readLine.indexOf('/') + 1, readLine.lastIndexOf(' '));
				}
				System.out.println("page info :" + reqPage);
			} else {
				// 如果找到cookie
				if (readLine.startsWith("Cookie:")) {
					cookieInfo = readLine;
					System.out.println("cookie msg:" + cookieInfo);
				} else if (readLine.isEmpty()) {
					// outCookie(cookieInfo, reqPage, synsokcet);
					readFile(reqPage, map, synsokcet);

				}
			}
		}
	}

	private void getParam(String lineValue, Map<String, String> map) {
		String[] arrays = lineValue.split("&");

		for (int i = 0; i < arrays.length; i++) {
			String[] items = arrays[i].split("=");

			map.put(items[0], items[1]);
		}

	}

	public void readFile(String reqFile, Map<String, String> param, Socket sock) throws IOException {

		String path = CorePervlet.class.getClassLoader().getResource("com/kk/nio/socket/httpserver/pervlet").getPath();

		String basepath = CorePervlet.class.getClassLoader().getResource(".").getPath();

		File file = new File(basepath + "msp/", reqFile);

		OutputStream output = sock.getOutputStream();

		if (file.exists()) {

			PervletCoreFlow.runFlow(file, path, param, sock);

			System.out.println("response over");
		} else {
			String msg = "I can't find file ....cry \r\n";

			String rsponse = "HTTP/1.1 200 OK\r\n";
			rsponse += "Server: kk server/1.0\r\n";
			rsponse += "Content-Length:" + (msg.length() - 4) + "\r\n";
			rsponse += "\r\n";
			rsponse += msg;

			output.write(rsponse.getBytes());
			output.flush();

		}

	}

	/**
	 * 输出cookie信息
	 * 
	 * @param userInfo
	 * @param reqFile
	 * @param sock
	 * @throws IOException
	 */
	public void outCookie(String userInfo, String reqFile, Socket sock) throws IOException {

		OutputStream output = sock.getOutputStream();

		// String msg = "I can't find file ....cry \r\n";
		//
		// String rsponse = "HTTP/1.1 200 OK\r\n";
		// rsponse += "Server: kk server/1.0\r\n";
		//
		// // 如果用户的session信息不存在，则创建
		// if (null == userInfo) {
		// rsponse += getCookieMsg();
		// }
		//
		// rsponse += "Content-Length:" + (msg.length() - 4) + "\r\n";
		// rsponse += "\r\n";
		// rsponse += msg;
		//
		// output.write(rsponse.getBytes());
		// output.flush();

	}

	public String getCookieMsg() {
		StringBuilder msg = new StringBuilder();
		msg.append("Set-Cookie: ").append("jsessionid=").append(System.nanoTime()).append(".kk;");
		msg.append(" domain:localhost").append("\r\n");
		msg.append("Set-Cookie: autologin=true; domain=localhost\r\n");

		return msg.toString();
	}

}
