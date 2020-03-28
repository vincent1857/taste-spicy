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

package cn.vincent.taste.spicy.web.processor;

import cn.vincent.taste.spicy.helper.basic.Libs;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2019-07-01 17:49
 */
public class ParamNameDataBinder extends ExtendedServletRequestDataBinder {

    public ParamNameDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        Map<String, Object> valueList = new HashMap<>(16);
        for (PropertyValue propertyValue : mpvs.getPropertyValueList()) {
            String name = propertyValue.getName();
            String cname = Libs.underlineToCamel(name);
            Object value = propertyValue.getValue();
            valueList.put(cname, value);
        }
        mpvs.addPropertyValues(valueList);
        super.addBindValues(mpvs, request);
    }
}
