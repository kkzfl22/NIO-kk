package com.kk.nio.socket.httpserver.pervlet;

/**
 * ��������servlet��ģ��
 * @author Think
 *
 */
public interface Pervlet {
	
	
	/**
	 * �������ҵ��Ĵ���
	 * @param req ������Ϣ 
	 * @return ��Ӧ��Ϣ
	 */
	public PvResponse process(PvRequest req);
}
