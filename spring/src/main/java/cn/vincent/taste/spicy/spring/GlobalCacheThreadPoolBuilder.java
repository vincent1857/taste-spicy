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

import cn.vincent.taste.spicy.helper.thread.GlobalCacheThreadPool;
import cn.vincent.taste.spicy.spring.configuration.AppConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vincent
 * @version 1.0 2017/8/20 02:50
 */
@Slf4j
public class GlobalCacheThreadPoolBuilder {

    public static final String DEFAULT_NAME = "default";

    public static final String THREAD_POOL_SIZE_PREFFIX = "thread.pool.size.";
    public static final String THREAD_POOL_MAX_SIZE_PREFFIX = "thread.pool.maxsize.";
    public static final String THREAD_POOL_QUEUE_MAX_SIZE_PREFFIX = "thread.pool.queue.maxsize.";

    private GlobalCacheThreadPoolBuilder(){

    }

    public static GlobalCacheThreadPool build() {
        return build(DEFAULT_NAME);
    }

    public static GlobalCacheThreadPool build(String poolName) {
        log.info("synchronized get GlobalCacheThreadPool for poolName {}", poolName);
        GlobalCacheThreadPool pool = GlobalCacheThreadPool.getPool(poolName, false);
        if (pool == null) {
            log.info("synchronized get GloabalCacheThreadPool for poolName {} init...", poolName);
            AppConfiguration configuration = AppConfiguration.getInstance();

            synchronized (GlobalCacheThreadPoolBuilder.class) {
                int poolSize = configuration.getInt(THREAD_POOL_SIZE_PREFFIX + poolName, 0);
                int maxPoolSize = configuration.getInt(THREAD_POOL_MAX_SIZE_PREFFIX + poolName, 0);
                int poolQueueMaxSize = configuration.getInt(THREAD_POOL_QUEUE_MAX_SIZE_PREFFIX + poolName, 0);

                pool = GlobalCacheThreadPool.registe(poolName, poolSize, maxPoolSize, poolQueueMaxSize);
            }
        }
        return pool;
    }
}
