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

import cn.vincent.taste.spicy.encrypt.utils.Algorithm;
import cn.vincent.taste.spicy.encrypt.utils.Byte2Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 消息摘要
 *
 * @author vincent
 * @version 1.0 2017/8/20 14:43
 */
public class MessageDigestEncryptor {

    private static final String CHARSET = "UTF-8";

    public static final Algorithm ALGORITHM_MD5 = Algorithm.MD5;
    public static final Algorithm ALGORITHM_SHA1 = Algorithm.SHA1;
    public static final Algorithm ALGORITHM_SHA256 = Algorithm.SHA256;
    public static final Algorithm ALGORITHM_SHA512 = Algorithm.SHA512;

    public static final String SIGNATURE_ALGORITHM = "PBEWithMD5AndDES";

    public enum MACHmac {

        // 算法

        HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
    }

    private MessageDigestEncryptor() {

    }

    /**
     * 字节签名
     *
     * @param data byte[] 签名字节数组
     * @return 签名后字节数组
     */
    public static byte[] encryptRaw(String algorithm, byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(algorithm);
        return md5.digest(data);
    }

    /**
     * 字节签名
     *
     * @param data byte[] 签名字节数组
     * @return 签名后字符串
     */
    public static String encryptBytes(String algorithm, byte[] data) throws Exception {
        byte[] encryptData = encryptRaw(algorithm, data);
        if (encryptData == null || encryptData.length == 0) {
            return null;
        }
//        StringBuilder encryptDataString = new StringBuilder();
//        for (byte encryptDatum : encryptData) {
//            encryptDataString.append(Byte2Hex.byte2Hex(encryptDatum));
//        }
//        return encryptDataString.toString();
        return Byte2Hex.byte2Hex(encryptData);
    }

    /**
     * 字符串加密 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
     *
     * @param plainText String 需要签名的字符串
     * @param encoding  String 字符串编码
     * @return 签名字符串
     */
    public static String encrypt(String algorithm, String plainText, String encoding) throws Exception {
        if (plainText == null) {
            return null;
        }
        return encryptBytes(algorithm, plainText.getBytes(encoding));
    }

    /**
     * 字符串加密 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
     *
     * @param plainText String 需要签名的字符串
     * @return 签名字符串
     */
    public static String encrypt(String algorithm, String plainText) throws Exception {
        return encrypt(algorithm, plainText, CHARSET);
    }

    /**
     * 文件流（生成文件摘要）
     *
     * @param input InputStream 文件输入流
     * @return 签名串
     * @throws Exception 异常
     */
    public static String encryptStream(String algorithm, InputStream input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = input.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        byte[] b = messageDigest.digest();
        return Byte2Hex.byte2Hex(b);
    }

    public static String md5(String plainText, String encoding) throws Exception {
        return encrypt(ALGORITHM_MD5.getKey(), plainText, encoding);
    }

    public static String md5(String plainText) throws Exception {
        return encrypt(ALGORITHM_MD5.getKey(), plainText);
    }

    public static byte[] md5Raw(byte[] data) throws Exception {
        return encryptRaw(ALGORITHM_MD5.getKey(), data);
    }

    public static String md5Bytes(byte[] data) throws Exception {
        return encryptBytes(ALGORITHM_MD5.getKey(), data);
    }

    public static String md5Stream(InputStream input) throws Exception {
        return encryptStream(ALGORITHM_MD5.getKey(), input);
    }

    /**
     * 用SHA1算法生成安全签名
     * <p>
     * 安全哈希算法（Secure Hash Algorithm）主要适用于数字签名标准 （Digital Signature Standard
     * DSS）里面定义的数字签名算法（Digital Signature Algorithm
     * DSA）。对于长度小于2^64位的消息，SHA1会产生一个160位的消息摘要。当接收到消息的时候，这个消息摘要可以用来验证数据的完整性。
     * 在传输的过程中， 数据很可能会发生变化，那么这时候就会产生不同的消息摘要。
     * SHA1有如下特性：不可以从消息摘要中复原信息；两个不同的消息不会产生同样的消息摘要,(但会有1x10 ^
     * 48分之一的机率出现相同的消息摘要,一般使用时忽略)。
     * </p>
     *
     * @param plainText 原始字符串
     * @return 签名字符串
     * @throws Exception 异常
     */
    public static String sha1(String plainText) throws Exception {
        return encrypt(ALGORITHM_SHA1.getKey(), plainText);
    }

