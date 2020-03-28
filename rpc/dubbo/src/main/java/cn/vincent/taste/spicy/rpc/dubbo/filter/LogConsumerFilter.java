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
import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * 客户端调用拦截器
 *
 * @author vincent
 * @version 1.0 2017/8/20 16:20
 */
@Activate(group = CommonConstants.CONSUMER, order = Integer.MIN_VALUE)
public class LogConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (MDC.get(LogHelper.KEY_REQUEST_ID) == null) {
            MDC.put(LogHelper.KEY_REQUEST_ID, RandomStringUtils.randomAlphanumeric(8));
        }

        RpcContext.getContext().setAttachment(LogHelper.KEY_REQUEST_ID, MDC.get(LogHelper.KEY_REQUEST_ID));
        try {
            return invoker.invoke(invocation);
        } catch (RpcException e) {
            throw new SystemException(SystemErrorCodes.RPC_INNER_SERVER_FAIL, e);
        } finally {
            RpcContext.getContext().clearAttachments();
        }
    }
}