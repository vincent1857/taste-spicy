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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件配置属性
 *
 * @author vincent
 * @version 1.0 2017/8/20 00:13
 */
@Slf4j
public class PropertiesConfiguration extends AbstractConfiguration {

    private Properties properties;

    public PropertiesConfiguration(String filePath) {
        super();
        // 加载配置文件
        Validate.notNull(filePath, "properties filePath can not be null");
        try {
            this.properties = PropertiesHelper.loadPropertiesFile(filePath);
        } catch (IOException e) {
            log.error("can not load properties file :" + filePath, e);
        }
    }

    public PropertiesConfiguration(Properties properties) {
        Validate.notNull(properties, "properties can not be null");
        this.properties = properties;
    }

    @Override
    public String get(String key) {
        if (this.properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    @Override
    public boolean hasConfig(String key) {
        return this.properties != null && this.properties.containsKey(key);
    }
}
