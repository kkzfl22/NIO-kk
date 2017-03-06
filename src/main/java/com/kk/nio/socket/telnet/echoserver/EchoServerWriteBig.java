package com.kk.nio.socket.telnet.echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 进行echo的服务的开发操作
 * 
 * 网速太快的假象,一次写完了缓冲区数倍大小的数据
 * @author kk
 * @time 2017年3月6日
 * @version 0.0.1
 */
public class EchoServerWriteBig {
	
	/**
	 * 启动服务操作
	 * @param port 端口信息
	 * @throws IOException 
	 */
	public void startUp(int port) throws IOException
	{
		//构造选择器对象
		Selector select = Selector.open();
		
		//构造服务端socket对象
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		
		serverSocket.configureBlocking(false);
		
		//绑定具体到端口
		serverSocket.bind(new InetSocketAddress( port));
		
		
		//注册连接事件
		serverSocket.register(select, SelectionKey.OP_ACCEPT);
		
		while(true)
		{
			//每隔0.5秒进行一次尝试激活
			int selectnum = select.select(500);
			
			//System.out.println("已经激活的键信息:"+selectnum);
			
			Iterator<SelectionKey> iterSelkey =  select.selectedKeys().iterator();
			
			while(iterSelkey.hasNext())
			{
				SelectionKey selKey = iterSelkey.next();
				
				//如果当前为连接事件，进行处理
				if((selKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT )
				{
					//获得通道
					ServerSocketChannel serverSocketChan = (ServerSocketChannel) selKey.channel();
					
					//进行连接处理
					SocketChannel sockChannel = serverSocketChan.accept();
					
					sockChannel.configureBlocking(false);
					
					//注册读取事件
					sockChannel.register(select, SelectionKey.OP_READ);
					
					//写入提示信息
					sockChannel.write(ByteBuffer.wrap("Welcome come to kk echo server!\r\n".getBytes()));
					
				}
				//进行读事件处理
				else if((selKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ)
				{
					System.out.println("received read event");
					SocketChannel socketChannel = (SocketChannel) selKey.channel();
					
					ByteBuffer buffer = ByteBuffer.allocate(100);
					
					socketChannel.read(buffer);
					
					//得到缓冲区的大小
					int bufferSize = socketChannel.socket().getSendBufferSize();
					
					System.out.println("send buffer size:"+bufferSize);
					
					buffer = ByteBuffer.allocate(bufferSize*50+2);
					
					for (int i = 0; i < buffer.capacity()-2; i++) {
						buffer.put((byte)('a'+i%25));
					}
					
					buffer.put("\r\n".getBytes());
					buffer.flip();
					int writed = socketChannel.write(buffer);
					System.out.println("writed "+writed);
					
					//如果还有未写完的元素
					if(buffer.hasRemaining())
					{
						//剩余的buffer数
						System.out.println("not write finish ,remains "+buffer.remaining());
					}
				}
				
				iterSelkey.remove();
					
			}
			
		}
	}
	
	public static void main(String[] args) {
		EchoServerWriteBig server = new EchoServerWriteBig();
		try {
			int port = 94;
			
			System.out.println("服务启动中！端口："+port);
			server.startUp(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
