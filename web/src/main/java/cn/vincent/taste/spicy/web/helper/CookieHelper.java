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

import cn.vincent.taste.spicy.helper.character.CharsetKit;
import cn.vincent.taste.spicy.helper.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * cookie工具
 *
 * @author Vincent
 * @version 1.0 2017/03/08 00:06
 */
public class CookieHelper {

    /**
     * 浏览器关闭时自动删除
     */
    public final static int CLEAR_BROWSER_IS_CLOSED = -1;
    /**
     * 立即删除
     */
    public final static int CLEAR_IMMEDIATELY_REMOVE = 0;

    /**
     * 清除指定doamin的所有Cookie
     *
     * @param request  请求
     * @param response 响应
     */
    public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cooky : cookies) {
            clearCookie(response, cooky.getName(), cooky.getPath(), cooky.getDomain(), cooky.isHttpOnly(), cooky.getSecure());
        }
    }

    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie ck = findCookieByName(request, cookieName);
        if (ck != null) {
            clearCookie(response, cookieName, ck.getPath(), ck.getDomain(), ck.isHttpOnly(), ck.getSecure());
        }
    }

    public static void clearCookie(HttpServletResponse response, String name, String path, String domain) {
        setCookiesValue(response, name, Constant.EMPTY, CLEAR_IMMEDIATELY_REMOVE, path, domain, true, false);
    }

    public static void clearCookie(HttpServletResponse response, String name, String path, String domain, boolean httpOnly, boolean secure) {
        setCookiesValue(response, name, Constant.EMPTY, CLEAR_IMMEDIATELY_REMOVE, path, domain, httpOnly, secure);
    }

    public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals(cookieName)) {
                return cooky;
            }
        }
        return null;
    }

    /**
     * 获取cookies的对应值
     *
     * @param request 请求
     * @param name    名称
     * @return 值
     */
    public static String getCookiesValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                }
            }
        }

        if (StringUtils.isNotBlank(value)) {
            try {
                return URLDecoder.decode(value, CharsetKit.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return value;
            }
        }
        return null;
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value) {
        setCookiesValue(response, name, value, null, null);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path) {
        setCookiesValue(response, name, value, expiry, path, null);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     * @param domain   域
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path, String domain) {
        setCookiesValue(response, name, value, expiry, path, domain, true, false);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     * @param domain   域
     * @param httpOnly true/false
     * @param secure   true/false
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path, String domain, boolean httpOnly, boolean secure) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        String code = null;
        if (StringUtils.isNotBlank(value)) {
            try {
                code = URLEncoder.encode(value, CharsetKit.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                code = value;
            }
        }
        Cookie cookie = new Cookie(name, code);
        if (null != expiry) {
            cookie.setMaxAge(expiry);
        }
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        } else {
            cookie.setPath(Constant.MARK_SLASH);
        }
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }

        if (httpOnly) {
            cookie.setHttpOnly(true);
        }
        if (secure) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
    }
}