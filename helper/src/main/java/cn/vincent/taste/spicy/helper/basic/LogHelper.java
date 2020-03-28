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

package cn.vincent.taste.spicy.helper.basic;

import org.apache.commons.lang3.StringUtils;

/**
 * 日志组装工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 04:13
 */
public class LogHelper {

    /** 多条信息的分隔符 */
    public static final String LOG_SPLIT = ";";
    /** 记录上下文的标识码 */
    public static final String KEY_HEADER_REQUEST_ID = "v_requestid";
    /** 记录上下文的标识码 */
    public static final String KEY_REQUEST_ID = "requestid";
    /** 记录上下文的请求ip */
    public static final String KEY_REQUEST_IP = "requestip";
    /** 记录上下文的机器编号 */
    public static final String KEY_REQUEST_MACHINE = "requestmachine";
    /** 标记功能分隔符 */
    public static final String KEY_REQUEST_SIGN = ":>";

    public static String getLogStr(Object... params) {
        return getSplitLogStr(null, params);
    }

    public static String getSignLogStr(String sign, Object... params) {
        if(StringUtils.isNotBlank(sign)){
            return sign + " " + KEY_REQUEST_SIGN + " " + getSplitLogStr(null, params);
        } else {
            return getLogStr(params);
        }
    }

    public static String getSplitLogStr(String split, Object... params) {
        return StringUtils.join(params, split == null ? LOG_SPLIT : split);
    }
}
