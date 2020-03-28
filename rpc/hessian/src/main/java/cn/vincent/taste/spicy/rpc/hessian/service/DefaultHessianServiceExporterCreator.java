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

import lombok.Setter;
import org.springframework.remoting.caucho.HessianServiceExporter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:15
 */
public class DefaultHessianServiceExporterCreator implements HessianServiceExporterCreator {

    @Setter
    private List<HessianServiceInterceptor> serviceInterceptors;

    @Override
    public HessianServiceExporter create(Object serviceBean, String beanName, Class<?> interfaceClass) {

        InterceptHessianServiceExporter serviceExporter = new InterceptHessianServiceExporter();
        serviceExporter.setService(serviceBean);
        serviceExporter.setServiceInterface(interfaceClass);
        serviceExporter.setServiceInterceptors(this.getServiceInterceptors());
        serviceExporter.afterPropertiesSet();

        return serviceExporter;
    }

    public List<HessianServiceInterceptor> getServiceInterceptors() {
        if (this.serviceInterceptors == null) {
            this.serviceInterceptors = new ArrayList<>();
            this.serviceInterceptors.add(new LogHessianServiceInterceptor());
        }
        return this.serviceInterceptors;
    }
}
