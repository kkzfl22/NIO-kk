package com.kk.nio.socket.util;

import java.io.Closeable;

public class IOUtils {

	public static void colse(Closeable colose) {
		if (null != colose) {
			try {
				colose.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
