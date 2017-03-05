package com.kk.nio.socket.httpserver.fileupload.pervletImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kk.nio.socket.httpserver.fileupload.PervletSocketInf;
import com.kk.nio.socket.httpserver.fileupload.PervletUploadBean;
import com.kk.nio.socket.httpserver.fileupload.comm.flowxml.PervletXml;
import com.kk.nio.socket.httpserver.util.IOUtils;

public class ReadFileProcess implements PervletSocketInf {

	@Override
	public void process(PervletUploadBean bean, String line) throws IOException {

		// 搜索资源路径
		String basePath = PervletXml.class.getClassLoader().getResource(".").getPath();

		File file = new File(basePath + "html/", bean.getReqUrl());

		OutputStream output = bean.getSocket().getOutputStream();

		// 如果文件存在，则进行文件打印响应
		if (file.exists()) {
			InputStream input = null;

			try {
				input = new FileInputStream(file);
				byte[] buffer = new byte[input.available()];

				input.read(buffer);

				String rsponse = "HTTP/1.1 200 OK\r\n";
				rsponse += "Server: kk server/1.0\r\n";
				rsponse += "Content-Length:" + (buffer.length - 4) + "\r\n";
				rsponse += "\r\n";

				output.write(rsponse.getBytes());

				output.write(buffer);

				output.flush();

				System.out.println("response over");
			} finally {
				IOUtils.colse(input);
			}

		}
		// 如果不存在，则提示错误
		else {
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

}
