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

package cn.vincent.taste.spicy.fastjson.serializer;

import cn.vincent.taste.spicy.helper.type.ObjectTypeReference;
import cn.vincent.taste.spicy.helper.type.ValueSerializer;
import com.alibaba.fastjson.JSON;

/**
 * @author vincent
 * @version 1.0 2019/9/23 21:27
 */
public class FastjsonValueSerializer implements ValueSerializer {

    @Override
    public String serialize(Object object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T deserialize(String string, ObjectTypeReference<T> type) {
        return JSON.parseObject(string, type.getType());
    }
}
