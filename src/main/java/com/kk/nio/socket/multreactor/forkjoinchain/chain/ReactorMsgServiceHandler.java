package com.kk.nio.socket.multreactor.forkjoinchain.chain;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * 进行消息的业务处理
 * 
 * @since 2017年3月29日 下午5:34:39
 * @version 0.0.1
 * @author liujun
 */
public class ReactorMsgServiceHandler implements MsgDataServiceInf {

	/**
	 * 消息编解码对象信息
	 */
	private final MsgEnDecodeInf<String> msgEndecode;

	/**
	 * forkjoin时间
	 */
	private ForkJoinPool forjoin = new ForkJoinPool();;

	/**
	 * 缓存 队列
	 */
	private final ConcurrentMap<String, Future<Map<String, Integer>>> CACHE_FUTURE = new ConcurrentHashMap<String, Future<Map<String, Integer>>>();

	public ReactorMsgServiceHandler(MsgEnDecodeInf<String> msgEndecode) {
		super();
		this.msgEndecode = msgEndecode;
	}

	@Override
	public void readData(Context context) throws IOException {
		// 进行消息的解码操作
		String msg = this.msgEndecode.msgDecode(context);

		ByteBuffer readerBuffer = context.getReadBuffer();

		// 进行压缩
		// 4,当容量超过总容量的2分之一大小则需要压缩
		if (readerBuffer.position() > readerBuffer.capacity() / 2) {
			// 重新标识position,将当前的容量标识为buffer中已有数据的容量
			readerBuffer.limit(readerBuffer.position());
			// 标识当前pos为上一次已经读取到的数据的pos
			readerBuffer.position(context.getLastModPositon());

			// 压缩数据,即将上一次读取之前的记录丢弃
			readerBuffer.compact();

			context.setLastModPositon(0);
		}

		if (null != msg && !msg.isEmpty()) {
			// 取消事件注册，因为要应答数据
			context.getSelectKey().interestOps(context.getSelectKey().interestOps() & ~SelectionKey.OP_READ);

			if (msg.contains("2")) {
				context.getSelectKey().cancel();
				context.getSocketChannel().socket().close();
			}
			// 使用forkjoin进行单词的统计
			else if (msg.contains("1")) {
				// 进行文本的单词统计
				String dir = msg.substring(msg.indexOf(" ") + 1);

				System.out.println("当前key:" + dir);

				File filedir = new File(dir);

				if (filedir.exists() && filedir.isDirectory()) {

					Future<Map<String, Integer>> forkRsp = CACHE_FUTURE.get(dir);

					if (null == forkRsp) {

						// 构建异步计算的并获取结果的缓存数据
						File[] pathArray = filedir.listFiles();
						// 进行计算
						ForkjoinTextCount count = new ForkjoinTextCount(pathArray, 1, pathArray.length);
						Future<Map<String, Integer>> futureCount = forjoin.submit(count);

						forkRsp = CACHE_FUTURE.put(dir, futureCount);

						// 最终只能一个放入成功
						if (null == forkRsp) {
							// 仅能执行一次，后面都会直接获取结果
							forkRsp = futureCount;

						}
					}
					Map<String, Integer> result = null;

					try {
						result = forkRsp.get();
					}
					// 如果计算取消
					catch (CancellationException e1) {
						e1.printStackTrace();
						CACHE_FUTURE.remove(dir);
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (null != result) {
						// 设置数据响应结果
						context.setWriteData(String.valueOf(result));

						// 将结果进行写入
						this.writeData(context);
					}
				}
			}

		}
	}

	@Override
	public void writeData(Context context) throws IOException {
		// 如果当前需要写入的数据不为空，则进行编码
		if (context.getWriteData() != null) {
			msgEndecode.msgEncode(context);
		}
		// 需要取消录写入事件，进行读取操作
		else {
			context.getSelectKey()
					.interestOps(context.getSelectKey().interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		}

	}

}
