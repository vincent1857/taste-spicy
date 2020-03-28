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

package cn.vincent.taste.spicy.rpc.dubbo.filter;

import cn.vincent.taste.spicy.helper.basic.LogHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @author vincent
 * @version 1.0 2017/8/20 17:01
 */
@Activate(group = CommonConstants.CONSUMER, order = Integer.MIN_VALUE)
public class LogProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        String requestId = context.getAttachment(LogHelper.KEY_REQUEST_ID);

        if (StringUtils.isBlank(requestId)) {
            requestId = RandomStringUtils.randomAlphanumeric(8);
            context.setAttachment(LogHelper.KEY_REQUEST_ID, requestId);
        }

        MDC.put(LogHelper.KEY_REQUEST_ID, requestId);
        MDC.put(LogHelper.KEY_REQUEST_IP, RpcContext.getContext().getRemoteHost());

        try {
            return invoker.invoke(invocation);
        } finally {
            RpcContext.getContext().clearAttachments();

            if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
                MDC.remove(LogHelper.KEY_REQUEST_ID);
            }

            if (MDC.get(LogHelper.KEY_REQUEST_IP) != null) {
                MDC.remove(LogHelper.KEY_REQUEST_IP);
            }
        }
    }
}