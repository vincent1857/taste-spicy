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

package cn.vincent.taste.spicy.encrypt.biz;

import cn.vincent.taste.spicy.encrypt.MessageDigestEncryptor;
import cn.vincent.taste.spicy.encrypt.utils.Algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author vincent
 * @version 1.0 2017/8/20 13:18
 */
public class MessageDigestEncryptorHelper {

    public static final String PARAMS_EQ = "=";
    public static final String PARAMS_SPLIT = "#";

    private MessageDigestEncryptorHelper() {
    }

    /**
     * 参数签名生成算法
     *
     * @param org    String 签名字符串
     * @param secret String 签名密钥
     * @return 签名
     */
    public static String getSignature(String algorithm, String org, String secret) throws Exception {
        return MessageDigestEncryptor.encrypt(algorithm, org + PARAMS_SPLIT + MessageDigestEncryptor.encrypt(algorithm, secret));
    }

    /**
     * 参数签名生成算法
     *
     * @param params 请求参数集
     * @param secret 签名密钥
     * @return 签名串
     */
    public static String getSignature(String algorithm, HashMap<String, ?> params, String secret) throws Exception {
        Map<String, ?> sortedParams = new TreeMap<>(params);
        StringBuilder org = new StringBuilder();
        for (String key : sortedParams.keySet()) {
            org.append(key).append(PARAMS_EQ);
            Object value = sortedParams.get(key);
            if (value != null) {
                org.append(value.toString());
            }
        }
        return getSignature(algorithm, org.toString(), secret);
    }

    /**
     * md5 参数签名生成算法
     *
     * @param params 请求参数集
     * @param secret 签名密钥
     * @return 签名串
     */
    public static String md5Signature(HashMap<String, ?> params, String secret) throws Exception {
        return getSignature(Algorithm.MD5.getKey(), params, secret);
    }

    /**
     * sga1 参数签名生成算法
     *
     * @param params 请求参数集
     * @param secret 签名密钥
     * @return 签名串
     */
    public static String sha1Signature(HashMap<String, ?> params, String secret) throws Exception {
        return getSignature(Algorithm.SHA1.getKey(), params, secret);
    }

    public static String sha256Signature(HashMap<String, ?> params, String secret) throws Exception {
        return getSignature(Algorithm.SHA256.getKey(), params, secret);
    }

    public static String sha512Signature(HashMap<String, ?> params, String secret) throws Exception {
        return getSignature(Algorithm.SHA512.getKey(), params, secret);
    }
}
