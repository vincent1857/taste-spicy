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

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author vincent
 * @version 1.0 2017/8/20 17:57
 */
public class RegisterPublisher implements Publisher, ApplicationListener<ContextRefreshedEvent> {

    @Getter
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    private List<String> registerGroups = Collections.singletonList("all");

    /**
     * 服务的处理列表
     */
    @Getter
    @Setter
    private List<RegisterHandler> handlers = new LinkedList<>();

    @Override
    public boolean canPublish(Register register) {
        return this.getRegisterGroups().contains("all") || (register.getGroup() != null && this.getRegisterGroups().contains(register.getGroup().toLowerCase()));
    }

    @Override
    public void publish() {
        // 获取所有服务注册器
        Collection<Register> registers = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), Register.class).values();

        // 对每个register服务接口列表，对接口发布服务
        for (Register register : registers) {
            if (this.canPublish(register)) {
                publishRegister(register);
            }
        }
    }

    /**
     * 对每个register服务接口列表，对接口发布服务
     *
     * @param register 服务注册器
     */
    protected void publishRegister(Register register) {
        // 获取注册的接口类
        Set<Class<?>> serviceInterfaceClasses = register.getServices();
        if (serviceInterfaceClasses == null || serviceInterfaceClasses.size() == 0) {
            return;
        }

        for (Class<?> serviceInterfaceClass : serviceInterfaceClasses) {
            publishService(serviceInterfaceClass);
        }
    }

    /**
     * 对接口发布服务
     *
     * @param serviceInterfaceClass 接口class
     */
    protected void publishService(Class<?> serviceInterfaceClass) {
        Map<String, ?> registerMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), serviceInterfaceClass);

        if (registerMap.size() == 0) {
            return;
        }

        for (Map.Entry<String, ?> entry : registerMap.entrySet()) {
            for (RegisterHandler handler : handlers) {
                handler.handle(entry.getValue(), entry.getKey(), serviceInterfaceClass);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();

        // 发布服务
        this.publish();
    }

    public void setRegisterGroup(String registerGroup) {
        String[] registerGroups = StringUtils.tokenizeToStringArray(registerGroup.toLowerCase(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        if (registerGroups != null) {
            this.registerGroups = Arrays.asList(registerGroups);
        }
    }
}
