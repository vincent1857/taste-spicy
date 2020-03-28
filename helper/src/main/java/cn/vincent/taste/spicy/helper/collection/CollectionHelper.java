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

package cn.vincent.taste.spicy.helper.collection;

import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2017/8/20 03:16
 */
public class CollectionHelper {

    public static String get(Map<String, ?> map, String key) {
        if (map == null || key == null || map.get(key) == null) {
            return null;
        }

        return map.get(key).toString();
    }

    public static Integer getInteger(Map<String, ?> map, String key) {
        if (map == null || key == null || map.get(key) == null) {
            return null;
        }
        try {
            return Integer.parseInt(map.get(key).toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int getInt(Map<String, ?> map, String key) {
        return getInt(map, key, 0);
    }

    public static int getInt(Map<String, ?> map, String key, int defaultValue) {
        if (map == null || key == null || map.get(key) == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(map.get(key).toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Float getFloatWrap(Map<String, ?> map, String key) {
        if (map == null || key == null || map.get(key) == null) {
            return null;
        }
        try {
            return Float.parseFloat(map.get(key).toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static float getFloat(Map<String, ?> map, String key) {
        return getFloat(map, key, 0F);
    }

    public static float getFloat(Map<String, ?> map, String key, float defaultValue) {
        if (map == null || key == null || map.get(key) == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(map.get(key).toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double getDoubleWrap(Map<String, ?> map, String key) {
        if (map == null || key == null || map.get(key) == null) {
            return null;
        }
        try {
            return Double.parseDouble(map.get(key).toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static double getDouble(Map<String, ?> map, String key) {
        return getDouble(map, key, 0D);
    }

    public static double getDouble(Map<String, ?> map, String key, double defaultValue) {
        if (map == null || key == null || map.get(key) == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(map.get(key).toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
