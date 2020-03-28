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

package cn.vincent.taste.spicy.mybatis.page;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持通配符扫包
 *
 * @author vincent
 * @version 1.0 2019/9/23 22:40
 */
@Slf4j
public class PathSqlSessionFactoryBean extends SqlSessionFactoryBean {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    static final String ANY_PATTERN = "*";

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private Environment environment;

    public PathSqlSessionFactoryBean() {
        this(new StandardEnvironment());
    }

    public PathSqlSessionFactoryBean(Environment environment) {
        this.environment = environment;
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    protected List<Class<?>> doScan(String basePackage) {
        List<Class<?>> clazzes = new ArrayList<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                log.info("Scanning " + resource);
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    try {
                        Class<?> clazz = Class.forName(className);
                        clazzes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        log.error("class not found for " + className, e);
                    }
                } else {
                    log.warn("Ignored because not readable: " + resource);
                }
            }
        } catch (IOException e) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }

        return clazzes;
    }

    @Override
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        if (org.apache.commons.lang3.StringUtils.isBlank(typeAliasesPackage) || !typeAliasesPackage.contains(ANY_PATTERN)) {
            super.setTypeAliasesPackage(typeAliasesPackage);
            return;
        }
        String[] scanPackageArray = StringUtils.tokenizeToStringArray(typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        List<Class<?>> scanClazzes = new ArrayList<>();
        for (String basePackage : scanPackageArray) {
            List<Class<?>> clazzes = this.doScan(basePackage);
            if (null != clazzes && clazzes.size() > 0) {
                scanClazzes.addAll(clazzes);
            }
        }
        super.setTypeAliasesPackage(null);
        super.setTypeAliases(scanClazzes.toArray(new Class<?>[0]));
    }

    public void setEnvironment(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return this.environment;
    }
}
