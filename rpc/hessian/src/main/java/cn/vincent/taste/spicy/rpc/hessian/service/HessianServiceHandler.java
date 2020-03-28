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

import cn.vincent.taste.spicy.rpc.hessian.support.HessianHelper;
import cn.vincent.taste.spicy.rpc.support.RegisterHandler;
import cn.vincent.taste.spicy.rpc.support.helper.RpcHelper;
import cn.vincent.taste.spicy.support.annotation.rpc.HessianService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.ServletConfig;

/**
 * @author vincent
 * @version 1.0 2017/8/20 16:56
 */
public class HessianServiceHandler extends SimpleUrlHandlerMapping implements ServletConfigAware, BeanFactoryAware, RegisterHandler {

    protected BeanFactory beanFactory;
    protected ServletConfig servletConfig;

    @Getter
    @Setter
    private String urlPrefix = "/";
    @Getter
    @Setter
    private String serviceSuffix = ".hs";
    @Getter
    @Setter
    private boolean enable = true;
    @Getter
    @Setter
    private boolean force = false;
    @Getter
    @Setter
    private HessianServiceExporterCreator creator = new DefaultHessianServiceExporterCreator();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public void handle(Object serviceBean, String beanName, Class<?> interfaceClass) {
        // 检查是否可发布
        if (!this.isEnable()) {
            return;
        }

        Class<?> clazz = ClassUtils.getUserClass(serviceBean);

        String serviceName = null;

        // 获取服务的注解信息
        HessianService hessianServiceAnno = HessianHelper.getAnnotation(clazz, interfaceClass);
        if (hessianServiceAnno == null) {
            if (!this.force) {
                return;
            }
        } else {
            serviceName = hessianServiceAnno.serviceName();
        }

        // 生成服务映射的URL
        String url = RpcHelper.getServiceUrl(this.getUrlPrefix(), this.getServiceSuffix(), serviceName, interfaceClass);
        // 如果URL已被映射，则直接返回
        if (getHandlerMap().containsKey(url)) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("hessian service(%s) has been published,can't publish by %s", url, beanName));
            }

            return;
        }
        // 生成请求处理的Handler
        HessianServiceExporter serviceExporter = creator.create(serviceBean, beanName, interfaceClass);
        // 注册服务
        registerHandler(url, serviceExporter);

        if (logger.isInfoEnabled()) {
            logger.info(String.format("hessian service(%s) is published by [%s][%s]", url, beanName, clazz.getName()));
        }
    }
}
