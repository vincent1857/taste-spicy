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

package cn.vincent.taste.spicy.helper.constant;

import cn.vincent.taste.spicy.helper.enums.EnumItem;
import cn.vincent.taste.spicy.helper.enums.IEnum;

/**
 * @author vincent
 * @version 1.0 2018-12-26 11:46
 */
public interface GlobalEnums {

    enum ENABLED implements IEnum<Integer> {

        // 启用
        ENABLE(1, "启用"),
        // 停用
        UNABLE(0, "停用");

        private EnumItem<Integer> item;

        ENABLED(int status, String name) {
            this.item = new EnumItem<>(status, name);
        }

        @Override
        public EnumItem<Integer> item() {
            return this.item;
        }
    }

    enum FLAG implements IEnum<Integer> {

        // 真
        TRUE(1, "true"),
        // 假
        FALSE(0, "false");

        private EnumItem<Integer> item;

        FLAG(int status, String name) {
            this.item = new EnumItem<>(status, name);
        }

        @Override
        public EnumItem<Integer> item() {
            return this.item;
        }
    }
}
