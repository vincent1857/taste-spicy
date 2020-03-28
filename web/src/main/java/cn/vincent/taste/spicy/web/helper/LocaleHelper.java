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

import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * 语言环境相关
 *
 * @author Vincent
 * @version 1.0 2017/03/08 00:08
 */
public class LocaleHelper {

    /**
     * 获取当前的语言环境
     *
     * @return 本地化对象
     */
    public static Locale getCurrentLocale() {
        HttpServletRequest request = RequestHelper.getRequest();
        if (null != request) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (null != localeResolver) {
                return localeResolver.resolveLocale(request);
            }
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    /**
     * 获取国际化信息
     *
     * @param message        原始消息
     * @param defaultMessage 默认消息
     * @param locale         本地化信息
     * @param args           参数列表
     * @return 国际化信息
     */
    public static String getMessage(String message, String defaultMessage, Locale locale, Object[] args) {
        HttpServletRequest request = RequestHelper.getRequest();
        if (null != request && null != locale) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            return applicationContext.getMessage(message, args, defaultMessage, locale);
        }
        return MessageFormat.format(defaultMessage, args);
    }

    /**
     * 获取国际化信息
     *
     * @param resolvable 解析对象
     * @param locale     本地化信息
     * @return 国际化信息
     */
    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        HttpServletRequest request = RequestHelper.getRequest();
        if (null != request && null != locale) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            return applicationContext.getMessage(resolvable, locale);
        }
        return resolvable.getDefaultMessage();
    }

    /**
     * 获取国际化信息
     *
     * @param resolvable 解析对象
     * @return 国际化信息
     */
    public static String getMessage(MessageSourceResolvable resolvable) {
        Locale locale = getCurrentLocale();
        if (null == locale) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        return getMessage(resolvable, locale);
    }

    /**
     * 获取国际化信息
     *
     * @param message        原始消息
     * @param defaultMessage 默认消息
     * @param args           参数列表
     * @return 国际化信息
     */
    public static String getMessage(String message, String defaultMessage, Object[] args) {
        Locale locale = getCurrentLocale();
        if (null != locale) {
            return getMessage(message, defaultMessage, locale, args);
        }
        return MessageFormat.format(defaultMessage, args);
    }

    /**
     * 获取国际化信息
     *
     * @param message 原始消息
     * @param args    参数列表
     * @return 国际化信息
     */
    public static String getMessage(String message, Object[] args) {
        return getMessage(message, message, args);
    }

    /**
     * 获取国际化信息
     *
     * @param message 原始消息
     * @return 国际化信息
     */
    public static String getMessage(String message) {
        return getMessage(message, null);
    }
}