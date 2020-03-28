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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 对称加密工具类
 * <p>
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * <p>
 * 在官方网站下载JCE无限制权限策略文件
 * JDK1.7 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
 * JDK1.8 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * <p>
 * 下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
 * 如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件
 * 如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件
 *
 * @author vincent
 * @version 1.0 2017/8/20 12:18
 */
public class AesEncryptor {

    private static final String CHARSET = "UTF-8";

    private static final int BLOCK_LENGTH = 16;

    public static final Algorithm ALGORITHM = Algorithm.AES;

    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_CFB = "AES/CFB/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_OFB = "AES/OFB/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_PCBC = "AES/PCBC/PKCS5Padding";

    /**
     * AES/CBC/NoPadding 要求密钥必须是16位的；Initialization vector (IV) 必须是16位
     * 待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常：
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
     * 由于固定了位数，所以对于被加密数据有中文的, 加、解密不完整
     * 可以看到，在原始数据长度为16的整数n倍时，假如原始数据长度等于16*n，则使用NoPadding时加密后数据长度等于16*n，
     * 其它情况下加密数据长 度等于16*(n+1)。在不足16的整数倍的情况下，假如原始数据长度等于16*n+m[其中m小于16]，
     * 除了NoPadding填充之外的任何方 式，加密数据长度都等于16*(n+1).
     */
    public static final String CIPHER_ALGORITHM_CBC_NO_PADDING = "AES/CBC/NoPadding";
    public static final String CIPHER_ALGORITHM_ECB_NO_PADDING = "AES/ECB/NoPadding";
    public static final String CIPHER_ALGORITHM_CFB_NO_PADDING = "AES/CFB/NoPadding";
    public static final String CIPHER_ALGORITHM_OFB_NO_PADDING = "AES/OFB/NoPadding";
    public static final String CIPHER_ALGORITHM_PCBC_NO_PADDING = "AES/PCBC/NoPadding";

    public static final String CIPHER_ALGORITHM_CBC_IS_O10126_PADDING = "AES/CBC/ISO10126Padding";
    public static final String CIPHER_ALGORITHM_ECB_IS_O10126_PADDING = "AES/ECB/ISO10126Padding";
    public static final String CIPHER_ALGORITHM_CFB_IS_O10126_PADDING = "AES/CFB/ISO10126Padding";
    public static final String CIPHER_ALGORITHM_OFB_IS_O10126_PADDING = "AES/OFB/ISO10126Padding";
    public static final String CIPHER_ALGORITHM_PCBC_IS_O10126_PADDING = "AES/PCBC/ISO10126Padding";

    private AesEncryptor() {

    }

