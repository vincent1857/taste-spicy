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

package cn.vincent.taste.spicy.jackson;

import cn.vincent.taste.spicy.jackson.serializer.AbstractJsonSerializer;
import cn.vincent.taste.spicy.support.annotation.json.JsonSerializerType;
import cn.vincent.taste.spicy.support.annotation.json.JsonValueParse;
import cn.vincent.taste.spicy.support.json.JsonScope;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jackson拦截器扩展
 *
 * @author vincent
 * @version 1.0 2017/8/20 15:54
 */
@Slf4j
public class ExtJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    private static final Map<String, AbstractJsonSerializer> JSONSERIALIZER_MAP = new ConcurrentHashMap<>();

    protected String scope;

    private Map<String, Class<? extends AbstractJsonSerializer>> serializerClasses = new HashMap<>();

    public ExtJacksonAnnotationIntrospector(String scope, List<Class<? extends AbstractJsonSerializer>> serializers) {
        this.scope = scope;
        if (serializers != null && serializers.size() > 0) {
            for (Class<? extends AbstractJsonSerializer> serializerClass : serializers) {

                String type = null;
                JsonSerializerType serializerType = serializerClass.getAnnotation(JsonSerializerType.class);
                if (serializerType != null) {
                    type = serializerType.value();
                }

                if (StringUtils.isBlank(type)) {
                    type = serializerClass.getSimpleName();
                }

                if (this.serializerClasses.containsKey(type)) {
                    log.warn("序列化名称重复, {}", serializerClass.getName());
                    continue;
                }

                this.serializerClasses.put(type, serializerClass);
            }
        }
    }

    @Override
    public Object findSerializer(Annotated annotated) {
        Object obj = super.findSerializer(annotated);
        if (obj != null) {
            return obj;
        }

        // 从缓存获取
        String name = annotated.toString();
        if (JSONSERIALIZER_MAP.containsKey(name)) {
            return JSONSERIALIZER_MAP.get(name);
        }

        // 获取注解
        JsonValueParse jsonValueParse = annotated.getAnnotation(JsonValueParse.class);
        if (jsonValueParse == null) {
            return null;
        }
        List<String> scopes = Arrays.asList(jsonValueParse.scopes());
        if (!scopes.contains(JsonScope.ALL) && !scopes.contains(this.scope)) {
            return null;
        }

        String type = jsonValueParse.type();
        if (StringUtils.isBlank(type) || !this.serializerClasses.containsKey(type)) {
            return null;
        }

        AbstractJsonSerializer serializer = null;
        Class<? extends AbstractJsonSerializer> clazz = this.serializerClasses.get(type);
        try {
            Constructor<? extends AbstractJsonSerializer> constructor = clazz.getConstructor(JsonValueParse.class);
            serializer = constructor.newInstance(jsonValueParse);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error("init serializer class error for class " + clazz.getName(), e);
        }

        JSONSERIALIZER_MAP.put(name, serializer);
        return serializer;
    }
}
