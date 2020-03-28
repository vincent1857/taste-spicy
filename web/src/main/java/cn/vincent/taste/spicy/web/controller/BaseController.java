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

package cn.vincent.taste.spicy.web.controller;

import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vincent
 * @version 1.0 2019/9/29 14:48
 */
public class BaseController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    protected void redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("redirect error to " + url, e);
            throw new SystemException(SystemErrorCodes.INVALID_REDIRECT_URL, e);
        }
    }
}
