package com.springboot.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具类
 */
@SuppressWarnings("all")
public class AESUtil {

	private static final String AES = "AES";
	/**
	 * 必须16个字节
	 */
	private static final String CRYPT_KEY = "dongyangyangpuff";

	/**
	 * 加密
	 * @param encryptStr
	 * @return
	 */
	public static byte[] encrypt(byte[] src, String key)  {
		try{
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			return cipher.doFinal(src);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @param decryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String key)  {
		try{
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			return cipher.doFinal(src);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 解密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes()), CRYPT_KEY), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public final static String encrypt(String data) {
		try{
			return byte2hex(encrypt(data.getBytes("UTF-8"), CRYPT_KEY));
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)  {
		String string = encrypt("曹杰");
		System.out.println(string);
		String string2 = decrypt(string);
		System.out.println(string2);
	}
}
