package com.kk.nio.socket.multreactor.procchain;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TimeColltion {

	private static final Map<String, Long> TIMELIST = new TreeMap<>();

	public static void addTime(String key, long time) {
		TIMELIST.put(key, time);
	}

	public static Map<String, Long> getTime() {
		return TIMELIST;
	}

	public static void print() {
		Iterator<Entry<String, Long>> iterEntry = TIMELIST.entrySet().iterator();
		Entry<String, Long> item = null;
		while(iterEntry.hasNext())
		{
			item = iterEntry.next();
			System.out.print("key :" + item.getKey());
			for (int i = 0; i < 30-item.getKey().length(); i++) {
				System.out.print(" ");
			}
			System.out.print(",value:"+item.getValue());
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(SelectionKey.OP_CONNECT));
		System.out.println(Integer.toBinaryString(SelectionKey.OP_ACCEPT));
		System.out.println(Integer.toBinaryString(SelectionKey.OP_READ));
		System.out.println(Integer.toBinaryString(SelectionKey.OP_WRITE));
		
	}

}
