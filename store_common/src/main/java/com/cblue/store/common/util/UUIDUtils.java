package com.cblue.store.common.util;

import java.util.UUID;

/**
 * 生成激活码的工具类
 * @author Administrator
 *
 */
public class UUIDUtils {

	public static String getUUID(){
		 String string = UUID.randomUUID().toString().replace("-", "");
		 return string;
	}
	
	public static void main(String[] args) {
		String uuid = getUUID();
		System.out.println(uuid);
	}
}
