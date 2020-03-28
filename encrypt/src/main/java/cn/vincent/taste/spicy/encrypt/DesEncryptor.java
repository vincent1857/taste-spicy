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

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加解密
 * <p>
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现。
 * <p>
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 *
 * @author vincent
 * @version 1.0 2017/8/20 13:20
 */
public class DesEncryptor {

    private static final String CHARSET = "UTF-8";

    public static final Algorithm ALGORITHM = Algorithm.DES;

    private DesEncryptor() {

    }

    /**
     * 加密
     *
     * @param datasource byte[] 原始字节
     * @param secretKey  String 加密密钥
     * @return byte[] 加密后字节
     */
    public static byte[] encryptBytes(byte[] datasource, String secretKey) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(secretKey.getBytes(CHARSET));
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM.getKey());
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM.getKey());
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(datasource);
    }

    public static String encrypt(String plainText, String secretKey) throws Exception {
        byte[] dest = encryptBytes(plainText.getBytes(CHARSET), secretKey);
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * 解密
     *
     * @param src       加密后字节
     * @param secretKey 加密密钥
     * @return 解密后/原始字符串
     * @throws Exception 异常
     */
    public static byte[] decryptBytes(byte[] src, String secretKey) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(secretKey.getBytes(CHARSET));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM.getKey());
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM.getKey());
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param src       加密后字符串
     * @param secretKey 加密密钥
     * @return 解密后/原始字符串
     * @throws Exception 异常
     */
    public static String decrypt(String src, String secretKey) throws Exception {
        byte[] dest = decryptBytes(Byte2Hex.hex2Bytes(src), secretKey);
        return new String(dest, CHARSET);
    }
}
