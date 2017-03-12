package com.kk.nio.socket.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 使用java执行cmd的工具类
 * 
 * @since 2017年3月12日 下午7:26:41
 * @version 0.0.1
 * @author kk
 */
public class CmdUtils {

	/**
	 * 运行windows平台下的cmd命令
	 * 
	 * @param cmd
	 *            命令信息
	 * @return 结果信息
	 */
	public static String runCmd(String cmd) {

		StringBuilder cmdRsp = new StringBuilder();

		InputStream input = null;
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			input = process.getInputStream();
			inputReader = new InputStreamReader(input, "GBK");
			bufferReader = new BufferedReader(inputReader);

			String line = null;

			while ((line = bufferReader.readLine()) != null) {
				cmdRsp.append(line + "\r\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.colse(bufferReader);
			IOUtils.colse(inputReader);
			IOUtils.colse(input);
		}

		return cmdRsp.toString();
	}

	public static void main(String[] args) {
		System.out.println(CmdUtils.runCmd("ping localhost"));
	}

}
