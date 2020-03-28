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

import cn.vincent.taste.spicy.helper.error.code.BusinessErrorCodeEnum;
import cn.vincent.taste.spicy.helper.error.code.ErrorCodeEnum;
import cn.vincent.taste.spicy.helper.error.code.SystemErrorCodeEnum;
import cn.vincent.taste.spicy.helper.exception.BusinessException;
import cn.vincent.taste.spicy.helper.exception.SubjectException;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Map;

/**
 * 验证工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 04:15
 */
@Slf4j
public class ValidationHelper {

    private ValidationHelper(){ }

    private static void throwException(ErrorCodeEnum errorCode, Object... args) {
        if (errorCode instanceof BusinessErrorCodeEnum) {
            throw new BusinessException((BusinessErrorCodeEnum) errorCode, args);
        } else if (errorCode instanceof SystemErrorCodeEnum) {
            throw new SystemException((SystemErrorCodeEnum) errorCode, args);
        } else {
            throw new SubjectException(errorCode, args);
        }
    }

    public static void isTrue(boolean expression, ErrorCodeEnum errorCode, Object... args) {
        if (!expression) {
            throwException(errorCode, args);
        }
    }

    public static void notNull(Object object, ErrorCodeEnum errorCode, Object... args) {
        if (object == null) {
            throwException(errorCode, args);
        }
    }

    public static void notEmpty(Object[] array, ErrorCodeEnum errorCode, Object... args) {
        if (array == null || array.length == 0) {
            throwException(errorCode, args);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCodeEnum errorCode, Object... args) {
        if (collection == null || collection.size() == 0) {
            throwException(errorCode, args);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCodeEnum errorCode, Object... args) {
        if (map == null || map.size() == 0) {
            throwException(errorCode, args);
        }
    }

    public static void notEmpty(String string, ErrorCodeEnum errorCode, Object... args) {
        if (string == null || string.trim().length() == 0) {
            throwException(errorCode, args);
        }
    }

    public static void noNullElements(Object[] array, ErrorCodeEnum errorCode, Object... args) {
        notNull(array, errorCode, args);
        for (Object anArray : array) {
            if (anArray == null) {
                throwException(errorCode, args);
            }
        }
    }

    public static void noNullElements(Collection<?> collection, ErrorCodeEnum errorCode, Object... args) {
        Validate.notNull(collection);
        for (Object aCollection : collection) {
            if (aCollection == null) {
                throwException(errorCode, args);
            }
        }
    }
}
