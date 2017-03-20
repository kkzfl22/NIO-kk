package com.kk.nio.socket.four;

import java.nio.ByteBuffer;
import java.util.TreeSet;
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
public class DirectByteBufferPool {

	/**
	 * 进行容量的存储
	 */
	private final ByteBuffer[] bufferPool;

	/**
	 * 使用的buffer的记录
	 */
	private final TreeSet<Integer> useSet;

	/**
	 * 最大的内存块大小
	 */
	private int bufferSize;

	/**
	 * 是否锁定标识 isLock
	 */
	protected AtomicBoolean isLock = new AtomicBoolean(false);

	/**
	 * 初始化内存池信息
	 * 
	 * @param poolSize
	 *            池信息
	 * @param bufferSize
	 *            单内内存块大小
	 */
	public DirectByteBufferPool(int poolSize, int bufferSize) {
		try {
			// 如果成功则进行内存对象信息的分配
			if (isLock.compareAndSet(false, true)) {

				this.bufferSize = bufferSize;

				bufferPool = new ByteBuffer[poolSize];
				useSet = new TreeSet<>();

				// 进行单个内存大小的分配
				for (int i = 0; i < bufferPool.length; i++) {
					bufferPool[i] = ByteBuffer.allocateDirect(bufferSize);
					useSet.add(i);
				}
			} else {
				bufferPool = null;
				useSet = null;
				return;
			}
		} finally {
			isLock.set(false);
		}

	}

	/**
	 * 进行内存的分配
	 * 
	 * @param size
	 *            大小信息
	 * @return 返回 分配的内存
	 */
	public ByteBuffer alloctMemory(int size) {
		try {
			// 如果成功则进行内存对象信息的分配
			if (isLock.compareAndSet(false, true)) {
				if (size > bufferSize) {
					throw new RuntimeException("curr size too big");
				} else {
					int index = useSet.pollFirst();
					return bufferPool[index];
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
				for (int i = 0; i < this.bufferPool.length; i++) {
					if (bufferPool[i] == byteBuffer) {
						useSet.add(i);
						byteBuffer.position(0);
						byteBuffer.limit(bufferSize);
						break;
					}
				}
			}
		} finally {
			isLock.set(false);
		}
	}

	public static void main(String[] args) {
		DirectByteBufferPool pool = new DirectByteBufferPool(10, 1024);
		ByteBuffer buffer = pool.alloctMemory(512);
		ByteBuffer buffer2 = pool.alloctMemory(512);

		pool.recycle(buffer);
		pool.recycle(buffer2);

		System.out.println(pool);

	}

}
