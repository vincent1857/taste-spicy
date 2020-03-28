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

package cn.vincent.taste.spicy.helper.configuration;

/**
 * 配置选项接口
 *
 * @author vincent
 * @version 1.0 2017/8/20 23:58
 */
public interface Configuration {

    /**
     * 获取字符串型的值
     *
     * @param key key值
     * @return value值
     */
    String get(String key);

    /**
     * 获取字符串型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    String get(String key, String defaultValue);

    /**
     * 获取布尔型的值
     *
     * @param key key值
     * @return value值
     */
    boolean getBoolean(String key);

    /**
     * 获取布尔型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取整型的值
     *
     * @param key key值
     * @return value值
     */
    int getInt(String key);

    /**
     * 获取整型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取长整型的值
     *
     * @param key key值
     * @return value值
     */
    long getLong(String key);

    /**
     * 获取长整型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    long getLong(String key, long defaultValue);

    /**
     * 获取Double型的值
     *
     * @param key key值
     * @return value值
     */
    double getDouble(String key);

    /**
     * 获取Double型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    double getDouble(String key, double defaultValue);

    /**
     * 获取Float型的值
     *
     * @param key key值
     * @return value值
     */
    float getFloat(String key);

    /**
     * 获取Float型的值
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return value值
     */
    float getFloat(String key, float defaultValue);

    /**
     * 是否存在配置
     *
     * @param key key值
     * @return true/false
     */
    boolean hasConfig(String key);
}
