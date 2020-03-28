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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 扩展spring的BeanUtils，增加拷贝属性排除null值的功能(注：String为null不考虑)
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:15
 */
public class BeanPropertiesCopier extends BeanUtils {

    public static void copyNotNullProperties(Object source, Object target, String[] ignoreProperties) throws BeansException {
        copyNotNullProperties(source, target, null, ignoreProperties);
    }

    public static void copyNotNullProperties(Object source, Object target, Class<?> editable) throws BeansException {
        copyNotNullProperties(source, target, editable, null);
    }

    public static void copyNotNullProperties(Object source, Object target) throws BeansException {
        copyNotNullProperties(source, target, null, null);
    }

    private static void copyNotNullProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            boolean flag = (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())));
            if (targetPd.getWriteMethod() != null && flag) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        boolean stringValueTypeFlag = "java.lang.String".equals(readMethod.getReturnType().getName());
                        // 这里判断以下value是否为空，当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等，如果是String类型，则不需要验证是否为空
                        if (value != null || stringValueTypeFlag) {
                            boolean isEmpty = false;
                            if (stringValueTypeFlag) {
                                if (value == null) {
                                    isEmpty = true;
                                } else {
                                    String stringValue = (String) value;
                                    if ("".equals(stringValue.trim())) {
                                        isEmpty = true;
                                    }
                                }
                            }
                            if (value instanceof Set) {
                                Set<?> s = (Set<?>) value;
                                if (s.isEmpty()) {
                                    isEmpty = true;
                                }
                            } else if (value instanceof Map) {
                                Map<?, ?> m = (Map<?, ?>) value;
                                if (m.isEmpty()) {
                                    isEmpty = true;
                                }
                            } else if (value instanceof List) {
                                List<?> l = (List<?>) value;
                                if (l.size() < 1) {
                                    isEmpty = true;
                                }
                            } else if (value instanceof Collection) {
                                Collection<?> c = (Collection<?>) value;
                                if (c.size() < 1) {
                                    isEmpty = true;
                                }
                            }
                            if (!isEmpty) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

    public static void copyNotNullPropertiesSimple(Object source, Object target, String[] ignoreProperties) throws BeansException {
        copyNotNullPropertiesSimple(source, target, null, ignoreProperties);
    }

    public static void copyNotNullPropertiesSimple(Object source, Object target, Class<?> editable) throws BeansException {
        copyNotNullPropertiesSimple(source, target, editable, null);
    }

    public static void copyNotNullPropertiesSimple(Object source, Object target) throws BeansException {
        copyNotNullPropertiesSimple(source, target, null, null);
    }

    /**
     * 这个实现只是简单的判断值是否等于null，不做其他默认值的判断
     *
     * @param source           源对象
     * @param target           目标对象
     * @param editable         是否可编辑
     * @param ignoreProperties 忽略属性
     * @throws BeansException 异常
     */
    private static void copyNotNullPropertiesSimple(Object source, Object target, Class<?> editable, String[] ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            boolean flag = (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())));
            if (targetPd.getWriteMethod() != null && flag) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }
}
