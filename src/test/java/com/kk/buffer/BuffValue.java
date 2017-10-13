package com.kk.buffer;

import java.nio.ByteBuffer;

public class BuffValue {
	
	public static void main(String[] args) {
		byte value=(byte)0xfb;
		System.out.println(value);
		
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(20);
		
		buffer.put((byte)1);
		buffer.put((byte)2);
		buffer.put((byte)3);
		buffer.put((byte)4);
		buffer.put((byte)5);
		buffer.put((byte)6);
		
		//buffer.position(4);
		
		byte[] buf = new byte[2];
		
		buffer.get(buf, 4, buf.length);
		
		System.out.println(buf);
		
	}

}
