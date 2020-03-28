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

package cn.vincent.taste.spicy.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * spring容器管理
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:26
 */
@Slf4j
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据Bean名称获取实例
     *
     * @param name Bean注册名称
     * @return bean实例
     * @throws BeansException 异常
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static Object getBeanSilent(String name) {
        try {
            return getBean(name);
        } catch (BeansException e) {
            log.warn("get bean", e);
        }
        return null;
    }

    /**
     * 根据Class获取实例
     *
     * @param cls Bean注册类
     * @return bean实例
     * @throws BeansException 异常
     */
    public static <T> T getBean(Class<T> cls) throws BeansException {
        return applicationContext.getBean(cls);
    }

    public static <T> T getBeanSilent(Class<T> clazz) {
        try {
            return getBean(clazz);
        } catch (BeansException e) {
            log.warn("get bean", e);
        }
        return null;
    }

    /**
     * 根据Class获取实例
     *
     * @param name Bean注册名称
     * @return bean实例
     * @throws BeansException 异常
     */
    public static boolean containsBean(String name) throws BeansException {
        return applicationContext.containsBean(name);
    }

    /**
     * 根据Bean的名称获取预期类型的Bean
     *
     * @param beanName     Bean的名称
     * @param expectedType 预期类型
     * @return bean对象
     */
    public static <T> T getBean(String beanName, Class<T> expectedType) throws BeansException {
        return applicationContext.getBean(beanName, expectedType);
    }

    public static <T> T getBeanSilent(String beanName, Class<T> expectedType) {
        try {
            return getBean(beanName, expectedType);
        } catch (BeansException e) {
            log.warn("get bean", e);
        }
        return null;
    }

    public static <T> List<T> getBeans(Class<T> expectedType) throws BeansException {
        return new ArrayList<>(applicationContext.getBeansOfType(expectedType).values());
    }

    public static <T> List<T> getBeansSilent(Class<T> expectedType) {
        try {
            return getBeans(expectedType);
        } catch (BeansException e) {
            log.warn("get beans warn", e);
        }
        return null;
    }
}