    /**
     * 通过aes加密
     *
     * @param source          byte[] 需要加密字节
     * @param secretKey       byte[] 密钥
     * @param cipherAlgorithm String 加密模式
     * @param iv              byte[] 加密向量
     * @return 加密字节
     * @throws Exception 异常
     */
    public static byte[] encryptByAES(byte[] source, byte[] secretKey, String cipherAlgorithm, byte[] iv) throws Exception {
        // KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        // keyGenerator.init(128, new
        // SecureRandom(secretKey.getBytes(CHARSET)));
        // SecretKey key = keyGenerator.generateKey();

        SecretKeySpec key = new SecretKeySpec(secretKey, ALGORITHM.getKey());
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用加密模式初始化 密钥
        if (iv == null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            if (iv.length < BLOCK_LENGTH) {
                throw new IllegalAccessError("iv's length must at least 16");
            }
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv, 0, 16));
        }
        return cipher.doFinal(source);
    }

    /**
     * 通过aes加密
     *
     * @param source          byte[] 需要加密字节
     * @param secretKey       byte[] 密钥
     * @param cipherAlgorithm String 加密模式
     * @return 加密字节
     * @throws Exception 异常
     */
    public static byte[] encryptByAES(byte[] source, byte[] secretKey, String cipherAlgorithm) throws Exception {
        return encryptByAES(source, secretKey, cipherAlgorithm, secretKey);
    }

    /**
     * 加密，使用默认向量
     *
     * @param plainText       String 需要加密字符串
     * @param secretKey       String 加密密钥
     * @param cipherAlgorithm String 加密模式
     * @return 加密字节
     * @throws Exception 异常
     */
    public static byte[] encryptByAES(String plainText, String secretKey, String cipherAlgorithm) throws Exception {
        return encryptByAES(plainText.getBytes(CHARSET), secretKey.getBytes(CHARSET), cipherAlgorithm);
    }

    /**
     * 加密，使用默认向量
     *
     * @param plainText       String 需要加密字符串
     * @param secretKey       String 加密密钥
     * @param cipherAlgorithm String 加密模式
     * @return 加密字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, String secretKey, String cipherAlgorithm) throws Exception {
        byte[] dest = encryptByAES(plainText.getBytes(CHARSET), secretKey.getBytes(CHARSET), cipherAlgorithm);
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * ECB加密模式是最简单的模式，每一个数据块之间没有任何关系。因此它不需要也不能使用IV（初始化向量：Initialization
     * vector）。默认的加密模式就是ECB（直接使用"AES"获取算法时
     *
     * @param plainText String 需要加密字符串
     * @param secretKey String 加密密钥
     * @return 加密字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, String secretKey) throws Exception {
        return encrypt(plainText, secretKey, CIPHER_ALGORITHM_ECB);
    }

    /**
     * 解密
     *
     * @param encryptSource   byte[] 需要解密字节
     * @param secretKey       byte[] 密钥
     * @param cipherAlgorithm String 加密模式
     * @param iv              byte[] 加密向量
     * @return 解密字节
     * @throws Exception 异常
     */
    public static byte[] decryptByAES(byte[] encryptSource, byte[] secretKey, String cipherAlgorithm, byte[] iv) throws Exception {
        // KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        // keyGenerator.init(128, new
        // SecureRandom(secretKey.getBytes(CHARSET)));
        // SecretKey key = keyGenerator.generateKey();

        SecretKeySpec key = new SecretKeySpec(secretKey, ALGORITHM.getKey());
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);

        // 使用解密模式初始化 密钥
        if (iv == null) {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } else {
            if (iv.length < BLOCK_LENGTH) {
                throw new IllegalAccessError("iv's length must at least 16");
            }
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv, 0, 16));
        }
        return cipher.doFinal(encryptSource);
    }

    /**
     * 解密
     *
     * @param encryptSource   byte[] 需要解密字节
     * @param secretKey       byte[] 密钥
     * @param cipherAlgorithm String 加密模式
     * @return 解密字节
     * @throws Exception 异常
     */
    public static byte[] decryptByAES(byte[] encryptSource, byte[] secretKey, String cipherAlgorithm) throws Exception {
        return decryptByAES(encryptSource, secretKey, cipherAlgorithm, secretKey);
    }

    /**
     * 通过aes解密
     *
     * @param encryptText     String 需要解密字符串
     * @param secretKey       String 密钥
     * @param cipherAlgorithm String 加密模式
     * @return 解密字节
     * @throws Exception 异常
     */
    public static byte[] decryptByAES(String encryptText, String secretKey, String cipherAlgorithm) throws Exception {
        return decryptByAES(Byte2Hex.hex2Bytes(encryptText), secretKey.getBytes(CHARSET), cipherAlgorithm);
    }

    /**
     * AES/ECB/PKCS5Padding 解密
     *
     * @param encryptText String 需要解密字符串
     * @param secretKey   String 密钥
     * @return 解密字符串
     * @throws Exception 异常
     */
    public static String decrypt(String encryptText, String secretKey) throws Exception {
        return decrypt(encryptText, secretKey, CIPHER_ALGORITHM_ECB);
    }

    /**
     * 解密，使用默认向量
     *
     * @param encryptText     String 需要加密字符串
     * @param secretKey       String 加密密钥
     * @param cipherAlgorithm String 加密模式
     * @return 解密字符串
     * @throws Exception 异常
     */
    public static String decrypt(String encryptText, String secretKey, String cipherAlgorithm) throws Exception {
        byte[] dest = decryptByAES(encryptText, secretKey, cipherAlgorithm);
        return new String(dest, CHARSET);
    }
}
