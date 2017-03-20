package com.kk.nio.socket.four;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 进行treeset的使用
 * @since 2017年3月20日 上午11:14:46
 * @version 0.0.1
 * @author liujun
 */
public class TreeSetUse<T> {

	private TreeSet<T> treeset = new TreeSet<>();

	public void add(T item) {
		treeset.add(item);
	}
	
	public void remove(T item)
	{
		treeset.remove(item);
	}
	
	public T pollFirst() 
	{
		return treeset.pollFirst();
	}

	/**
	 * 返回此 set 中大于等于给定元素的最小元素；如果不存在这样的元素，则返回 null。
	 * 
	 * @param get
	 * @return
	 */
	public T ceiling(T get) {
		return treeset.ceiling(get);
	}

	/**
	 * higher：返回此 set 中严格大于给定元素的最小元素；如果不存在这样的元素，则返回 null。
	 * 
	 * @param item
	 * @return
	 */
	public T higher(T item) {
		return treeset.higher(item);
	}

	/**
	 * lower：返回此 set 中严格小于给定元素的最大元素；如果不存在这样的元素，则返回 null。
	 * 
	 * @param item
	 * @return
	 */
	public T lower(T item) {
		return treeset.lower(item);
	}

	/**
	 * 返回此 set 的部分视图，其元素从 fromElement（包括）到 toElement（不包括）。
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public SortedSet<T> subset(T from, T to) {
		
		//from true，视图包括开始匹配数据
		//to false 视图不包括匹配的结束数据
		return new TreeSet<>(treeset.subSet(from,true, to,false));
	}
	
	/**
	 * 返回此 set 的部分视图，其元素大于（或等于，如果 inclusive 为 true）fromElement。
	 * @param from
	 * @return
	 */
	public SortedSet<T> tailSet(T from)
	{
		return new TreeSet<>(treeset.tailSet(from,false));
	}

	public static void main(String[] args) {
		TreeSetUse<Integer> treeIter = new TreeSetUse<Integer>();

		for (int i = 0; i < 30; i++) {
			treeIter.add(i);
		}
		// 使用大于等于进行匹配
		System.out.println(treeIter.ceiling(10));
		// 使用大于进行匹配
		System.out.println(treeIter.higher(10));
		// 使用小于进行匹配
		System.out.println(treeIter.lower(10));
		
		treeIter.remove(15);
		
		//进行范围截取，指定开始与结束
		System.out.println(treeIter.subset(0, 20));
		
		treeIter.add(15);
		
		//指定开始进行获取
		System.out.println(treeIter.tailSet(10));
		System.out.println(treeIter.pollFirst());
		System.out.println(treeIter.pollFirst());
		System.out.println(treeIter.pollFirst());

	}

}
