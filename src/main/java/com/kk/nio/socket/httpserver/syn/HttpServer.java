package com.kk.nio.socket.httpserver.syn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * http��server��Ϣ
 * 
 * @author Think
 *
 */
public class HttpServer {

	/**
	 * Ĭ�ϵĶ˿���Ϣ
	 */
	private static final int PORT = 91;

	public static void main(String[] args) {
		try {
			ServerSocket socket = new ServerSocket(PORT);
			System.out.println("�����ɹ����˿�:" + PORT);

			// ������Ϣ
			while (true) {
				// ���ͬ����socket��socket��Ϣ
				Socket synsokcet = socket.accept();
				System.out.println("������Ϣ:" + synsokcet.toString());

				LineNumberReader read = new LineNumberReader(new InputStreamReader(synsokcet.getInputStream()));

				String readLine = null;
				String reqPage = null;

				String cookieInfo = null;

				while ((readLine = read.readLine()) != null) {
					System.out.println("line msg :" + readLine);

					if (read.getLineNumber() == 1) {
						reqPage = readLine.substring(readLine.indexOf('/') + 1, readLine.lastIndexOf(' '));
						System.out.println("page info :" + reqPage);
					} else {
						// ����ҵ�cookie
						if (readLine.startsWith("Cookie:")) {
							cookieInfo = readLine;
							System.out.println("cookie msg:" + cookieInfo);
						} else if (readLine.isEmpty()) {
							// readFile(reqPage,synsokcet);
							outCookie(cookieInfo, reqPage, synsokcet);
						}
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readFile(String reqFile, Socket sock) throws IOException {
		File file = new File("D:/java/test/", reqFile);

		OutputStream output = sock.getOutputStream();

		if (file.exists()) {
			InputStream input = new FileInputStream(file);

			byte[] buffer = new byte[input.available()];

			input.read(buffer);

			input.close();
			String rsponse = "HTTP/1.1 200 OK\r\n";
			rsponse += "Server: kk server/1.0\r\n";
			rsponse += "Content-Length:" + (buffer.length - 4) + "\r\n";
			rsponse += "\r\n";

			output.write(rsponse.getBytes());

			output.write(buffer);

			output.flush();

			sock.close();
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
	 * ���cookie��Ϣ
	 * 
	 * @param userInfo
	 * @param reqFile
	 * @param sock
	 * @throws IOException
	 */
	public static void outCookie(String userInfo, String reqFile, Socket sock) throws IOException {

		OutputStream output = sock.getOutputStream();

		String msg = "I can't find file ....cry \r\n";

		String rsponse = "HTTP/1.1 200 OK\r\n";
		rsponse += "Server: kk server/1.0\r\n";

		// ����û���session��Ϣ�����ڣ��򴴽�
		if (null == userInfo) {
			rsponse += getCookieMsg();
		}

		rsponse += "Content-Length:" + (msg.length() - 4) + "\r\n";
		rsponse += "\r\n";
		rsponse += msg;

		output.write(rsponse.getBytes());
		output.flush();

	}

	public static String getCookieMsg() {
		StringBuilder msg = new StringBuilder();
		msg.append("Set-Cookie: ").append("jsessionid=").append(System.nanoTime()).append(".kk;");
		msg.append(" domain:localhost").append("\r\n");
		msg.append("Set-Cookie: autologin=true; domain=localhost\r\n");

		return msg.toString();
	}

}
