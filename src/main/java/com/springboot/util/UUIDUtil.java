package com.springboot.util;

import java.util.UUID;

/**
 * UUID工具类
 */
@SuppressWarnings("all")
public class UUIDUtil {

	/**
	 * 通过jdk自带的uuid生成器生成36位的uuid
	 * @return
	 */
	public static String get36UUID() {
		// 使用JDK自带的UUID生成器
		return UUID.randomUUID().toString();
	}

	/**
	 * 通过jdk自带的uuid生成器生成32位的uuid
	 * @return
	 */
	public static String get32UUID() {
		// 使用JDK自带的UUID生成器
		return UUID.randomUUID().toString().replace("-","");
	}

	public static void main(String[] args) {
		String a = get36UUID();
		String b = get32UUID();
		System.out.println(a.length());
		System.out.println(b.length());
	}
}
