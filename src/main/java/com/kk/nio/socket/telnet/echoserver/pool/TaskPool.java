package com.kk.nio.socket.telnet.echoserver.pool;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务执行队列
 * @author kk
 * @time 2017年3月11日
 * @version 0.0.1
 */
public class TaskPool {
	
	/**
	 * 任务执行
	 */
	private static ExecutorService execute = Executors.newFixedThreadPool(3);
	
	/**
	 * 提交任务
	 * @param run
	 */
	public static void submit(Runnable run)
	{
		execute.submit(run);
	}
	
	
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		
		buffer.put((byte)1);
		buffer.put((byte)2);
		buffer.put((byte)3);
		buffer.put((byte)4);
		buffer.put((byte)5);
		buffer.put((byte)6);
		buffer.put((byte)7);
		buffer.put((byte)8);
		buffer.put((byte)9);
		
		buffer.flip();
		
		buffer.position(4);
		
		byte[] arrays = buffer.array();
		
		System.out.println("compact before:"+Arrays.toString(arrays)+",info:"+buffer);
		
		buffer.compact();
		
		System.out.println("compact    end:"+Arrays.toString(arrays)+",info:"+buffer);
	}

}
