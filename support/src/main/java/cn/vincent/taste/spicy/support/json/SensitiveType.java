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

package cn.vincent.taste.spicy.support.json;

/**
 * 敏感类型
 *
 * @author vincent
 * @version 1.0 2017/8/20 16:59
 */
public enum SensitiveType {

    /** 默认，全部替换* */
    DEFAULT,
    /** 中文名, 隐藏中间 */
    CHINESE_NAME_NOMIDDLE,
    /** 中文名, 只显示姓 */
    CHINESE_NAME_FIRST,
    /** 身份证号 */
    ID_CARD,
    /** 座机号 */
    FIXED_PHONE,
    /** 手机号 */
    MOBILE_PHONE,
    /** 地址 */
    ADDRESS,
    /** 电子邮件 */
    EMAIL,
    /** 银行卡 */
    BANK_CARD,
    /** 密码, 替换成6个* */
    PASSWORD,
    /** 全量隐藏 */
    ALL;

    public static final String DEFAULT_STRING = "DEFAULT";
    public static final String CHINESE_NAME_NOMIDDLE_STRING = "CHINESE_NAME_NOMIDDLE";
    public static final String CHINESE_NAME_FIRST_STRING = "CHINESE_NAME_FIRST";
    public static final String ID_CARD_STRING = "ID_CARD";
    public static final String FIXED_PHONE_STRING = "FIXED_PHONE";
    public static final String MOBILE_PHONE_STRING = "MOBILE_PHONE";
    public static final String ADDRESS_STRING = "ADDRESS";
    public static final String EMAIL_STRING = "EMAIL";
    public static final String BANK_CARD_STRING = "BANK_CARD";
    public static final String PASSWORD_STRING = "PASSWORD";
    public static final String ALL_STRING = "ALL";
}
