package com.kk.nio.socket.httpserver.pervlet.seq;

public interface ParamConfig {

	/**
	 * str类型的字符信息
	 * 
	 * @author liujun
	 * 
	 * @vsersion 0.0.1
	 */
	enum CONFIG_STR {

		/**
		 * 核心文件信息
		 */
		PERVLET_CORE_FILE("pervlet.core.file"),

		/**
		 * 生成文件路径
		 */
		PERVLET_CREATE_FILEPATH("pervlet.create.file.path"),

		/**
		 * 动态编译的文件的路径
		 */
		PERVLET_COMPILER_FILE("pervlet.compiler.file"),
		
		
		/**
		 * socket输入流信息
		 */
		PERVLET_SOCKET_INPUT("pervlet.socket.input"),

		
		/**
		 * socket输出流信息
		 */
		PERVLET_SOCKET_OUTPUT("pervlet.socket.output"),
		
		
		/**
		 * 当前的参数map信息
		 */
		PERVLET_PARAM_MAP("pervlet.param.map"),
		;

		private String key;

		private CONFIG_STR(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	/**
	 * int类型的参数配制
	 * 
	 * @author liujun
	 * 
	 * @vsersion 0.0.1
	 */
	enum CONFIG_INT {

		/**
		 */
		EXEC_SQL_RECOMINATION(1),

		;
		private int key;

		private CONFIG_INT(int key) {
			this.key = key;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

	}

}
