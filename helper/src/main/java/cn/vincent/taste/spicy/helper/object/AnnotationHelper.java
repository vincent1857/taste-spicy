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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.vincent.taste.spicy.helper.object;

import cn.vincent.taste.spicy.helper.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author vincent
 * @version 1.0 2019/11/10 16:13
 */
@Slf4j
public class AnnotationHelper {

    protected static ConcurrentMap<String, Annotation> CLASSTYPECACHE_MAP
            = new ConcurrentHashMap<>(16, 0.75f, 1);

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Object obj, String curentName, Class<T> annotationClass) {
        Class<?> cls = obj.getClass();
        Annotation anno = CLASSTYPECACHE_MAP.get(cls.getName() + Constant.MARK_POINT + curentName);
        if (anno != null && annotationClass.isAssignableFrom(anno.getClass())) {
            return (T) anno;
        }

        String methodName = "get" + StringUtils.capitalize(curentName);
        Method method = null;
        try {
            method = ReflectionHelper.getDeclaredMethod(obj, methodName);
        } catch (Exception e) {
            log.warn("从{}获取方法{}失败,{}", cls.getName(), methodName, e.toString());
        }

        T annotation;
        if (method != null && method.isAnnotationPresent(annotationClass)) {
            annotation = method.getAnnotation(annotationClass);
            CLASSTYPECACHE_MAP.put(cls.getName() + Constant.MARK_POINT + curentName, annotation);
            return annotation;
        } else {
            // 从属性获取
            Field field = null;
            try {
                field = ReflectionHelper.getDeclaredField(obj, curentName);
            } catch (Exception e) {
                log.warn("从{}获取属性{}失败,{}", cls.getName(), curentName, e.toString());
            }

            if (field != null && field.isAnnotationPresent(annotationClass)) {
                annotation = field.getAnnotation(annotationClass);
                CLASSTYPECACHE_MAP.put(cls.getName() + Constant.MARK_POINT + curentName, annotation);
                return annotation;
            }
        }
        return null;
    }
}
