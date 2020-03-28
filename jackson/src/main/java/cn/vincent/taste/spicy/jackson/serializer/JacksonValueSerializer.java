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

import cn.vincent.taste.spicy.helper.type.ObjectTypeReference;
import cn.vincent.taste.spicy.helper.type.ValueSerializer;
import cn.vincent.taste.spicy.jackson.type.JacksonTypeReference;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author vincent
 * @version 1.0 2019/9/23 21:31
 */
@Slf4j
public class JacksonValueSerializer implements ValueSerializer {

    private ObjectMapper objectMapper;

    public JacksonValueSerializer() {
        this.objectMapper = new ObjectMapper();
        // 没有setter getter不报错
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // key值可以不带引号
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许特殊字符
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    public JacksonValueSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("serialize json object error", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(String string, ObjectTypeReference<T> type) {
        try {
            return this.objectMapper.readValue(string, new JacksonTypeReference<T>(type.getType()) { });
        } catch (IOException e) {
            log.error("deserialize json object error", e);
        }
        return null;
    }
}
