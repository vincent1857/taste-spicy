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

import cn.vincent.taste.spicy.rpc.hessian.context.HessianContext;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

import java.net.URL;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2017/8/20 15:32
 */
public class HeaderHessianProxy extends HessianProxy {

    protected HeaderHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }

    protected HeaderHessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
        super(url, factory, type);
    }

    @Override
    protected void addRequestHeaders(HessianConnection conn) {
        super.addRequestHeaders(conn);

        HessianContext context = HessianContext.getContext();

        // add Hessian Header
        Map<String, String> headerMap = context.getHeaders();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            conn.addHeader(entry.getKey(), entry.getValue());
        }
    }
}
