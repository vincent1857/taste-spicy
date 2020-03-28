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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.vincent.taste.spicy.support.annotation.json;

import cn.vincent.taste.spicy.support.json.JsonScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解解析
 *
 * @author vincent
 * @version 1.0 2019/12/16 16:15
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonValueParse {

    /**
     * 默认后缀
     */
    String DEFAULT_SUFFIX = "Cnt";

    /**
     * 解析名称
     *
     * @return 名称字符串
     */
    String type();

    /**
     * 生效会话类型
     *
     * @return 默认为输出
     */
    String[] scopes() default JsonScope.OUTPUT;

    /**
     * 后缀
     *
     * @return 后缀值
     */
    String suffix() default DEFAULT_SUFFIX;

    /**
     * 附加参数
     *
     * @return 数组
     */
    String[] params() default {};
}
