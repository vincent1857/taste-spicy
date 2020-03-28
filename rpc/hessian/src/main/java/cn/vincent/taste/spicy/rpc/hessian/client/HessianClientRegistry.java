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

package cn.vincent.taste.spicy.rpc.hessian.client;

import cn.vincent.taste.spicy.rpc.hessian.support.HessianHelper;
import cn.vincent.taste.spicy.rpc.support.Publisher;
import cn.vincent.taste.spicy.rpc.support.Register;
import cn.vincent.taste.spicy.rpc.support.helper.RpcHelper;
import cn.vincent.taste.spicy.spring.configuration.AppConfiguration;
import cn.vincent.taste.spicy.support.annotation.rpc.HessianService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.util.*;

/**
 * @author vincent
 * @version 1.0 2017/8/20 04:42
 */
@Slf4j
public class HessianClientRegistry implements Publisher, ApplicationListener<ContextRefreshedEvent> {

    @Getter
    private ApplicationContext applicationContext;
    @Getter
    @Setter
    private List<String> registerGroups = Collections.singletonList("all");
    @Getter
    @Setter
    private List<HessianClientInterceptor> clientInterceptors;
    /** 统一前缀 */
    @Getter
    @Setter
    private Map<String, String> urlPreffixes;
    /** 服务后缀 */
    @Getter
    @Setter
    private String serviceSuffix = ".hs";
    @Getter
    @Setter
    private HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();

    @Override
    public boolean canPublish(Register register) {
        return this.getRegisterGroups().contains("all") || (register.getGroup() != null && this.getRegisterGroups().contains(register.getGroup().toLowerCase()));
    }

    @Override
    public void publish() {
        if (this.urlPreffixes == null) {
            log.warn("创建hessian客户端失败，未获取到hessian client urlPrefix");
            this.urlPreffixes = new HashMap<>(16);
        }

        // 获取所有服务注册器
        Collection<Register> registers = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), Register.class).values();

        // 对每个register服务接口列表，对接口发布服务
        for (Register register : registers) {
            if (this.canPublish(register)) {
                String urlPreffix = this.urlPreffixes.get(register.getGroup());
                if (StringUtils.isBlank(urlPreffix)) {
                    log.warn("信息提示:未获取到hessian client " + register.getGroup() + " urlPrefix, 即将从配置文件获取");
                    String configKey = "hessian." + register.getGroup() + ".server";
                    urlPreffix = AppConfiguration.getInstance().get(configKey);
                    if (StringUtils.isBlank(urlPreffix)) {
                        log.warn("创建hessian客户端失败，未获取到hessian client " + register.getGroup() + " urlPrefix");
                        continue;
                    }
                }
                registeClientBean(register, urlPreffix);
            }
        }
    }

    protected void registeClientBean(Register register, String urlPreffix) {
        if (register.getServices() == null || register.getServices().size() == 0) {
            return;
        }

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (Class<?> interfaceClass : register.getServices()) {
            // 获取服务的注解信息
            HessianService hessianServiceAnno = HessianHelper.getAnnotation(interfaceClass, interfaceClass);
            String serviceName = null;
            if (hessianServiceAnno != null) {
                serviceName = hessianServiceAnno.serviceName();
            }

            if (StringUtils.isBlank(serviceName)) {
                serviceName = Introspector.decapitalize(ClassUtils.getShortName(interfaceClass.getName()));
            }

            if (!this.applicationContext.containsBean(serviceName)) {
                String serviceUrl = RpcHelper.getServiceUrl(urlPreffix, this.serviceSuffix, serviceName, interfaceClass);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(InterceptHessianProxyFactoryBean.class);
                builder.addPropertyValue("clientInterceptors", this.getClientInterceptors());
                builder.addPropertyValue("proxyFactory", this.getHessianProxyFactory());
                builder.addPropertyValue("serviceUrl", serviceUrl);
                builder.addPropertyValue("serviceInterface", interfaceClass);
                beanFactory.registerBeanDefinition(serviceName, builder.getBeanDefinition());
                log.info("registe bean name {} for {}, this url is {}", serviceName, interfaceClass.getName(), serviceUrl);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();

        // 发布服务
        this.publish();
    }
}
