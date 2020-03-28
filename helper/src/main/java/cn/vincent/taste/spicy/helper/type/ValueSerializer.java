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

package cn.vincent.taste.spicy.helper.type;

/**
 * @author vincent
 * @version 1.0 2019/9/23 14:54
 */
public interface ValueSerializer {

    /**
     * 序列化
     *
     * @param object 对象
     * @return 字符串
     */
    String serialize(Object object);

    /**
     * 反序列化
     *
     * @param string 字符串
     * @param type   类型
     * @param <T>    泛型
     * @return 对象
     */
    <T> T deserialize(String string, ObjectTypeReference<T> type);
}
