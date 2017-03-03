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
