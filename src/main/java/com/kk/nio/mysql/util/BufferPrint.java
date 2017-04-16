package com.kk.nio.mysql.util;

import java.nio.ByteBuffer;

public class BufferPrint {
	
	public static void print(ByteBuffer buffer)
	{
		int size = buffer.position();
		
		for (int i = 0; i < size; i++) {
			System.out.println(i+":"+buffer.get(i)+"\t");
		}
	}

}
