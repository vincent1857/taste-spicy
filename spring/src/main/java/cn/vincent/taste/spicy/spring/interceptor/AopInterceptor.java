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

package cn.vincent.taste.spicy.spring.interceptor;

import org.springframework.core.Ordered;

/**
 * 拦截器
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:41
 */
public interface AopInterceptor<T> extends Ordered {

    /**
     * 程序执行之前，如果返回false，那么不会执行下面的两个方法：afterCompletion和afterCompletion
     *
     * @param data 对象
     * @return true/false
     */
    boolean preHandle(T data);

    /**
     * 程序执行之后
     *
     * @param data 对象
     */
    void postHandle(T data);

    /**
     * 程序抛出异常的时候执行，前提条件是preHandle方法返回true
     *
     * @param data 对象
     * @param ex 异常
     */
    void afterCompletion(T data, Exception ex);
}
