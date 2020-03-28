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

package cn.vincent.taste.spicy.helper.convert;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拷贝对象属性工具
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:52
 */
public class BeanConvert {

    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    private BeanConvert() {
    }

    /**
     * 拷贝属性, 过滤属性
     *
     * @param source      源对象
     * @param targetClass 目标对象类
     * @param args        需要过滤的属性名称
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象
     */
    public static <T1, T2> T2 copyFilterProperties(T1 source, Class<T2> targetClass, String... args) {
        BeanFilterConverter filterConverter = new BeanFilterConverter(args);
        return copyProperties(source, targetClass, filterConverter);
    }

    /**
     * 拷贝属性, 过滤属性
     *
     * @param source 源对象
     * @param target 目标对象
     * @param args   需要过滤的属性名称
     * @param <T1>   t1
     * @param <T2>   t2
     */
    public static <T1, T2> void copyFilterProperties(T1 source, T2 target, String... args) {
        BeanFilterConverter filterConverter = new BeanFilterConverter(args);
        copyProperties(source, target, filterConverter);
    }

    /**
     * 拷贝属性, 默认转换处理
     *
     * @param source      源对象
     * @param targetClass Class 目标类class
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象
     */
    public static <T1, T2> T2 copyProperties(T1 source, Class<T2> targetClass) {
        return copyProperties(source, targetClass, null);
    }

    /**
     * 拷贝属性
     *
     * @param source      源对象
     * @param targetClass Class 目标类class
     * @param converter   转换处理类
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象
     */
    public static <T1, T2> T2 copyProperties(T1 source, Class<T2> targetClass, Converter converter) {
        if (source == null) {
            return null;
        }

        T2 t;
        try {
            t = targetClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        copyProperties(source, t, converter);
        return t;
    }

    /**
     * 拷贝属性, 默认转换处理
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T1>   t1
     * @param <T2>   t2
     */
    public static <T1, T2> void copyProperties(T1 source, T2 target) {
        copyProperties(source, target, null);
    }

    /**
     * 拷贝属性
     *
     * @param source    源对象
     * @param target    目标对象
     * @param converter 转换处理类
     * @param <T1>      t1
     * @param <T2>      t2
     */
    public static <T1, T2> void copyProperties(T1 source, T2 target, Converter converter) {
        if (source == null || target == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), converter != null);
        copier.copy(source, target, converter);
    }

    /**
     * 拷贝列表数据的属性, 无转换器
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象class
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象列表
     */
    public static <T1, T2> List<T2> copyListProperties(List<T1> sourceList, Class<T2> targetClass) {
        return copyListProperties(sourceList, targetClass, null);
    }

    /**
     * 拷贝列表数据的属性, 忽略部分属性
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象class
     * @param args        忽略的属性名
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象列表
     */
    public static <T1, T2> List<T2> copyListFilterProperties(List<T1> sourceList, Class<T2> targetClass, String... args) {
        BeanFilterConverter filterConverter = new BeanFilterConverter(args);
        return copyListProperties(sourceList, targetClass, filterConverter);
    }

    /**
     * 拷贝列表数据的属性
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象class
     * @param converter   转换对象
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 目标对象列表
     */
    public static <T1, T2> List<T2> copyListProperties(List<T1> sourceList, Class<T2> targetClass, Converter converter) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }

        List<T2> resultList = new ArrayList<>(sourceList.size());
        for (T1 t1 : sourceList) {
            T2 t2;
            try {
                t2 = targetClass.newInstance();
                copyProperties(t1, t2, converter);
                resultList.add(t2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    /**
     * 获取缓存的key
     *
     * @param class1      Class 源对象所属class
     * @param class2      Class 目标对象所属class
     * @param convertFlag boolean 是否转换
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 缓存key
     */
    private static <T1, T2> String generateKey(Class<T1> class1, Class<T2> class2, boolean convertFlag) {
        return class1.toString() + class2.toString() + convertFlag;
    }

    /**
     * 获取BeanCopier
     *
     * @param sourceClass Class 源对象所属class
     * @param targetClass Class 目标对象所属class
     * @param convertFlag boolean 是否转换
     * @param <T1>        t1
     * @param <T2>        t2
     * @return 转换实现对象
     */
    private static <T1, T2> BeanCopier getBeanCopier(Class<T1> sourceClass, Class<T2> targetClass, boolean convertFlag) {
        String beanKey = generateKey(sourceClass, targetClass, convertFlag);
        BeanCopier copier;
        if (!BEAN_COPIER_CACHE.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, convertFlag);
            BEAN_COPIER_CACHE.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(beanKey);
        }
        return copier;
    }
}
