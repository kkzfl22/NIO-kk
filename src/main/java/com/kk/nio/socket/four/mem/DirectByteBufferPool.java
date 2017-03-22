package com.kk.nio.socket.four.mem;

import java.nio.ByteBuffer;
import java.util.TreeSet;

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
	 * 节点保存信息
	 */
	private TreeSet<DirectByteBufferChunk> useSet = new TreeSet<>();

	/**
	 * 初始化内存大小
	 */
	private int initChunkSize = 1024 * 1024;

	/**
	 * 初始化内存池信息
	 * 
	 * @param initSize
	 *            以m为单位的分配
	 */
	public DirectByteBufferPool() {
		// 128字节的大小进行分配
		int index = 128;
		while (initChunkSize / index > 4) {
			useSet.add(new DirectByteBufferChunk(initChunkSize, index));
			index = index * 2;
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
		DirectByteBufferChunk chunk = useSet.ceiling(new DirectByteBufferChunk(size));
		return chunk.alloctMemory();
	}

	/**
	 * 进行内存回收
	 * 
	 * @param byteBuffer
	 *            需加收的buffer信息
	 */
	public void recycle(ByteBuffer byteBuffer) {
		DirectByteBufferChunk chunk = useSet.ceiling(new DirectByteBufferChunk(byteBuffer.limit()));
		chunk.recycle(byteBuffer);
	}

	public static void main(String[] args) {
		DirectByteBufferPool pool = new DirectByteBufferPool();

		ByteBuffer buffer = pool.alloctMemory(128);
		ByteBuffer buffer2 = pool.alloctMemory(256);
		
		ByteBuffer buffer3 = pool.alloctMemory(756);
		ByteBuffer buffer4 = pool.alloctMemory(1024);

		pool.recycle(buffer);
		pool.recycle(buffer2);
		pool.recycle(buffer3);
		pool.recycle(buffer4);

		System.out.println(pool);

	}

}
