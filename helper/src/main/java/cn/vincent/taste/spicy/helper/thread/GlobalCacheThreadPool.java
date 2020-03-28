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

package cn.vincent.taste.spicy.helper.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 全局线程池
 *
 * @author vincent
 * @version 1.0 2017/8/20 03:54
 */
@Slf4j
public class GlobalCacheThreadPool {

    private static final Map<String, GlobalCacheThreadPool> MAP_CACHE_THREAD_POOL = new ConcurrentHashMap<>();

    private static final String DEFAULT_POOL_NAME = "default";

    /** 默认线程池的基本大小为20 */
    private static final int DEFAULT_POOL_SIZE = 20;
    private static final int DEFAULT_POOL_MAX_SIZE = 20;
    private static final int DEFAULT_QUEUE_MAX_SIZE = Integer.MAX_VALUE;

    private ExecutorService executor;
    private BlockingQueue<Runnable> workQueue;

    private String name;
    private int corePoolSize = DEFAULT_POOL_SIZE;
    private int maxPoolSize = DEFAULT_POOL_MAX_SIZE;
    private int queueMaxSize = DEFAULT_QUEUE_MAX_SIZE;

    public static GlobalCacheThreadPool getDefaultPool() {
        return getPool(DEFAULT_POOL_NAME);
    }

    public static GlobalCacheThreadPool getPool(String name) {
        return getPool(name, true);
    }

    public static GlobalCacheThreadPool getPool(String name, boolean initFlag) {
        GlobalCacheThreadPool pool = MAP_CACHE_THREAD_POOL.get(name);
        if (pool == null && initFlag) {
            pool = registe(name, DEFAULT_POOL_SIZE, DEFAULT_POOL_MAX_SIZE, DEFAULT_QUEUE_MAX_SIZE);
        }


        return pool;
    }

    private GlobalCacheThreadPool() {

    }

    public static GlobalCacheThreadPool registe(int corePoolSize, int maxPoolSize, int queueMaxSize) {
        return registe(DEFAULT_POOL_NAME, corePoolSize, maxPoolSize, queueMaxSize);
    }

    public static GlobalCacheThreadPool registe(String name, int corePoolSize, int maxPoolSize, int queueMaxSize) {
        GlobalCacheThreadPool pool = MAP_CACHE_THREAD_POOL.get(name);
        if (pool != null) {
            throw new IllegalArgumentException("GloabalCacheThreadPool for name " + name + " already started!");
        }

        synchronized (GlobalCacheThreadPool.class) {
            pool = MAP_CACHE_THREAD_POOL.get(name);
            if (pool != null) {
                throw new IllegalArgumentException("GloabalCacheThreadPool for name " + name + " already started!");
            }

            pool = new GlobalCacheThreadPool();
            pool.corePoolSize = corePoolSize <= 0 ? DEFAULT_POOL_SIZE : corePoolSize;
            pool.maxPoolSize = maxPoolSize <= 0 ? DEFAULT_POOL_SIZE : maxPoolSize;
            pool.queueMaxSize = queueMaxSize <= 0 ? DEFAULT_POOL_SIZE : queueMaxSize;
            pool.name = name;
            pool.init();
            MAP_CACHE_THREAD_POOL.put(name, pool);
            return pool;
        }
    }

    private void init() {
        if (executor != null) {
            throw new IllegalArgumentException("GloabalCacheThreadPool for name " + name + "  already started!");
        }
        workQueue = new LinkedBlockingDeque<>(queueMaxSize);
        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60L, TimeUnit.SECONDS, workQueue, new NamedThreadFactory(name));
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getQueueMaxSize() {
        return queueMaxSize;
    }

    public String getName() {
        return name;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public static void shutdownByName(String name) {
        log.info("shutdown pool {}", name);
        GlobalCacheThreadPool pool = MAP_CACHE_THREAD_POOL.get(name);
        if (pool != null) {
            pool.shutdown();
            MAP_CACHE_THREAD_POOL.remove(name);
        }
    }

    public static void shutdownAll() {
        Set<String> keys = MAP_CACHE_THREAD_POOL.keySet();
        for (String key : keys) {
            shutdownByName(key);
        }
    }
}
