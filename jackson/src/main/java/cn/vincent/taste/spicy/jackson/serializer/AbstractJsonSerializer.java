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

import cn.vincent.taste.spicy.support.annotation.json.JsonValueParse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author vincent
 * @version 1.0 2017/8/20 14:02
 */
@Slf4j
public abstract class AbstractJsonSerializer extends JsonSerializer<Object> {

    protected JsonValueParse jsonValueParse;

    public AbstractJsonSerializer(JsonValueParse jsonValueParse) {
        this.jsonValueParse = jsonValueParse;
    }

    /**
     * 实际序列化
     *
     * @param value          原始值
     * @param jsonGenerator  序列化
     * @return 序列化数据
     * @throws IOException 异常
     */
    protected abstract Object attach(Object value, JsonGenerator jsonGenerator) throws IOException;

    protected boolean replace() {
        return StringUtils.isBlank(this.jsonValueParse.suffix());
    }

    /**
     * 写入原始值
     *
     * @param value          原始值
     * @param jsonGenerator  序列化
     * @param provider       提供者
     * @throws IOException 异常
     */
    protected void writeOrgValue(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (!this.replace()) {
            jsonGenerator.writeObject(value);
        }
    }

    protected void writeValue(String fieldName, Object value,
                              JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (!this.replace()) {
            PropertyNamingStrategy pr = provider.getConfig().getPropertyNamingStrategy();
            if (pr instanceof PropertyNamingStrategy.PropertyNamingStrategyBase) {
                PropertyNamingStrategy.PropertyNamingStrategyBase rename = (PropertyNamingStrategy.PropertyNamingStrategyBase) pr;
                fieldName = rename.translate(fieldName);
            }
            jsonGenerator.writeFieldName(fieldName);
        }
        jsonGenerator.writeObject(value);

    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        Object obj = jsonGenerator.getCurrentValue();
        if (obj == null) {
            return;
        }
        String curentName = jsonGenerator.getOutputContext().getCurrentName();

        if (this.jsonValueParse == null) {
            jsonGenerator.writeObject(value);
            return;
        }

        this.writeOrgValue(value, jsonGenerator, provider);
        Object nameValue = this.attach(value, jsonGenerator);
        if (nameValue != null) {
            String fieldName = this.replace() ? curentName : curentName + this.jsonValueParse.suffix();
            this.writeValue(fieldName, nameValue, jsonGenerator, provider);
        }
    }
}
