package com.kk.demo;

public class BitDemo {
	
	public static void main(String[] args) {
		int value = 0x000a;
		System.out.println(Integer.toBinaryString(0xf));
		System.out.println(Integer.toBinaryString(value));
		System.out.println(Integer.toBinaryString(1<<1));
		int value2  = value & (1<<3) ;
		int value3  = value & (1<<1) ;
		
		System.out.println(Integer.toBinaryString(value2));
		System.out.println(value2 == 1<<3);
		System.out.println(value3 == 1<<1);
	}

}
