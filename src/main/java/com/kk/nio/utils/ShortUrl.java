package com.kk.nio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShortUrl {

	/**
	 * 短网址生成方法 这个方法会,生成四个短字符串,每一个字符串的长度为6 这个方法是从网上搜索的一个方法,但不知道出自何处了,稍微将key换了一下
	 * 
	 * @param url
	 * @return
	 */
	public static String[] shortUrl(String url) {

		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		String key = UUID.randomUUID().toString();

		// 混淆key,加上当前时间,并且取一个随机字符串
		key = System.currentTimeMillis() + key;
		// 要使用生成 URL 的字符

		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",

				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",

				"u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",

				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",

				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",

				"U", "V", "W", "X", "Y", "Z" };

		// 对传入网址进行 MD5 加密
		String sMD5EncryptResult = MD5Utils.getMD5String(key + url);

		String hex = sMD5EncryptResult;

		String[] resUrl = new String[4];

		for (int i = 0; i < 4; i++) {

			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算

			String sTempSubString = hex.substring(i * 8, i * 8 + 8);

			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
			// long ，则会越界

			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);

			String outChars = "";

			for (int j = 0; j < 6; j++) {

				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引

				long index = 0x0000003D & lHexLong;

				// 把取得的字符相加

				outChars += chars[(int) index];

				// 每次循环按位右移 5 位

				lHexLong = lHexLong >> 5;

			}

			// 把字符串存入对应索引的输出数组

			resUrl[i] = outChars;

		}

		return resUrl;
	}

	/**
	 * 获取我想要的字符串,将生成的两个相加,得到我想要的12位字符
	 * 
	 * @param url
	 * @return
	 */
	public static String getShortUrl(String url) {
		String[] aResult = shortUrl(url);

		return aResult[0] + aResult[1];
	}

	// 测试方法
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < 1000; i++) {
		// sb.append("ab");// 长度为2000的一个字符串
		// }
		
		sb.append("http://www.cnblogs.com/yzuzhang/p/5864214.html");

		String[] strings = shortUrl(sb.toString());
		for (int i = 0; i < strings.length; i++) {
			System.out.println(strings[i]);
		}

		// 验证生成的是否是唯一的
		List<String> list = new ArrayList<>();
		// 生成1000万个短字符,已经足够使用
		for (int i = 0; i < 10000000; i++) {
			list.add(getShortUrl(sb.toString()));
		}

		// 经测试生成1000万个短链接没有重复的
		// 这里的判断重复的方法,是使用的SF上面一位网友的方法,我觉得比较优雅,只使用一次循环,而且也能判断,所以就使用了他的方法
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (String string : list) {
			if (hashMap.get(string) != null) {
				Integer value = hashMap.get(string);
				hashMap.put(string, value + 1);
				System.out.println("the element:" + string + " is repeat");
			} else {
				hashMap.put(string, 1);
			}
		}
	}

}
