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

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 配置选项抽象类
 *
 * @author vincent
 * @version 1.0 2017/8/20 00:00
 */
public abstract class AbstractConfiguration implements Configuration {

    @Override
    public String get(String key, String defaultValue) {
        String value = this.get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public boolean getBoolean(String key) {
        return BooleanUtils.toBoolean(this.get(key));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (!this.hasConfig(key)) {
            return defaultValue;
        }
        return this.getBoolean(key);
    }

    @Override
    public int getInt(String key) {
        return NumberUtils.toInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return NumberUtils.toInt(this.get(key), defaultValue);
    }

    @Override
    public long getLong(String key) {
        return NumberUtils.toLong(this.get(key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return NumberUtils.toLong(this.get(key), defaultValue);
    }

    @Override
    public double getDouble(String key) {
        return NumberUtils.toDouble(this.get(key));
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return NumberUtils.toDouble(this.get(key), defaultValue);
    }

    @Override
    public float getFloat(String key) {
        return NumberUtils.toFloat(this.get(key));
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return NumberUtils.toFloat(this.get(key), defaultValue);
    }
}
