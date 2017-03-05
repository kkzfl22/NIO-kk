package com.kk.nio.socket.httpserver.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kk.nio.socket.httpserver.fileupload.comm.flowxml.PervletBean;
import com.kk.nio.socket.httpserver.fileupload.comm.flowxml.PervletXml;

public class PervletUploadProcess {

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

			PervletUploadProcess core = new PervletUploadProcess();

			// 接收信息
			while (true) {
				// 获得同步的socket的socket信息
				Socket synsokcet = socket.accept();

				// 使用线程池来处理任务，
				EXECUTEPOOL.execute(() -> {
					try {
						core.ThreadProcess(synsokcet);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ThreadProcess(Socket synsokcet) throws IOException {
		
		InputStream input = synsokcet.getInputStream();
		
		
		InputStreamReader readReader = new InputStreamReader(input);

		LineNumberReader read = new LineNumberReader(readReader);

		String readLine = null;
		String reqURLPage = null;
		String spitFlag = null;

		PervletUploadBean paramBean = new PervletUploadBean();

		paramBean.setSocket(synsokcet);
		

		while ((readLine = read.readLine()) != null) {
			System.out.println("line msg :" + readLine);

			if (read.getLineNumber() == 1) {
				reqURLPage = readLine.substring(readLine.indexOf('/') + 1, readLine.lastIndexOf(' '));
				paramBean.setReqUrl(reqURLPage);
			} else {
				// 查找分隔符
				if (readLine.indexOf("boundary") != -1) {
					spitFlag = readLine.substring(readLine.indexOf("boundary") + 9);
					paramBean.setSpitFlag(spitFlag);
				}
				// 读取名称
				else if (readLine.indexOf("filename") != -1) {
					paramBean.setFileName(
							readLine.substring(readLine.indexOf("filename") + 10, readLine.lastIndexOf("\"")));
				}
				// 读取类型
				else if (readLine.indexOf("Content-Type") != -1 && paramBean.getFileName() != null) {
					paramBean.setType(readLine.substring(readLine.indexOf(":") + 2));
				}
				// 首次
				else if (readLine.isEmpty()) {
					// 当头部信息加载完毕后，则进行实体信息的处理
					this.bodyProcess(paramBean, readLine);
				}
				// 其他信息都已经加载到，则继续进行循环
				else if (paramBean.getType() != null) {
					this.bodyProcess(paramBean, readLine);
				}
			}
		}
	}

	/**
	 * 进行实体类的处理操作
	 * 
	 * @param reqUrl
	 * @param sockClient
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void bodyProcess(PervletUploadBean paramBean, String line) {
		PervletBean bean = PervletXml.GetPervlet(paramBean.getReqUrl());

		if (null != bean) {
			// 加载对应的实现类信息
			try {

				if (null == bean.getObjInit()) {
					Class processClass = Class.forName(bean.getJavaClass());
					bean.setPerMethod(processClass.getMethod("process", PervletUploadBean.class, String.class));
					bean.setObjInit(processClass.newInstance());
				}

				// 进行处理
				bean.getPerMethod().invoke(bean.getObjInit(), paramBean, line);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static String getCookieMsg() {
		StringBuilder msg = new StringBuilder();
		msg.append("Set-Cookie: ").append("jsessionid=").append(System.nanoTime()).append(".kk;");
		msg.append(" domain:localhost").append("\r\n");
		msg.append("Set-Cookie: autologin=true; domain=localhost\r\n");

		return msg.toString();
	}

}
