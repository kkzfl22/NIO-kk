package com.kk.nio.socket.four.mem;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 实现一个简单的多线程能力的DirectByteBufferPool， *
 * 里面存放DirectByteBuffer，结合TreeSet这种可以范围查询的数据结构 ， 实现任意大小的ByteBuffer的分配复用能力，
 * 比如需要一个1024大小的ByteBuffer，则可以返回大于1024的某个最小的空闲ByteBuffer
 * 
 * @since 2017年3月20日 上午10:49:40
 * @version 0.0.1
 * @author liujun
 */
public class DirectByteBufferChunk implements Comparable<DirectByteBufferChunk> {

	/**
	 * 内存池对象信息
	 */
	private ByteBuffer bufferPool;

	/**
	 * 节点保存信息
	 */
	private BitSet memoryUse;

	/**
	 * 是否锁定标识 isLock
	 */
	protected AtomicBoolean isLock = new AtomicBoolean(false);

	/**
	 * 单块的总大小
	 */
	private int itemChunkinit = 1024 * 1024;

	/**
	 * 单个块的大小
	 */
	private int bufferSize;

	/**
	 * 计算得出的块数
	 */
	private int chunkNum;

	/**
	 * 初始化内存池信息
	 */
	public DirectByteBufferChunk(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * 初始化内存池信息
	 * 
	 * @param chunkSumSize
	 *            整个块的大小
	 * @param itemSize
	 *            单个块池的大小
	 */
	public DirectByteBufferChunk(int chunkSumSize, int itemSize) {

		if (chunkSumSize > itemChunkinit) {
			itemChunkinit = chunkSumSize;
		}

		try {
			// 如果成功则进行内存对象信息的分配
			if (isLock.compareAndSet(false, true)) {

				// 计算块数
				chunkNum = itemChunkinit / itemSize;
				this.bufferSize = itemSize;

				// 首先进行内存的分配，默认为1M的大小
				bufferPool = ByteBuffer.allocateDirect(itemChunkinit);

				memoryUse = new BitSet(chunkNum);

				for (int i = 0; i < chunkNum; i++) {
					memoryUse.set(i, true);
				}
			} else {
				bufferPool = null;
				memoryUse = null;
				return;
			}
		} finally {
			isLock.set(false);
		}

	}

	/**
	 * 进行内存的分配
	 * 
	 * @return 返回 分配的内存
	 */
	public ByteBuffer alloctMemory() {
		try {
			// 如果成功则进行内存对象信息的分配
			if (isLock.compareAndSet(false, true)) {

				for (int i = 0; i < memoryUse.length(); i++) {
					if (memoryUse.get(i)) {
						memoryUse.set(i, false);
						// 计算当前的内存块信息
						bufferPool.position(bufferSize * i);
						bufferPool.limit(bufferSize * (i + 1));
						return bufferPool.slice();
					}
				}

			}
		} finally {
			isLock.set(false);
		}
		return null;
	}

	/**
	 * 进行内存加收
	 * 
	 * @param byteBuffer
	 *            需加收的buffer信息
	 */
	public void recycle(ByteBuffer byteBuffer) {
		try {
			// 如果成功则进行内存对象信息的回收
			if (isLock.compareAndSet(false, true)) {
				// 获得内存buffer
				sun.nio.ch.DirectBuffer thisNavBuf = (sun.nio.ch.DirectBuffer) byteBuffer;
				// attachment对象在buf.slice();的时候将attachment对象设置为总的buff对象
				sun.nio.ch.DirectBuffer parentBuf = (sun.nio.ch.DirectBuffer) thisNavBuf.attachment();

				int startChunk = (int) ((thisNavBuf.address() - parentBuf.address()) / this.bufferSize);

				memoryUse.set(startChunk, true);
			}
		} finally {
			isLock.set(false);
		}
	}

	public static void main(String[] args) {
		DirectByteBufferChunk pool = new DirectByteBufferChunk(10, 1024 * 1024);
		ByteBuffer buffer = pool.alloctMemory();
		ByteBuffer buffer2 = pool.alloctMemory();

		pool.recycle(buffer);
		pool.recycle(buffer2);

		System.out.println(pool);

	}

	@Override
	public int compareTo(DirectByteBufferChunk o) {
		if (this.bufferSize > o.bufferSize) {
			return 1;
		} else if (this.bufferSize < o.bufferSize) {
			return -1;
		}
		return 0;
	}

}
