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

package cn.vincent.taste.spicy.helper.enums;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2017/8/20 02:42
 */
@Getter
public class EnumItem<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final T key;
    private final String name;

    public EnumItem(T key, String name){
        this.key = key;
        this.name = name;
    }
}
