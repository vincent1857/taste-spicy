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
import cn.vincent.taste.spicy.web.constant.WebHeaderConstant;
import cn.vincent.taste.spicy.web.helper.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:11
 */
@Slf4j
public class InnerCoreHandlerInterceptor extends CoreHandlerInterceptor {

    @Override
    protected void initMdc(HttpServletRequest request, HttpServletResponse response) {
        String requestId = request.getHeader(LogHelper.KEY_HEADER_REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            requestId = RandomStringUtils.randomAlphanumeric(8);
        }
        MDC.put(LogHelper.KEY_REQUEST_ID, requestId);
        MDC.put(LogHelper.KEY_REQUEST_IP, RequestHelper.getRequestIps(request));
        String machine = ProjectConfiguration.getMachine();
        if (StringUtils.isNotBlank(machine)) {
            MDC.put(LogHelper.KEY_REQUEST_MACHINE, machine);
        }
    }

    @Override
    protected void printLog(HttpServletRequest request, HttpServletResponse response) {
        super.printLog(request, response);

        String message;
        String serverType = request.getHeader(WebHeaderConstant.NAME_SERVER_TYPE);
        if (WebHeaderConstant.NAME_SERVER_TYPE_VALUE.equalsIgnoreCase(serverType)) {
            String instId = request.getHeader(WebHeaderConstant.NAME_GATEWAY_INST);
            message = LogHelper.getLogStr("request server type " + serverType + ", request inst id " + instId);
        } else {
            message = LogHelper.getLogStr("request server type " + serverType);
        }

        if (ProjectConfiguration.isLogPrint()) {
            log.info(message);
        } else {
            log.debug(message);
        }
    }

    @Override
    protected void responseHeader(HttpServletRequest request, HttpServletResponse response) {

    }
}