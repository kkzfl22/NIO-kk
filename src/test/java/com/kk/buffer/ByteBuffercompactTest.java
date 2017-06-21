package com.kk.buffer;

import java.nio.ByteBuffer;

public class ByteBuffercompactTest {
	
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		for (int i = 0; i < 10; i++) {
			buffer.put((byte)i);
		}
		
		System.out.println("压缩前:"+buffer);
		buffer.compact();
		System.out.println("压缩后:"+buffer);
		
		buffer.position(0);
		for (int i = 0; i < 10; i++) {
			buffer.put((byte)i);
		}
		
		System.out.println("压缩前:"+buffer);
		buffer.position(5);
		System.out.println(buffer.get());
		System.out.println(buffer.get());
		System.out.println(buffer.get());
		System.out.println("压缩后:"+buffer);
		
	}

}
