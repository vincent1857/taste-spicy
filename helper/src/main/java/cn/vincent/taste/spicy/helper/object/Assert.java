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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.vincent.taste.spicy.helper.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * 仿照Spring定制的验证类
 *
 * @author vincent
 * @version 1.0 2019-06-14 09:10
 */
public class Assert {

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression, String message, Object... additional) {
        if (!expression) {
            throw new IllegalStateException(message + Arrays.toString(additional));
        }
    }

    public static void isNull(Object object, String message, Object... additional) {
        if (object != null) {
            throw new IllegalArgumentException(message + Arrays.toString(additional));
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message, Object... additional) {
        if (object == null) {
            throw new IllegalArgumentException(message + Arrays.toString(additional));
        }
    }

    public static void hasLength(String text, String message) {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, String message, Object... additional) {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException(message + Arrays.toString(additional));
        }
    }

    public static void equals(Object o1, Object o2, String message) {
        if (!Objects.equals(o1, o2)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void equals(Object o1, Object o2, String message, Object... additional) {
        if (!Objects.equals(o1, o2)) {
            throw new IllegalArgumentException(message + Arrays.toString(additional));
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (null == collection || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message, Object... additional) {
        if (null == collection || collection.isEmpty()) {
            throw new IllegalArgumentException(message + Arrays.toString(additional));
        }
    }
}
