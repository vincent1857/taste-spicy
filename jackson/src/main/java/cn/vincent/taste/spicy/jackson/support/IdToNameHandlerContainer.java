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

package cn.vincent.taste.spicy.jackson.support;

import cn.vincent.taste.spicy.support.json.IdToNameHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 * @version 1.0 2017/8/20 02:16
 */
@Slf4j
public class IdToNameHandlerContainer {

    private static final IdToNameHandlerContainer INSTANCE = new IdToNameHandlerContainer();

    private final Map<String, IdToNameHandler> handlers = new ConcurrentHashMap<>();

    public static IdToNameHandlerContainer getInstance() {
        return INSTANCE;
    }

    public IdToNameHandler find(String name) {
        return handlers.get(name);
    }

    public void add(String name, IdToNameHandler handler) {
        String className = handler.getClass().getName();
        if (handlers.containsKey(name)) {
            log.warn("IdToNameHandlerContainer contains key " + name + ", this handler " + className + " add faild!");
        } else {
            handlers.put(name, handler);
        }

        if (handlers.containsKey(className)) {
            log.warn("IdToNameHandlerContainer contains key " + className + ", this handler " + className + " add faild!");
        } else {
            handlers.put(className, handler);
        }
    }

    public void concat(Map<String, IdToNameHandler> handlersMap) {
        if (handlersMap == null || handlersMap.size() == 0) {
            return;
        }

        for (String name : handlersMap.keySet()) {
            this.add(name, handlersMap.get(name));
        }
    }
}
