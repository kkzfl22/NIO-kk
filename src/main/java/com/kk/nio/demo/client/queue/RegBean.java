package com.kk.nio.demo.client.queue;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.kk.nio.demo.client.MysqlClientIOHandlerBase;

public class RegBean {

	/**
	 * 通道对象
	 */
	private SocketChannel socketChannel;

	/**
	 * 进行注册的select对象信息
	 */
	private Selector select;

	/**
	 * 在注册完成后由对象设置
	 */
	private SelectionKey currSelectKey;

	/**
	 * 处理对象信息
	 */
	private MysqlClientIOHandlerBase handler;

	public RegBean(SocketChannel socketChannel, Selector select, MysqlClientIOHandlerBase handler) {
		super();
		this.socketChannel = socketChannel;
		this.select = select;
		this.handler = handler;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public Selector getSelect() {
		return select;
	}

	public void setSelect(Selector select) {
		this.select = select;
	}

	public SelectionKey getCurrSelectKey() {
		return currSelectKey;
	}

	public void setCurrSelectKey(SelectionKey currSelectKey) {
		this.currSelectKey = currSelectKey;
	}

	public MysqlClientIOHandlerBase getHandler() {
		return handler;
	}

	public void setHandler(MysqlClientIOHandlerBase handler) {
		this.handler = handler;
	}

}
