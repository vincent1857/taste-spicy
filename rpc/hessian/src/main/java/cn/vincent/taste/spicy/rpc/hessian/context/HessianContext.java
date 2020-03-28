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

package cn.vincent.taste.spicy.rpc.hessian.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 * @version 1.0 2017/8/20 15:41
 */
public class HessianContext {

    private static ThreadLocal<HessianContext> contextThreadLocal = new ThreadLocal<>();

    protected final Map<String, String> headers = new ConcurrentHashMap<>();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(this.headers);
    }

    public void release() {
        this.headers.clear();
    }

    /**
     * 设置当前的Context
     *
     * @param context 上下文对象
     */
    public static void setContext(HessianContext context) {
        contextThreadLocal.set(context);
    }

    /**
     * 取得当前的Context
     *
     * @return 上下文对象
     */
    public static HessianContext getContext() {
        HessianContext ctx = contextThreadLocal.get();
        if (ctx == null) {
            ctx = new HessianContext();
            contextThreadLocal.set(ctx);
        }
        return ctx;
    }

    /**
     * 移除当前上下文信息
     */
    public static void removeContext() {
        if (contextThreadLocal.get() != null) {
            contextThreadLocal.get().release();
            contextThreadLocal.remove();
        }
    }

    /**
     * 判断当前是否有Context
     *
     * @return 结果true/false
     */
    public static boolean hasContext() {
        return (contextThreadLocal.get() != null);
    }
}
