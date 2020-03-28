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

package cn.vincent.taste.spicy.helper.net;

import cn.vincent.taste.spicy.helper.character.CharsetKit;
import cn.vincent.taste.spicy.helper.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 链接相关工具
 *
 * @author vincent
 * @version 1.0 2017/8/20 04:14
 */
public class UrlBuilder {

    /**
     * 组装请求URL
     *
     * @param params    请求参数列表
     * @param actionURL http请求访问链接
     * @return 带参数链接
     */
    public static String buildRequestUrl(Map<String, Object> params, String actionURL) {
        return buildRequestUrl(params, null, actionURL);
    }

    /**
     * 组装请求URL
     *
     * @param params    请求参数列表
     * @param keyList   拼装顺序,拼装的顺序:如果为空，则按默认顺序组装Map中的所有参数;如果不为空则按List中的顺序拼装
     * @param actionURL http请求访问链接
     * @return 带参数链接
     */
    public static String buildRequestUrl(Map<String, Object> params, List<String> keyList, String actionURL) {
        String paramsLink = buildParamsLink(params, keyList);
        return buildRequestUrl(paramsLink, actionURL);
    }

    /**
     * 组装请求URL
     *
     * @param params    请求参数字符串，形如a=1&amp;b=2
     * @param actionURL http请求访问链接
     * @return 带参数链接
     */
    public static String buildRequestUrl(String params, String actionURL) {
        if (StringUtils.isBlank(params)) {
            return actionURL;
        }

        StringBuilder url = new StringBuilder(actionURL);

        // 不存在?号
        if (!actionURL.contains(Constant.MARK_QUESTION)) {
            url.append(Constant.MARK_QUESTION);
        } else if (!actionURL.endsWith(Constant.MARK_QUESTION)) {
            url.append(Constant.MARK_AND);
        }

        return url.append(params).toString();
    }

    /**
     * 将URL返回的字符串解析成Map
     *
     * @param urlParam 解析的字符串格式：a=a&b=b&c=c
     * @return map参数集
     */
    public static Map<String, String> getUrlParamToMap(String urlParam) {
        if (StringUtils.isBlank(urlParam)) {
            return null;
        }

        Map<String, String> map = new HashMap<>(16);
        String[] params = urlParam.split(Constant.MARK_AND);
        for (String param : params) {
            String[] values = param.split(Constant.MARK_EQUAL, 2);
            String value = values.length == 2 ? values[1] : "";
            try {
                map.put(values[0], URLDecoder.decode(value, CharsetKit.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Map转换为String请求串
     *
     * @param paramMap 参数列表
     * @return 按照a=1&amp;b=2的格式
     */
    public static String buildParamsLink(Map<String, Object> paramMap) {
        return buildParamsLink(paramMap, null);
    }

    /**
     * 组装URL
     *
     * @param params  参数Map
     * @param keyList 拼装顺序,拼装的顺序, 如果为空，则按默认顺序组装Map中的所有参数, 如果不为空则按List中的顺序拼装
     * @return 按照a=1&amp;b=2的格式
     */
    public static String buildParamsLink(Map<String, Object> params, List<String> keyList) {
        if (params == null || params.size() == 0) {
            return null;
        }

        List<String> keys;
        if (keyList == null || keyList.size() == 0) {
            keys = new ArrayList<>(params.keySet());
        } else {
            keys = keyList;
        }

        List<String> res = new ArrayList<>(keys.size());
        for (String key : keys) {
            Object value = params.get(key);

            try {
                res.add(key + Constant.MARK_EQUAL + (value == null ? "" : URLEncoder.encode(value.toString().trim(), CharsetKit.UTF_8)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.join(res, Constant.MARK_AND);
    }
}
