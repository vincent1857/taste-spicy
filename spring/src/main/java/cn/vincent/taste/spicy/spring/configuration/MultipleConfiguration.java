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

package cn.vincent.taste.spicy.spring.configuration;

import cn.vincent.taste.spicy.helper.configuration.AbstractConfiguration;
import cn.vincent.taste.spicy.helper.configuration.PropertiesConfiguration;

/**
 * 复合功能的配置，常用于第三方接口
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:40
 */
public class MultipleConfiguration extends AbstractConfiguration {

    private ReloadableConfiguration reloadableConfiguration;

    private PropertiesConfiguration propertiesConfiguration;

    public MultipleConfiguration(ReloadableConfiguration reloadableConfiguration) {
        this.reloadableConfiguration = reloadableConfiguration;
        this.propertiesConfiguration = null;
    }

    public MultipleConfiguration(ReloadableConfiguration reloadableConfiguration, PropertiesConfiguration propertiesConfiguration) {
        this.reloadableConfiguration = reloadableConfiguration;
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    public String get(String key) {
        String value = reloadableConfiguration.get(key);
        if (value == null && propertiesConfiguration != null) {
            return propertiesConfiguration.get(key);
        }
        return value;
    }

    @Override
    public boolean hasConfig(String key) {
        return this.reloadableConfiguration.hasConfig(key) || (this.propertiesConfiguration != null && this.propertiesConfiguration.hasConfig(key));
    }

}