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

package cn.vincent.taste.spicy.rpc.support;

/**
 * @author vincent
 * @version 1.0 2017/8/20 18:00
 */
public interface RegisterHandler {

    /**
     * 根据bean和接口类型注册服务
     *
     * @param serviceBean    bean对象
     * @param beanName       bean名
     * @param interfaceClass 接口class
     */
    void handle(Object serviceBean, String beanName, Class<?> interfaceClass);
}
