package com.kk.nio.socket.multreactor.procchain;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用来进行流程执行上下文对象信息
 * 
 * @since 2017年3月28日 下午7:02:27
 * @version 0.0.1
 * @author liujun
 */
public class ContextChain {

	/**
	 * 用来存放流程的容器
	 */
	private List<MsgProcessInf> chainInvoke = new LinkedList<MsgProcessInf>();

	/**
	 * 用来存放参数的集合
	 */
	private Map<String, Object> param = new HashMap<String, Object>();

	/**
	 * 通道信息
	 */
	private final SocketChannel socketChannel;

	/**
	 * 进行写入的buffer信息
	 */
	private volatile ByteBuffer writeBuffer;

	/**
	 * 写的入队列信息
	 */
	private volatile LinkedList<ByteBuffer> writeQueue = new LinkedList<>();

	/**
	 * 写入标识
	 */
	private AtomicBoolean writeFlag = new AtomicBoolean(false);

	/**
	 * 分配的读取的readerbuffer信息
	 */
	private ByteBuffer readerBuffer;

	/**
	 * socket通道信息
	 */
	protected SelectionKey selectKey;

	/**
	 * 最后的位置信息
	 */
	private int lastModPositon;

	public ContextChain(SocketChannel socketChannel, SelectionKey selectKey) {
		this.socketChannel = socketChannel;
		this.readerBuffer = ByteBuffer.allocate(1024);
		this.selectKey = selectKey;
	}

	/**
	 * 添加流程代码
	 * 
	 * @param serviceExec
	 */
	public void addExec(MsgProcessInf serviceExec) {
		this.chainInvoke.add(serviceExec);
	}

	/**
	 * 添加流程代码
	 * 
	 * @param serviceExec
	 *            [] 流程执行数组
	 */
	public void addExec(MsgProcessInf[] serviceExec) {
		if (null != serviceExec) {
			for (int i = 0; i < serviceExec.length; i++) {
				this.chainInvoke.add(serviceExec[i]);
			}
		}
	}

	public void putParam(String key, Object value) {
		this.param.put(key, value);
	}

	public Object getValue(String key) {
		return param.get(key);
	}

	public Map<String, Object> getParam() {
		return param;
	}

	/**
	 * 执行下一个流程代码
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean nextDoInvoke() throws IOException {

		if (null != chainInvoke && chainInvoke.size() > 0) {

			MsgProcessInf servExec = chainInvoke.remove(0);

			return servExec.invoke(this);
		} else {
			// 运行完毕，索引结束
			return true;
		}

	}

	public List<MsgProcessInf> getChainInvoke() {
		return chainInvoke;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	public LinkedList<ByteBuffer> getWriteQueue() {
		return writeQueue;
	}

	public AtomicBoolean getWriteFlag() {
		return writeFlag;
	}

	public ByteBuffer getReaderBuffer() {
		return readerBuffer;
	}

	public SelectionKey getSelectKey() {
		return selectKey;
	}

	public int getLastModPositon() {
		return lastModPositon;
	}

	public void setLastModPositon(int lastModPositon) {
		this.lastModPositon = lastModPositon;
	}

	/**
	 * 进行数据的写入
	 * 
	 * @param data
	 *            写的数据
	 * @throws IOException
	 *             异常信息
	 */
	public void writeData(byte[] data) throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			ByteBuffer theWriteBuffer = writeBuffer;

			// 所有待发送的数据都为空，则直接发送
			if (theWriteBuffer == null && writeQueue.isEmpty()) {
				this.doWriteToChannel(ByteBuffer.wrap(data));
			}
			// 否则将数据加入到待发送的队列中
			else {
				this.writeQueue.add(ByteBuffer.wrap(data));
				// 继续发送当前待发送的数据
				this.doWriteToChannel(theWriteBuffer);
			}
		} finally {
			// release
			writeFlag.lazySet(true);
		}
	}

	/**
	 * 进行数据写入
	 * 
	 * @throws IOException
	 */
	public void writeData() throws IOException {
		// 多次检查，当前仅一个进行发送数据
		while (this.writeFlag.compareAndSet(false, true)) {

		}
		try {
			// 直接写入writebuffer信息
			ByteBuffer theWriteBuffer = writeBuffer;
			this.doWriteToChannel(theWriteBuffer);
		} finally {
			this.writeFlag.lazySet(true);
		}
	}

	/**
	 * 将数据写入通道
	 * 
	 * @param writeBufferinfo
	 * @throws IOException
	 */
	private void doWriteToChannel(ByteBuffer writeBufferinfo) throws IOException {
		int writeLength = socketChannel.write(writeBufferinfo);
		System.out.println("curr write length :" + writeLength);

		// 如果当前还有未发送的完成的数据,则继续保持对当前写事件的兴趣
		if (writeBufferinfo.hasRemaining()) {
			selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_WRITE);
			// 检查当前的buffer是否与发送的buffer是同一个
			if (writeBufferinfo != writeBuffer) {
				this.writeBuffer = writeBufferinfo;
			}
		}
		// 写入完成，则取消写事件
		else {
			System.out.println("data write finish!");
			// 检查当前队列是否已经完成,完成取取消写事件，注册读取事件
			if (this.writeQueue.isEmpty()) {
				System.out.println("wite over ,queue is null,cancel wirte event!");
				selectKey.interestOps(selectKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			}
			// 如果未完成，则遍历队列进行写入
			else {
				ByteBuffer buffer = writeQueue.removeFirst();
				buffer.flip();
				// 进行递归的数据写入
				this.doWriteToChannel(buffer);
			}

		}
	}

}
