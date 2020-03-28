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

package cn.vincent.taste.spicy.helper.context;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:59
 */
public class ContextHolder {

    private static final ThreadLocal<Map<String, Object>> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private static final int INIT_SIZE = 16;

    public static void set(String name, Object context) {
        if (StringUtils.isBlank(name) || context == null) {
            throw new IllegalArgumentException("name or context can not be null");
        }

        Map<String, Object> map = CONTEXT_THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(INIT_SIZE);
            CONTEXT_THREAD_LOCAL.set(map);
        }

        map.put(name, context);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name can not be null");
        }
        Map<String, Object> map = CONTEXT_THREAD_LOCAL.get();
        if (map == null || map.size() == 0) {
            return null;
        }

        Object contextObj = map.get(name);
        if (contextObj == null) {
            return null;
        }

        return (T) contextObj;
    }

    public static void release(String name){
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name can not be null");
        }
        Map<String, Object> map = CONTEXT_THREAD_LOCAL.get();
        if (map == null || map.size() == 0) {
            return;
        }
        map.remove(name);
    }

    public static void release(){
        CONTEXT_THREAD_LOCAL.remove();
    }
}
