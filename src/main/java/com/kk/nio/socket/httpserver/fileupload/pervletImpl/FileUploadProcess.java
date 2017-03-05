package com.kk.nio.socket.httpserver.fileupload.pervletImpl;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.kk.nio.socket.httpserver.fileupload.PervletSocketInf;
import com.kk.nio.socket.httpserver.fileupload.PervletUploadBean;
import com.kk.nio.socket.httpserver.util.IOUtils;

/**
 * 进行文件的上传操作
 * 
 * @author kk
 * @time 2017年3月4日
 * @version 0.0.1
 */
public class FileUploadProcess implements PervletSocketInf {

	private FileOutputStream output = null;
	private DataOutputStream daoutput = null;

	private boolean isOpen = false;

	@Override
	public void process(PervletUploadBean bean, String line) throws IOException {

		if (bean.getFileName() != null) {
			if (bean.getSpitFlag().equals(line) || ("--" + bean.getSpitFlag() + "--").equals(line)) {
				IOUtils.colse(daoutput);
				IOUtils.colse(output);
			} else {
				opeFile(line + "\r\n");
			}
		}

	}

	private void opeFile(String line) throws IOException {
		if (!isOpen) {
			String path = FileUploadProcess.class.getClassLoader().getResource("msp/").getPath();
			output = new FileOutputStream(path + "111.txt");
			daoutput = new DataOutputStream(output);

			isOpen = true;
		} else {
			daoutput.writeBytes(line);
		}
	}

}
