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

import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author vincent
 * @version 1.0 2017/8/20 17:52
 */
public class DefaultRegister implements Register {

    /** 服务接口类 */
    @Setter
    protected Set<Class<?>> services;

    @Setter
    protected String group;

    protected Set<Class<?>> getInnerServices() {
        if (services == null) {
            services = new LinkedHashSet<>();
        }

        return services;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public void registe(Class<?> interfaceClass) {
        Set<Class<?>> svcSet = getInnerServices();
        svcSet.add(interfaceClass);
    }

    @Override
    public Set<Class<?>> getServices() {
        return this.services;
    }
}
