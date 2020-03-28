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

package cn.vincent.taste.spicy.helper.attack;

import cn.vincent.taste.spicy.helper.attack.istrip.SqlInjectionIstrip;
import cn.vincent.taste.spicy.helper.attack.istrip.XssIstrip;

/**
 * Web防火墙工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 23:43
 */
public class WafHelper {

    private WafHelper() {

    }

    /**
     * 过滤xss脚本攻击
     *
     * @param value 原始字符串
     * @return 过滤后字符串
     */
    public static String stripXss(String value) {
        if (value == null) {
            return null;
        }

        return new XssIstrip().strip(value);
    }

    /**
     * 防止sql注入
     *
     * @param value 原始字符串
     * @return 过滤后字符串
     */
    public static String stripSqlInjection(String value) {
        if (value == null) {
            return null;
        }

        return new SqlInjectionIstrip().strip(value);
    }

    /**
     * 过滤SQL/XSS注入内容
     *
     * @param value 原始字符串
     * @return 过滤后字符串
     */
    public static String stripSqlXss(String value) {
        if (value == null) {
            return null;
        }

        return stripXss(stripSqlInjection(value));
    }
}
