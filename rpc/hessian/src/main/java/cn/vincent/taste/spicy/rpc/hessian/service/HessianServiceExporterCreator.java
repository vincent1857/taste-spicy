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

import org.springframework.remoting.caucho.HessianServiceExporter;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:09
 */
public interface HessianServiceExporterCreator {

    /**
     * 创建hessian服务
     *
     * @param serviceBean    实现类
     * @param beanName       bean名称
     * @param interfaceClass 接口
     * @return hessian服务
     */
    HessianServiceExporter create(Object serviceBean, String beanName, Class<?> interfaceClass);
}
