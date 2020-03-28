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

package cn.vincent.taste.spicy.helper.object;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vincent
 * @version 1.0 2019/9/23 15:22
 */
public class DeepCopyHelper {

    private DeepCopyHelper() {
    }

    /**
     * 单个对象的深拷贝，srcObj对应的需实现java.io.Serializable接口
     *
     * @param srcObj obj
     * @return new  obj
     */
    public static <T extends Serializable> Object depthClone(T srcObj) {
        Object cloneObj;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(srcObj);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(in);
            cloneObj = oi.readObject();
        } catch (Exception ex) {
            return null;
        }
        return cloneObj;
    }

    /**
     * 多个对象的深拷贝，srcObj对应的需实现java.io.Serializable接口
     *
     * @param list obj
     * @return new list obj
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> listDepthClone(List<T> list) {
        List<T> newList = new ArrayList<>();
        for (T item : list) {
            if (item == null) {
                continue;
            }
            Object val = depthClone(item);
            if (val != null) {
                newList.add((T) val);
            }
        }
        return newList;
    }
}
