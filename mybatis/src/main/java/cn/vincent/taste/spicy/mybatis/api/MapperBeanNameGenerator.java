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

package cn.vincent.taste.spicy.mybatis.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;

/**
 * 自动实例化spring dao mapper管理
 *
 * @author vincent
 * @version 1.0 2017/8/20 00:34
 */
public class MapperBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Getter
    @Setter
    protected String prefix;
    @Getter
    @Setter
    protected String suffix;

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        assert definition.getBeanClassName() != null;
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());

        StringBuilder sb = new StringBuilder();
        // 拼接前缀
        if (StringUtils.hasText(prefix)) {
            sb.append(prefix);
        }

        sb.append(shortClassName);

        // 拼接后缀
        if (StringUtils.hasText(suffix)) {
            sb.append(suffix);
        }

        // 首字母小写,如果是连着两个大写，则不将首字母转小写
        return Introspector.decapitalize(sb.toString());
    }
}
