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

package cn.vincent.taste.spicy.encrypt;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.UrlBase64;

/**
 * BASE64编码解码工具包
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:42
 */
public class Base64Encryptor {

    private static final String CHARSET = "UTF-8";

    private Base64Encryptor() {
    }

    /**
     * BASE64字符串解码为二进制数据
     *
     * @param base64 String 需要解码字符串
     * @return 解码后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes(CHARSET));
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes byte[] 编码字节数组
     * @return 编码后字符串
     * @throws Exception 异常
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes), CHARSET);
    }

    /**
     * 加密，使用UFT-8编码转换成字符串
     *
     * @param key byte[] 编码字节数组
     * @return 加密后字符串
     * @throws Exception 异常
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return encryptBase64(key, CHARSET);
    }

    /**
     * 加密
     *
     * @param key      byte[] 编码字节数组
     * @param encoding String 字符串转换编码
     * @return 加密后字符串
     * @throws Exception 异常
     */
    public static String encryptBase64(byte[] key, String encoding) throws Exception {
        byte[] b = UrlBase64.encode(key);
        return new String(b, encoding);
    }

    /**
     * 解密，使用UFT-8编码转换成字符串
     *
     * @param key String 需要解密字符串
     * @return 解密后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return decryptBase64(key, CHARSET);
    }

    /**
     * 解密
     *
     * @param key      String 需要解密字符串
     * @param encoding String 字符串转换编码
     * @return 解密后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decryptBase64(String key, String encoding) throws Exception {
        return UrlBase64.decode(key.getBytes(encoding));
    }
}
