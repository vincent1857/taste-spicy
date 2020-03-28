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

package cn.vincent.taste.spicy.helper.convert;

import net.sf.cglib.core.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2017/8/20 01:52
 */
public class BeanFilterConverter implements Converter {

    public List<String> filters = new ArrayList<>();

    public BeanFilterConverter(String[] filters) {
        if (filters != null && filters.length > 0) {
            this.filters.addAll(Arrays.asList(filters));
        }
    }

    /**
     * @param value   源对象属性
     * @param target  目标对象属性类
     * @param context 目标对象setter方法名
     * @return 拷贝
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object convert(Object value, Class target, Object context) {
        String methodName = (String) context;
        methodName = StringUtils.uncapitalize(methodName.substring(3));
        if (filters.contains(methodName)) {
            return null;
        }
        return value;
    }
}