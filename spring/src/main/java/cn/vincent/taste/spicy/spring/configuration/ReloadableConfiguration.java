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

import cn.vincent.taste.spicy.helper.character.CharsetKit;
import cn.vincent.taste.spicy.helper.configuration.AbstractConfiguration;
import cn.vincent.taste.spicy.spring.constant.GlobalConfigurationConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * 自动刷新的配置工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:35
 */
@Slf4j
public class ReloadableConfiguration extends AbstractConfiguration {

    /** 重新加载的时间间隔，秒为单位 */
    private int reloadTimespan = 30;

    /** 配置文件名，不带后缀 */
    protected String resourceBaseName;

    protected ReloadableResourceBundleMessageSource resource;

    public ReloadableConfiguration(String resourceBaseName) {
        this(resourceBaseName, new ReloadableResourceBundleMessageSource());
    }

    public ReloadableConfiguration(String resourceBaseName, ReloadableResourceBundleMessageSource resource) {
        this(resourceBaseName, -1, resource);
    }

    public ReloadableConfiguration(String resourceBaseName, int reloadTimespan, ReloadableResourceBundleMessageSource resource) {
        this.resourceBaseName = resourceBaseName;
        this.resource = resource == null ? new ReloadableResourceBundleMessageSource() : resource;
        if (reloadTimespan >= 0) {
            this.reloadTimespan = reloadTimespan;
        }
        this.initResource();
    }

    protected void initResource() {
        try {
            this.resource.setBasename(this.resourceBaseName);
            this.resource.setDefaultEncoding(CharsetKit.UTF_8);
            int time = this.getReloadTimespan();
            this.resource.setCacheSeconds(time);
            this.resource.clearCache();
        } catch (Exception e) {
            log.error("初始化加载classpath:" + resource + ".propertie出错", e);
        }
    }

    protected int getReloadTimespan() {
        String strTimeSpan = get(GlobalConfigurationConstant.CONF_SYS_RELOAD_TIMESPAN);
        if (!StringUtils.isBlank(strTimeSpan)) {
            return NumberUtils.toInt(strTimeSpan, reloadTimespan);
        }

        return reloadTimespan;
    }

    @Override
    public String get(String key) {

        if (this.resource == null) {
            log.debug("find key in config resource: {}=null", key);
            return null;
        }

        try {
            String text = this.resource.getMessage(key, null, Locale.getDefault());
            log.debug("find key in config resource: {}={}", key, text);
            return text;
        } catch (NoSuchMessageException ex) {
            if (log.isDebugEnabled()) {
                log.debug("not find key in config resource: {}", key);
            }
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error("get config " + key + " fail", ex);
            }
        }
        return null;
    }

    @Override
    public boolean hasConfig(String key) {
        return this.resource != null && this.get(key) != null;
    }
}
