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

package cn.vincent.taste.spicy.web.interceptor;

import cn.vincent.taste.spicy.helper.basic.LogHelper;
import cn.vincent.taste.spicy.helper.configuration.ProjectConfiguration;
import cn.vincent.taste.spicy.helper.context.ContextHolder;
import cn.vincent.taste.spicy.web.helper.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:11
 */
@Slf4j
public class CoreHandlerInterceptor extends HandlerInterceptorAdapter {

    protected void initMdc(HttpServletRequest request, HttpServletResponse response) {
        MDC.put(LogHelper.KEY_REQUEST_ID, RandomStringUtils.randomAlphanumeric(8));
        MDC.put(LogHelper.KEY_REQUEST_IP, RequestHelper.getRequestIps(request));
        String machine = ProjectConfiguration.getMachine();
        if (StringUtils.isNotBlank(machine)) {
            MDC.put(LogHelper.KEY_REQUEST_MACHINE, machine);
        }
    }

    protected void printLog(HttpServletRequest request, HttpServletResponse response) {
        if (ProjectConfiguration.isLogPrint()) {
            log.info(LogHelper.getLogStr("request url:" + RequestHelper.getRequestLog(request)));
            log.info(LogHelper.getLogStr("request referer:" + request.getHeader("referer")));
        } else {
            log.debug(LogHelper.getLogStr("request url:" + RequestHelper.getRequestLog(request)));
            log.debug(LogHelper.getLogStr("request referer:" + request.getHeader("referer")));
        }
    }

    protected void responseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(LogHelper.KEY_HEADER_REQUEST_ID, MDC.get(LogHelper.KEY_REQUEST_ID));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.initMdc(request, response);
        this.printLog(request, response);
        this.responseHeader(request, response);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        ContextHolder.release();
        if (MDC.get(LogHelper.KEY_REQUEST_MACHINE) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_MACHINE);
        }
        if (MDC.get(LogHelper.KEY_REQUEST_IP) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_IP);
        }
        if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_ID);
        }
    }
}