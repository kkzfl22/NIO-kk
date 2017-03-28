package com.kk.nio.socket.multreactor.procchain.chain;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.kk.nio.socket.multreactor.procchain.ChainEnum;
import com.kk.nio.socket.multreactor.procchain.ContextChain;
import com.kk.nio.socket.multreactor.procchain.MsgProcessInf;
import com.kk.nio.socket.util.CmdUtils;

/**
 * 进行消息的命令的处理
 * 
 * @since 2017年3月28日 下午7:17:52
 * @version 0.0.1
 * @author liujun
 */
public class MsgCommandProc implements MsgProcessInf {

	@Override
	public boolean invoke(ContextChain seqList) throws IOException {

		String line = (String) seqList.getValue(ChainEnum.CHAIN_DECODE_VALUE.getKey());

		// 3,检查是否需要执行命令,将数据写入返回
		if (null != line && !"".equals(line)) {
			System.out.println("收到数据:"+line);
			// 取消事件注册,，因为要应答数据
			seqList.getSelectKey().interestOps(seqList.getSelectKey().interestOps() & ~SelectionKey.OP_READ);
			// 执行命令
			String msg = CmdUtils.runCmd(line);

			// 进行结果数据写入
			try {
				seqList.writeData(msg.getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return seqList.nextDoInvoke();
	}

}
