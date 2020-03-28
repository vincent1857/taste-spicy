/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.vincent.taste.spicy.jackson.ExtJacksonAnnotationIntrospector;
import cn.vincent.taste.spicy.jackson.serializer.AbstractJsonSerializer;
import cn.vincent.taste.spicy.jackson.serializer.DesensitizedSerializer;
import cn.vincent.taste.spicy.jackson.serializer.IdToNameJsonSerializer;
import cn.vincent.taste.spicy.jackson.support.IdToNameHandlerCollect;
import cn.vincent.taste.spicy.support.json.IdToNameHandler;
import cn.vincent.taste.spicy.support.json.JsonScope;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2017/8/20 2018/10/12 02:52
 */
public class Test {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 没有setter getter不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // key值可以不带引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许特殊字符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 驼峰转下划线
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // 自定义
        List<Class<? extends AbstractJsonSerializer>> serializers = new ArrayList<>();
        serializers.add(DesensitizedSerializer.class);
        serializers.add(IdToNameJsonSerializer.class);

        ExtJacksonAnnotationIntrospector introspector = new ExtJacksonAnnotationIntrospector(JsonScope.OUTPUT, serializers);
        mapper.setAnnotationIntrospector(introspector);

        IdToNameHandlerCollect collect = new IdToNameHandlerCollect();
        Map<String, IdToNameHandler> handlers = new HashMap<>();
        handlers.put("test", new TestInToNameHandler());
        collect.setHandlers(handlers);
    }

    public static void main(String[] args) throws IOException {
        TestVO vo = new TestVO();
        vo.setId("123");
//        vo.setName("彭国卿");
        vo.setNameDesc("彭国卿");

        String json = mapper.writeValueAsString(vo);

        System.out.println(json);
        System.out.println(mapper.readValue(json, TestVO.class));

    }
}
