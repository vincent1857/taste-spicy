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

package cn.vincent.taste.spicy.rpc.webservice;

import cn.vincent.taste.spicy.rpc.support.RegisterHandler;
import cn.vincent.taste.spicy.rpc.support.helper.RpcHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.util.ClassHelper;
import org.apache.cxf.databinding.DataBinding;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.lang.reflect.Method;

/**
 * webService服务
 * <p>
 * 该类用于web service的接口映射处理，其实现是copy JaxWsWebServicePublisherBeanPostProcessor源码，重新写postProcessAfterInitialization函数中服务注册部分代码，由于JaxWsWebServicePublisherBeanPostProcessor定义的变量都是Private且无接口获取，无法用继承方式，因此copy代码重写
 *
 * @author vincent
 * @version 1.0 2017/8/20 05:28
 */
public class WebServiceHandler extends AbstractUrlHandlerMapping implements ServletConfigAware, BeanFactoryAware, RegisterHandler {

    private static final String CXF_SERVLET_CLASS_NAME = "org.apache.cxf.transport.servlet.CXFServlet";

    @Setter
    @Getter
    private String urlPrefix = "/";
    @Setter
    @Getter
    private String serviceSuffix = ".ws";
    @Setter
    @Getter
    private boolean enable = true;
    @Getter
    private BeanFactory beanFactory;
    @Setter
    @Getter
    private String prototypeServerFactoryBeanName;
    @Setter
    @Getter
    private String prototypeDataBindingBeanName;
    @Setter
    @Getter
    private WebServiceServletAdapterCreator creator;

    private Class<?> servletClass;
    private Method servletGetBusMethod;
    private Servlet shadowCxfServlet;

    @Resource(name = "cxf")
    protected Bus bus;

    /**
     * for testing 是否是自定义服务
     */
    @Setter
    @Getter
    private boolean customizedServerFactory;
    @Setter
    @Getter
    private boolean customizedDataBinding;

    public WebServiceHandler() throws SecurityException, NoSuchMethodException, ClassNotFoundException {
        try {
            this.servletClass = ClassLoaderUtils.loadClass(CXF_SERVLET_CLASS_NAME, getClass());
        } catch (ClassNotFoundException e) {
            logger.error("serverlet class missing " + CXF_SERVLET_CLASS_NAME);
            throw e;
        }
        this.servletGetBusMethod = this.servletClass.getMethod("getBus");
    }

    private Bus getServletBus() {
        if (bus != null) {
            return bus;
        }

        try {
            if (shadowCxfServlet == null) {
                // no servlet going on. Just launch.
                bus = BusFactory.getDefaultBus(true);
            } else {
                bus = (Bus) servletGetBusMethod.invoke(shadowCxfServlet);
            }
        } catch (Exception e) {
            // CXF internally inconsistent?
            throw new RuntimeException(e);
        }

        return bus;
    }

    /**
     * 获取服务的注解信息
     *
     * @param beanClazz      class对象
     * @param interfaceClass 接口class
     * @return 返回注解和接口
     */
    protected WebService getAnnotation(Class<?> beanClazz, Class<?> interfaceClass) {
        // 查看类上是否有service注解
        if (!beanClazz.isAnnotationPresent(Service.class)) {
            return null;
        }

        // 查看类实现的接口上是否有注解
        Class<?>[] interfaceList = beanClazz.getInterfaces();
        if (interfaceList == null) {
            return null;
        }

        for (Class<?> inClass : interfaceList) {
            if (inClass.equals(interfaceClass) && inClass.isAnnotationPresent(WebService.class)) {
                return inClass.getAnnotation(WebService.class);
            }
        }

        return null;
    }

    private void createAndPublishEndpoint(String url, Object implementor) {
        // 检查服务是否已发布
        if (checkUrlPublish(url)) {
            return;
        }

        ServerFactoryBean serverFactory;
        if (prototypeServerFactoryBeanName != null) {
            if (!beanFactory.isPrototype(prototypeServerFactoryBeanName)) {
                throw new IllegalArgumentException("prototypeServerFactoryBeanName must indicate a scope='prototype' bean");
            }
            serverFactory = beanFactory.getBean(prototypeServerFactoryBeanName, ServerFactoryBean.class);
            customizedServerFactory = true;
        } else {
            serverFactory = new JaxWsServerFactoryBean();
        }

        serverFactory.setServiceBean(implementor);
        serverFactory.setServiceClass(ClassHelper.getRealClass(implementor));
        serverFactory.setAddress(url);

        DataBinding dataBinding;
        if (prototypeDataBindingBeanName != null) {
            if (!beanFactory.isPrototype(prototypeDataBindingBeanName)) {
                throw new IllegalArgumentException("prototypeDataBindingBeanName must indicate a scope='prototype' bean");
            }
            customizedDataBinding = true;
            dataBinding = beanFactory.getBean(prototypeDataBindingBeanName, DataBinding.class);
        } else {
            dataBinding = new JAXBDataBinding();
        }

        serverFactory.setDataBinding(dataBinding);
        serverFactory.setBus(getServletBus());
        serverFactory.create();
    }

    /**
     * 检查服务是否已发布到bus
     *
     * @param url 服务url
     * @return true已发布，false-未发布
     */
    protected boolean checkUrlPublish(String url) {
        DestinationRegistry dr = getDestinationRegistryFromBus(getServletBus());
        if (dr == null) {
            return false;
        }

        AbstractHTTPDestination d = dr.getDestinationForPath(url);
        if (d != null) {
            logger.info("service " + url + "is published by " + d.getBeanName());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取服务的bus注册器
     *
     * @param bus 服务注册
     * @return 注册对象
     */
    protected DestinationRegistry getDestinationRegistryFromBus(Bus bus) {
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        if (dfm == null) {
            return null;
        }

        try {
            DestinationFactory df = dfm.getDestinationFactory("http://cxf.apache.org/transports/http/configuration");

            if (df == null) {
                return null;
            }

            if (df instanceof HTTPTransportFactory) {
                HTTPTransportFactory transportFactory = (HTTPTransportFactory) df;
                return transportFactory.getRegistry();
            }
        } catch (BusException e) {
            // why are we throwing a busexception if the DF isn't found?
        }

        return null;
    }

    @Override
    public void handle(Object serviceBean, String beanName, Class<?> interfaceClass) {
        // shadowCxfServlet未初始化，不能发布
        if (shadowCxfServlet == null) {
            return;
        }

        // 检查是否可发布服务
        if (!this.isEnable()) {
            return;
        }

        Class<?> clazz = ClassHelper.getRealClass(getServletBus(), serviceBean);
        // 获取服务的注解信息
        WebService webServiceAnno = this.getAnnotation(clazz, interfaceClass);
        if (webServiceAnno == null) {
            return;
        }

        // 生成映射URL
        String url = RpcHelper.getServiceUrl(urlPrefix, serviceSuffix, webServiceAnno.serviceName(), interfaceClass);
        // 检查是否被映射
        if (getHandlerMap().containsKey(url)) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("web service(%s) has been published,can't publish by %s", url, beanName));
            }

            return;
        }

        // 创建服务的EndPoint
        createAndPublishEndpoint(url, serviceBean);
        // 发布服务
        registerHandler(url, creator.create(shadowCxfServlet));
        if (logger.isInfoEnabled()) {
            logger.info(String.format("web service(%s) is published by [%s][%s]", url, beanName, clazz.getName()));
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        try {
            shadowCxfServlet = (Servlet) servletClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            shadowCxfServlet.init(servletConfig);
        } catch (ServletException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
