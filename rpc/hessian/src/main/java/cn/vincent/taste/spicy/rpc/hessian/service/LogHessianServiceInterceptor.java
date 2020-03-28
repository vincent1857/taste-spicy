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

package cn.vincent.taste.spicy.rpc.hessian.service;

import cn.vincent.taste.spicy.helper.basic.LogHelper;
import cn.vincent.taste.spicy.helper.constant.Constant;
import cn.vincent.taste.spicy.rpc.hessian.context.HessianContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author vincent
 * @version 1.0 2017/8/20 03:51
 */
@Slf4j
public class LogHessianServiceInterceptor extends HessianServiceInterceptorAdapter {

    private static final String UNKOWN = "unknown";
    private static final String LOCLAIP = "127.0.0.1";

    private static String getRequestIps(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCLAIP.equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("IpHelper error." + e.toString(), e);
                }
            }
        }
        return ip;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HessianContext context = HessianContext.getContext();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            context.addHeader(name, value);
        }

        if (MDC.get(LogHelper.KEY_REQUEST_ID) == null) {
            String requestId = context.getHeader(LogHelper.KEY_REQUEST_ID);
            if (StringUtils.isBlank(requestId)) {
                requestId = RandomStringUtils.randomAlphanumeric(8);
                context.addHeader(LogHelper.KEY_REQUEST_ID, requestId);
            }
            MDC.put(LogHelper.KEY_REQUEST_ID, requestId);
        }
        MDC.put(LogHelper.KEY_REQUEST_IP, getRequestIps(request));
        log.debug(LogHelper.getLogStr("[" + request.getRequestURL() + (StringUtils.isBlank(request.getQueryString()) ? "" : Constant.MARK_QUESTION + request.getQueryString()) + "]"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HessianContext.removeContext();

        if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_ID);
        }

        if (MDC.get(LogHelper.KEY_REQUEST_IP) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_IP);
        }
    }
}
