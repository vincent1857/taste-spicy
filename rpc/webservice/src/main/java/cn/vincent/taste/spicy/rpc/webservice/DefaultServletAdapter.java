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

package cn.vincent.taste.spicy.rpc.webservice;

import cn.vincent.taste.spicy.helper.exception.BizException;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2017/8/20 19:04
 */
@Slf4j
public class DefaultServletAdapter implements Controller {

    private Servlet controller;

    @Getter
    @Setter
    private List<WebServiceInterceptor> interceptors;

    public DefaultServletAdapter(Servlet controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 已经处理的
        List<WebServiceInterceptor> handledInterceptors = new ArrayList<>();
        Exception ex = null;
        try {
            // 执行拦截器预处理
            boolean flag = true;
            if(this.interceptors != null && this.interceptors.size() > 0){
                for (WebServiceInterceptor interceptor : this.interceptors) {
                    if (!interceptor.preHandle(request, response, this.controller)){
                        flag = false;
                        break;
                    }
                    handledInterceptors.add(interceptor);
                }
            }
            if (flag) {
                controller.service(request, response);

                if (handledInterceptors.size() > 0) {
                    for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                        handledInterceptors.get(i).postHandle(request, response, this.controller);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            ex = e;
            if(e instanceof BizException){
                throw e;
            }

            throw new SystemException(e);
        } finally {
            if (handledInterceptors.size() > 0) {
                for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                    handledInterceptors.get(i).afterCompletion(request, response, this.controller, ex);
                }
            }
        }
    }
}
