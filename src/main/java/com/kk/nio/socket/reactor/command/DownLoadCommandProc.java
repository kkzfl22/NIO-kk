package com.kk.nio.socket.reactor.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.kk.nio.socket.reactor.IOHandler;
import com.kk.nio.socket.util.IOUtils;

/**
 * 进行download的流程的处理
 * 
 * @since 2017年3月20日 下午2:55:45
 * @version 0.0.1
 * @author liujun
 */
public class DownLoadCommandProc implements CommandInf {

	/**
	 * 运行时获取的基础路径
	 */
	private String basePath;

	/**
	 * 用于命令的解析
	 */
	private static final String COMMAND_DOWNLOAD = "download";

	/**
	 * 上次写入的标记
	 */
	private long lastWritePositon = 0;

	/**
	 * 文件大小
	 */
	private long fileSize = 0;

	/**
	 * 读取流
	 */
	private FileInputStream read = null;

	/**
	 * 读取通道信息
	 */
	private FileChannel channel = null;
	
	/**
	 * 索引信息
	 */
	private int index =0;
	
	private long startTime = 0;
	
	private long endTime = 0;

	@Override
	public String commandProc(String command, SelectionKey selectKey) {

		if (command.startsWith(COMMAND_DOWNLOAD)) {
			String commandProc = command.substring(COMMAND_DOWNLOAD.length()).trim();

			if (lastWritePositon == 0) {
				// 加载路径信息
				String basePathFile = this.getbasePath();

				try {
					File fileExists = new File(basePathFile, commandProc);
					read = new FileInputStream(fileExists);
					channel = read.getChannel();
					fileSize = channel.size();

					IOHandler hander = (IOHandler) selectKey.attachment();
					
					hander.setRunCmd(COMMAND_DOWNLOAD);

					startTime = System.currentTimeMillis();
					
					// 进行数据的写入
					this.doWrite(hander.getWriteBuffer(), selectKey);
					

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		return command;
	}

	public String getbasePath() {
		if (null == basePath) {
			String downloadPath = this.getClass().getClassLoader().getResource("download").getPath();
			basePath = downloadPath + "/";

			return basePath;
		} else {
			return basePath;
		}
	}

	/**
	 * 用来生成文件
	 * 
	 * @param name
	 */
	public void writeFile(String name) {
		FileOutputStream file = null;

		// 1m的大小数所集
		byte[] lineBytes = new byte[1024 * 1024];

		try {

			for (int j = 0; j < lineBytes.length; j++) {
				lineBytes[j] = (byte) ('a' + j % 30);
			}

			file = new FileOutputStream(new File(this.getbasePath(), name));

			for (int i = 0; i < 200; i++) {
				file.write(lineBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.colse(file);
		}

	}

	public static void main(String[] args) {
		DownLoadCommandProc proc = new DownLoadCommandProc();
		proc.writeFile("testFile.txt");

	}

	@Override
	public String getCommandStart() {
		return COMMAND_DOWNLOAD;
	}

	@Override
	public void doWrite(ByteBuffer writeBuffer, SelectionKey selectKey) throws IOException {

		try {
			SocketChannel socketChannel = (SocketChannel) selectKey.channel();

			// 如果还没有写入完成，则继续写入
			if (lastWritePositon < fileSize) {
				// 向文件通道中写入
				lastWritePositon += channel.transferTo(lastWritePositon, fileSize, socketChannel);

			}
			
			if(index % 5 == 0)
			{
				endTime = System.currentTimeMillis();
				System.out.println("write position :" + lastWritePositon + " fileSize :" + fileSize);
				System.out.println("用时:"+(endTime-startTime)/1000.0f);
			}

			if (lastWritePositon >= fileSize) {
				// 取消写事件
				selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			} else {
				// 注册写事件，进行数据的写入
				selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_WRITE);
			}
			
			index++;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (lastWritePositon >= fileSize) {
				IOUtils.colse(channel);
				IOUtils.colse(read);
			}
		}

	}

}
