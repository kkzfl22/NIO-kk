package com.kk.nio.socket.multreactor.forkjoinchain.chain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import com.kk.nio.socket.util.IOUtils;

/**
 * 进行forkjoin的文件中的单词统计,使用客格进行分隔
 * 
 * @since 2017年4月1日 下午12:40:03
 * @version 0.0.1
 * @author liujun
 */
public class ForkjoinTextCount extends RecursiveTask<Map<String, Integer>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文件运行的控制
	 */
	private static final int PROCESS = 1;

	/**
	 * 文件列目录列表
	 */
	private String[] fileList;

	/**
	 * 开始搜索索引号
	 */
	private int startIndex;

	/**
	 * 结束索引号
	 */
	private int endIndex;

	public ForkjoinTextCount(String[] fileList, int startIndex, int endIndex) {
		this.fileList = fileList;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	protected Map<String, Integer> compute() {

		boolean fileOnlye = (endIndex - startIndex) <= PROCESS;

		Map<String, Integer> result = null;

		// 进行单调的统计操作
		if (fileOnlye) {
			Map<String, Integer> resultleft = fileMargeCount(fileList, startIndex - 1);

			if (startIndex != endIndex) {
				Map<String, Integer> resultright = fileMargeCount(fileList, endIndex - 1);
				result = this.marge(resultleft, resultright);
			} else {
				result = resultleft;
			}

		} else {

			// 进行分隔操作
			int mid = (startIndex + endIndex) / 2;

			ForkjoinTextCount left = new ForkjoinTextCount(fileList, startIndex, mid - 1);
			ForkjoinTextCount right = new ForkjoinTextCount(fileList, mid, endIndex);

			left.fork();
			right.fork();

			Map<String, Integer> resultLeft = left.join();
			Map<String, Integer> resultRight = right.join();

			result = this.marge(resultLeft, resultRight);
		}

		return result;
	}

	/**
	 * 进行map合并操作
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private Map<String, Integer> marge(Map<String, Integer> left, Map<String, Integer> right) {
		Iterator<Entry<String, Integer>> iterLeft = left.entrySet().iterator();

		Entry<String, Integer> next = null;

		Integer rightValue = null;

		while (iterLeft.hasNext()) {
			next = iterLeft.next();

			rightValue = right.get(next.getKey());

			if (null != rightValue) {
				rightValue = rightValue + next.getValue();
				left.put(next.getKey(), rightValue);

				right.remove(next.getKey());
			}
		}

		Iterator<Entry<String, Integer>> iterRight = right.entrySet().iterator();

		Entry<String, Integer> nextRight = null;

		while (iterRight.hasNext()) {
			nextRight = iterRight.next();

			left.put(nextRight.getKey(), nextRight.getValue());
		}

		return left;
	}

	/**
	 * 进行文件的合并操作
	 * 
	 * @param fileList
	 * @param index
	 * @return
	 */
	private Map<String, Integer> fileMargeCount(String[] fileList, int index) {
		Map<String, Integer> map = new HashMap<>();

		FileReader read = null;
		BufferedReader bufferRead = null;

		String line = null;
		String[] arrayLine = null;
		try {
			read = new FileReader(fileList[index]);
			bufferRead = new BufferedReader(read);

			while ((line = bufferRead.readLine()) != null) {
				arrayLine = line.split(" ");

				for (int i = 0; i < arrayLine.length; i++) {
					Integer value = map.get(arrayLine[i]);

					if (null == value) {
						value = 1;
					} else {
						value = value + 1;
					}
					map.put(arrayLine[i], value);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.colse(bufferRead);
			IOUtils.colse(read);
		}

		return map;
	}

	public static void main(String[] args) {

		ForkJoinPool forjoin = new ForkJoinPool();

		String path = ForkjoinTextCount.class.getClassLoader().getResource("forkcount").getPath();

		System.out.println("路径 :" + path);

		String[] files = { path + "/1.txt", path + "/2.txt", path + "/3.txt" };

		System.out.println(files.length);

		ForkjoinTextCount count = new ForkjoinTextCount(files, 1, files.length);

		Future<Map<String, Integer>> forkRsp = forjoin.submit(count);

		Map<String, Integer> result = null;

		try {
			result = forkRsp.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println(result);
	}

}
