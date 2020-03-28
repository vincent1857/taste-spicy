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

package cn.vincent.taste.spicy.rpc.hessian.support;

import cn.vincent.taste.spicy.support.annotation.rpc.HessianService;
import org.springframework.stereotype.Service;

/**
 * @author vincent
 * @version 1.0 2017/8/20 23:07
 */
public class HessianHelper {

    /**
     * 获取服务的注解信息
     *
     * @param beanClazz 服务bean的类
     * @return 返回服务的注解和接口类
     */
    public static HessianService getAnnotation(Class<?> beanClazz, Class<?> interfaceClass) {
        // 查看类上是否有Service注解
        if (!beanClazz.isAnnotationPresent(Service.class)) {
            return null;
        }

        // 则查看类实现的接口上是否有注解
        Class<?>[] interfaces = beanClazz.getInterfaces();
        if (interfaces == null) {
            return null;
        }

        for (Class<?> interfaceClazz : interfaces) {
            if (interfaceClazz.equals(interfaceClass) && interfaceClazz.isAnnotationPresent(HessianService.class)) {
                return interfaceClass.getAnnotation(HessianService.class);
            }
        }

        return null;
    }
}