    public static String sha1(String plainText, String encoding) throws Exception {
        return encrypt(ALGORITHM_SHA1.getKey(), plainText, encoding);
    }

    public static byte[] sha1Raw(byte[] data) throws Exception {
        return encryptRaw(ALGORITHM_SHA1.getKey(), data);
    }

    public static String sha1Bytes(byte[] data) throws Exception {
        return encryptBytes(ALGORITHM_SHA1.getKey(), data);
    }

    public static String sha1Stream(InputStream input) throws Exception {
        return encryptStream(ALGORITHM_SHA1.getKey(), input);
    }

    public static String sha256(String plainText) throws Exception {
        return encrypt(ALGORITHM_SHA256.getKey(), plainText);
    }

    public static String sha256(String plainText, String encoding) throws Exception {
        return encrypt(ALGORITHM_SHA256.getKey(), plainText, encoding);
    }

    public static byte[] sha256Raw(byte[] data) throws Exception {
        return encryptRaw(ALGORITHM_SHA256.getKey(), data);
    }

    public static String sha256Bytes(byte[] data) throws Exception {
        return encryptBytes(ALGORITHM_SHA256.getKey(), data);
    }

    public static String sha256Stream(InputStream input) throws Exception {
        return encryptStream(ALGORITHM_SHA256.getKey(), input);
    }

    public static String sha512(String plainText) throws Exception {
        return encrypt(ALGORITHM_SHA512.getKey(), plainText);
    }

    public static String sha512(String plainText, String encoding) throws Exception {
        return encrypt(ALGORITHM_SHA512.getKey(), plainText, encoding);
    }

    public static byte[] sha5126Raw(byte[] data) throws Exception {
        return encryptRaw(ALGORITHM_SHA512.getKey(), data);
    }

    public static String sha512Bytes(byte[] data) throws Exception {
        return encryptBytes(ALGORITHM_SHA512.getKey(), data);
    }

    public static String sha512Stream(InputStream input) throws Exception {
        return encryptStream(ALGORITHM_SHA512.getKey(), input);
    }

    public static byte[] macRaw(byte[] data, char[] secretKey, String hmac) throws Exception {
        if (secretKey == null) {
            return null;
        }
        PBEKeySpec keySpec = new PBEKeySpec(secretKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SIGNATURE_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(keySpec);

        /*
         * 此类提供“消息验证码”（Message Authentication Code，MAC）算法的功能。 MAC
         * 基于秘密密钥提供一种方式来检查在不可靠介质上进行传输或存储的信息的完整性。
         * 通常，消息验证码在共享秘密密钥的两个参与者之间使用，以验证这两者之间传输的信息。 基于加密哈希函数的 MAC 机制也叫作
         * HMAC。结合秘密共享密钥， HMAC 可以用于任何加密哈希函数（如 MD5 或 SHA-1）
         */
        Mac mac = Mac.getInstance(hmac);
        mac.init(key);
        return mac.doFinal(data);
    }

    public static byte[] macRaw(byte[] data, String secretKey, String hmac) throws Exception {
        return macRaw(data, secretKey.toCharArray(), hmac);
    }

    public static byte[] macRaw(String data, String secretKey, String hmac) throws Exception {
        return macRaw(data.getBytes(CHARSET), secretKey.toCharArray(), hmac);
    }

    public static String macBytes(byte[] data, char[] secretKey, String hmac) throws Exception {
        byte[] encryptData = macRaw(data, secretKey, hmac);
        return Byte2Hex.byte2Hex(encryptData);
    }

    public static String macBytes(byte[] data, String secretKey, String hmac) throws Exception {
        byte[] encryptData = macRaw(data, secretKey.toCharArray(), hmac);
        return Byte2Hex.byte2Hex(encryptData);
    }

    public static String mac(String data, String secretKey, String hmac) throws Exception {
        byte[] encryptData = macRaw(data, secretKey, hmac);
        return Byte2Hex.byte2Hex(encryptData);
    }
}
