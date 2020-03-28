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

package cn.vincent.taste.spicy.jackson.serializer;

import cn.vincent.taste.spicy.jackson.support.IdToNameHandlerContainer;
import cn.vincent.taste.spicy.support.annotation.json.JsonSerializerType;
import cn.vincent.taste.spicy.support.annotation.json.JsonValueParse;
import cn.vincent.taste.spicy.support.constant.JsonSerializeNameConstant;
import cn.vincent.taste.spicy.support.json.IdToNameHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author vincent
 * @version 1.0 2017/8/20 04:11
 */
@Slf4j
@JsonSerializerType(JsonSerializeNameConstant.ID_TO_NAME)
public class IdToNameJsonSerializer extends AbstractJsonSerializer {

    public IdToNameJsonSerializer(JsonValueParse jsonValueParse) {
        super(jsonValueParse);
    }

    @Override
    protected Object attach(Object value, JsonGenerator jsonGenerator) {
        String[] params = this.jsonValueParse.params();
        if (params.length == 0) {
            return null;
        }

        String aliasName = params[0];
        if (StringUtils.isBlank(aliasName)) {
            return null;
        }

        IdToNameHandler handler = IdToNameHandlerContainer.getInstance().find(aliasName);
        if (handler == null) {
            return null;
        }

        return handler.idToName(value);
    }
}