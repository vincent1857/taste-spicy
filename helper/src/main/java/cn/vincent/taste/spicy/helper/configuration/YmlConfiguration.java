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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 * @version 1.0 2019/9/23 15:49
 */
public class YmlConfiguration extends AbstractConfiguration {

    private final Map<String, String> cacheMap = new ConcurrentHashMap<>();
    private final Map<?, ?> fullMap;

    public YmlConfiguration(String filePath) {
        super();
        // 加载配置文件
        Validate.notNull(filePath, "yml filePath can not be null");
        this.fullMap = YmlHelper.loadYaml(filePath);
    }

    @Override
    public String get(String key) {
        String value = cacheMap.get(key);
        if (value == null) {
            Object valueObj = YmlHelper.getProperty(fullMap, key);
            value = (valueObj == null ? StringUtils.EMPTY : String.valueOf(valueObj));
            cacheMap.put(key, value);
        }
        return value;
    }

    @Override
    public boolean hasConfig(String key) {
        return StringUtils.isNotBlank(get(key));
    }

    public static void main(String[] args) {
        new YmlConfiguration("classpath:test.xml");
    }
}
