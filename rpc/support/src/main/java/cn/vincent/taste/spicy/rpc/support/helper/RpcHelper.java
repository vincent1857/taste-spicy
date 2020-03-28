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

package cn.vincent.taste.spicy.rpc.support.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * @author vincent
 * @version 1.0 2017/8/20 17:08
 */
public class RpcHelper {

    private static final String URL_SPLIT = "/";

    /**
     * 获取服务映射的URL
     *
     * @param prefix         前缀
     * @param suffix         后缀
     * @param svcName        服务名
     * @param interfaceClass 服务的接口类
     * @return 映射的URL
     */
    public static String getServiceUrl(String prefix, String suffix, String svcName, Class<?> interfaceClass) {
        StringBuilder sb = new StringBuilder();
        // 如果前缀为空则用/作为前缀
        if (StringUtils.isNotBlank(prefix)) {
            sb.append(prefix);
            if (!prefix.endsWith(URL_SPLIT)) {
                sb.append(URL_SPLIT);
            }
        } else {
            sb.append(URL_SPLIT);
        }
        // 如果service name为空则用接口名
        if (StringUtils.isBlank(svcName)) {
            svcName = ClassUtils.getShortName(interfaceClass.getName());

            // 首字母小写
            svcName = Introspector.decapitalize(svcName);
        }

        sb.append(svcName);
        // 如果设置了后缀则添加后缀
        if (StringUtils.isNotBlank(suffix) && !svcName.endsWith(suffix)) {
            sb.append(suffix);
        }

        return sb.toString();
    }
}
