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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

/**
 * RSA公钥/私钥/签名工具包
 * <p>
 * RSA公钥加密算法是1977年由罗纳德·李维斯特（Ron Rivest）、阿迪·萨莫尔（Adi Shamir）和伦纳德·阿德曼（Leonard Adleman）一起提出的。1987年首次公布，当时他们三人都在麻省理工学院工作。RSA就是他们三人姓氏开头字母拼在一起组成的。
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式,由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 * @author vincent
 * @version 1.0 2017/8/20 14:00
 */
public class RsaEncryptor {

    /**
     * 加密算法RSA
     */
    public static final Algorithm ALGORITHM = Algorithm.RSA;
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private RsaEncryptor() {

    }

    /**
     * 获取密钥对
     *
     * @return 包含公私钥的map
     * @throws Exception 异常
     */
    public static List<Key> getKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM.getKey());
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return Arrays.asList(publicKey, privateKey);
    }

    /**
     * 使用模和指数生成公钥
     *
     * @param modulus  String 模
     * @param exponent String 指数
     * @return 公钥
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) throws Exception {
        BigInteger b1 = new BigInteger(modulus);
        BigInteger b2 = new BigInteger(exponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用模和指数生成私钥
     *
     * @param modulus  String 模
     * @param exponent String 指数
     * @return 私钥
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) throws Exception {
        BigInteger b1 = new BigInteger(modulus);
        BigInteger b2 = new BigInteger(exponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥
     * @return 私钥
     * @throws Exception 异常
     */
    public static String getPrivateKey(Key privateKey) throws Exception {
        return Base64Encryptor.encode(privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥
     * @return 公钥
     * @throws Exception 异常
     */
    public static String getPublicKey(Key publicKey) throws Exception {
        return Base64Encryptor.encode(publicKey.getEncoded());
    }

    private static ByteArrayOutputStream encodeOrDecode(byte[] data, Cipher cipher, int maxBlock) throws BadPaddingException, IllegalBlockSizeException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        return out;
    }

    /**
     * 公钥加密
     *
     * @param data      byte[] 源数据
     * @param publicKey String 私钥(BASE64编码)
     * @return 加密字节
     * @throws Exception 异常
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        ByteArrayOutputStream out = encodeOrDecode(data, cipher, MAX_ENCRYPT_BLOCK);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥加密
     *
     * @param data       byte[] 源数据
     * @param privateKey String 私钥(BASE64编码)
     * @return 加密字节
     * @throws Exception 异常
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        ByteArrayOutputStream out = encodeOrDecode(data, cipher, MAX_ENCRYPT_BLOCK);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 公钥解密
     *
     * @param encryptedData byte[] 已加密数据
     * @param publicKey     String 公钥(BASE64编码)
     * @return 解密字节
     * @throws Exception 异常
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        ByteArrayOutputStream out = encodeOrDecode(encryptedData, cipher, MAX_DECRYPT_BLOCK);
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 私钥解密
     *
     * @param encryptedData byte[] 已加密数据
     * @param privateKey    String 私钥(BASE64编码)
     * @return 解密字节
     * @throws Exception 异常
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        ByteArrayOutputStream out = encodeOrDecode(encryptedData, cipher, MAX_DECRYPT_BLOCK);
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       byte[] 已加密数据
     * @param privateKey String 私钥(BASE64编码)
     * @return 签名字符串
     * @throws Exception 异常
     */
    public static String getSignature(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Encryptor.encode(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      byte[] 已加密数据
     * @param publicKey String 公钥(BASE64编码)
     * @param sign      String 签名
     * @return 验证结果true/false
     * @throws Exception 异常
     */
    public static boolean verifySignature(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Encryptor.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM.getKey());
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Encryptor.decode(sign));
    }
}
