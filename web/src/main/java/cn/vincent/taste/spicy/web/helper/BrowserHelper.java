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
package cn.vincent.taste.spicy.web.helper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器工具类
 *
 * @author Vincent
 * @version 1.0 2017/03/08 00:06
 */
@Slf4j
public class BrowserHelper {

    /**
     * 获取浏览器版本信息
     *
     * @param request 请求对象
     * @return agent
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }
}