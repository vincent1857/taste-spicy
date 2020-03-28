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

package cn.vincent.taste.spicy.helper.character;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据脱敏工具
 *
 * @author vincent
 * @version 1.0 2017/8/20 03:46
 */
public class DesensitizedHelper {

    private static final String SYMBOL = "*";
    private static final String EMAIL_SYMBOL = "@";

    private static final int CHINESE_NAME_LENGTH = 2;

    private DesensitizedHelper() {

    }

    public static void main(String[] args) {
        System.out.println(chineseMiddleName("彭国卿"));
        System.out.println(chineseMiddleName("彭国"));
        System.out.println(chineseMiddleName("彭国卿12"));
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号&lt;例子：李**&gt;
     *
     * @param fullName 姓名全称
     * @return 脱敏后数据
     */
    public static String chineseName(final String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return StringUtils.EMPTY;
        }
        final String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), SYMBOL);
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号&lt;例子：李**&gt;
     *
     * @param familyName 姓
     * @param givenName  名
     * @return 脱敏后数据
     */
    public static String chineseName(final String familyName, final String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return StringUtils.EMPTY;
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [中文姓名] 隐藏中间的名字
     *
     * @param fullName 姓名全称
     * @return 脱敏后数据
     */
    public static String chineseMiddleName(final String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return StringUtils.EMPTY;
        }
        final String name = StringUtils.left(fullName, 1);
        if (fullName.length() == CHINESE_NAME_LENGTH) {
            return StringUtils.rightPad(name, StringUtils.length(fullName), SYMBOL);
        }

        return StringUtils.rightPad(name, StringUtils.length(fullName) - 1, SYMBOL) + StringUtils.right(fullName, 1);
    }

    /**
     * [中文姓名] 隐藏中间的名字
     *
     * @param familyName 姓
     * @param givenName  名
     * @return 脱敏后数据
     */
    public static String chineseMiddleName(final String familyName, final String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return StringUtils.EMPTY;
        }
        return chineseMiddleName(familyName + givenName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。&lt;例子：*************5762&gt;
     *
     * @param id 身份证号
     * @return 脱敏后数据
     */
    public static String idCardNum(final String id) {
        if (StringUtils.isBlank(id)) {
            return StringUtils.EMPTY;
        }

        return StringUtils.left(id, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(id, 3), StringUtils.length(id), SYMBOL), SYMBOL + SYMBOL + SYMBOL));
    }

    /**
     * [固定电话] 后四位，其他隐藏&lt;例子：****1234&gt;
     *
     * @param num 固定电话
     * @return 脱敏后数据
     */
    public static String fixedPhone(final String num) {
        if (StringUtils.isBlank(num)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), SYMBOL);
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏&lt;例子:138******1234&gt;
     *
     * @param num 手机号码
     * @return 脱敏后数据
     */
    public static String mobilePhone(final String num) {
        if (StringUtils.isBlank(num)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.left(num, 2).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), SYMBOL), SYMBOL + SYMBOL + SYMBOL));

    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护&lt;例子：北京市海淀区****&gt;
     *
     * @param address       地址
     * @param sensitiveSize 敏感信息长度
     * @return 脱敏后数据
     */
    public static String address(final String address, final int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return StringUtils.EMPTY;
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, SYMBOL);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示&lt;例子:g**@163.com&gt;
     *
     * @param email 电子邮箱
     * @return 脱敏后数据
     */
    public static String email(final String email) {
        if (StringUtils.isBlank(email)) {
            return StringUtils.EMPTY;
        }
        final int index = StringUtils.indexOf(email, EMAIL_SYMBOL);
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, SYMBOL).concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号&lt;例子:6222600**********1234&gt;
     *
     * @param cardNum 银行卡号
     * @return 脱敏后数据
     */
    public static String bankCard(final String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), SYMBOL), SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号&lt;例子:12********&gt;
     *
     * @param code 公司开户银行联号
     * @return 脱敏后数据
     */
    public static String cnapsCode(final String code) {
        if (StringUtils.isBlank(code)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), SYMBOL);
    }

    /**
     * [密码] 隐藏成6位&lt;例子:******&gt;
     *
     * @param password 密码
     * @return 脱敏后数据
     */
    public static String password(final String password) {
        return SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL;
    }

    /**
     * [隐藏所有] 按照位数隐藏&lt;例子:******&gt;
     *
     * @param code 字符
     * @return 脱敏后数据
     */
    public static String all(final String code) {
        if (StringUtils.isBlank(code)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.rightPad(StringUtils.EMPTY, StringUtils.length(code), SYMBOL);
    }

    /**
     * [隐藏所有] 固定6位*&lt;例子:******&gt;
     *
     * @param code 字符
     * @return 脱敏后数据
     */
    public static String allNoCount(final String code) {
        return SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL + SYMBOL;
    }
}
