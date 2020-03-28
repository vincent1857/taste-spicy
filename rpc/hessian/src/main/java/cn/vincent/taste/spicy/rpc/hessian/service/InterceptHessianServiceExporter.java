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

import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.exception.BizException;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2017/8/20 15:47
 */
public class InterceptHessianServiceExporter extends HessianServiceExporter {

    @Getter
    @Setter
    private List<HessianServiceInterceptor> serviceInterceptors;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // 已经处理的
        List<HessianServiceInterceptor> handledInterceptors = new ArrayList<>();
        Object handler = this.getService();

        Exception ex = null;
        try {
            // 执行拦截器预处理
            boolean flag = true;
            if(this.serviceInterceptors != null && this.serviceInterceptors.size() > 0){
                for (HessianServiceInterceptor serviceInterceptor : this.serviceInterceptors) {
                    if (!serviceInterceptor.preHandle(request, response, handler)){
                        flag = false;
                        break;
                    }
                    handledInterceptors.add(serviceInterceptor);
                }
            }
            if (flag) {
                super.handleRequest(request, response);

                if (handledInterceptors.size() > 0) {
                    for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                        handledInterceptors.get(i).postHandle(request, response, handler);
                    }
                }
            }
        } catch (Exception e) {
            ex = e;
            if(e instanceof HttpRequestMethodNotSupportedException){
                throw (HttpRequestMethodNotSupportedException) e;
            } else if(e instanceof NestedServletException){
                throw (NestedServletException) e;
            } else if(e instanceof BizException){
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new SystemException(e);
                }
            } else {
                throw new SystemException(SystemErrorCodes.RPC_INNER_SERVER_EXCEPTION, e);
            }
        } finally {
            if (handledInterceptors.size() > 0) {
                for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                    handledInterceptors.get(i).afterCompletion(request, response, handler, ex);
                }
            }
        }
    }
}