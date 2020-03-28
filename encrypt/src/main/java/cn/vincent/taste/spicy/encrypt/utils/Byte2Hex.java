/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.encrypt.utils;

import java.util.Formatter;

/**
 * 字节与16进制字符串互相转换
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:35
 */
public class Byte2Hex {

    private Byte2Hex() {

    }

    /**
     * 字节转换16进制字符串
     *
     * @param b byte 需要转换的字节
     * @return 16进制字符串
     */
    public static String byte2Hex(byte b) {
        int length = 2;
        StringBuilder hex = new StringBuilder(Integer.toHexString(b));
        if (hex.length() > length) {
            hex = new StringBuilder(hex.substring(hex.length() - 2));
        }
        while (hex.length() < length) {
            hex.insert(0, "0");
        }
        return hex.toString();
    }

    /**
     * 字节数组转换16进制字符串
     *
     * @param bytes 需要转换的字节数组
     * @return 16进制字符串
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String hash = formatter.toString();
        formatter.close();
        return hash;
    }

    /**
     * 16进制字符串转换成二进制数组
     *
     * @param hexString 需要转换的16进制字符串
     * @return 转换后的二进制数控
     */
    public static byte[] hex2Bytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
