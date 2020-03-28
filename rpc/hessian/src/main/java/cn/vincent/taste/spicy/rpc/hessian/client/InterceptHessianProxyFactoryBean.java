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

package cn.vincent.taste.spicy.rpc.hessian.client;

import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.exception.BizException;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2017/8/20 03:40
 */
public class InterceptHessianProxyFactoryBean extends HessianProxyFactoryBean {

    @Setter
    private List<HessianClientInterceptor> clientInterceptors;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 已经处理的
        List<HessianClientInterceptor> handledInterceptors = new ArrayList<>();
        Exception ex = null;
        Object result = null;
        try {
            // 执行拦截器预处理
            boolean flag = true;
            if (this.getClientInterceptors() != null && this.getClientInterceptors().size() > 0) {
                for (HessianClientInterceptor clientInterceptor : this.getClientInterceptors()) {
                    if (!clientInterceptor.preHandle(invocation)) {
                        flag = false;
                        break;
                    }
                    handledInterceptors.add(clientInterceptor);
                }
            }
            if (flag) {
                result = super.invoke(invocation);

                if (handledInterceptors.size() > 0) {
                    for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                        handledInterceptors.get(i).postHandle(invocation);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            ex = e;
            if (e instanceof BizException) {
                throw e;
            } else if (e instanceof RemoteConnectFailureException) {
                throw new SystemException(SystemErrorCodes.RPC_INNER_SERVER_FAIL, e);
            }
            throw new SystemException(SystemErrorCodes.RPC_INNER_SERVER_EXCEPTION, e);
        } finally {
            if (handledInterceptors.size() > 0) {
                for (int i = handledInterceptors.size() - 1; i >= 0; i--) {
                    handledInterceptors.get(i).afterCompletion(invocation, ex);
                }
            }
        }
    }

    public List<HessianClientInterceptor> getClientInterceptors(){
        if (this.clientInterceptors == null) {
            this.clientInterceptors = new ArrayList<>();
            this.clientInterceptors.add(new LogHessianClientInterceptor());
        }
        return this.clientInterceptors;
    }
}
