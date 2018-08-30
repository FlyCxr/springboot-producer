package com.springboot.netty.common;

import java.util.Arrays;

public class ByteUtil {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * byte[]转16进制字符串
     * @param bytes
     * @return
     */
    public static String format(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * 16进制字符串转byte[]
     * @param hex
     * @return
     */
    public static byte[] toBytes(String hex) {
        if(hex == null || hex.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < hex.length() / 2; i++) {
            String subStr = hex.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static void main(String[] args) {
        String str = "e6b58be8af95";
        byte[] bytes = toBytes(str);
        System.out.println("转换后的字节数组：" + Arrays.toString(bytes));

        String format = format(bytes);
        System.out.println(format);
    }
}
