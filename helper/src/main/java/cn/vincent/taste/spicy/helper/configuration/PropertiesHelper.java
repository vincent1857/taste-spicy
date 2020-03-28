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

package cn.vincent.taste.spicy.helper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 00:24
 */
@Slf4j
public class PropertiesHelper {

    private PropertiesHelper() {
    }

    /**
     * 处理key为大写并去掉下划线
     *
     * @param key String 需要处理的key
     * @return 去掉下划线之后的key
     */
    public static String processKey(String key) {
        return key.toUpperCase().replaceAll("_", "");
    }

    /**
     * 处理Map中的键值为大写并去掉下划线
     *
     * @param map 键值对
     * @return 处理后的map
     */
    public static Map<String, String> processMapKey(Map<String, String> map) {
        Map<String, String> result = new HashMap<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(processKey(entry.getKey()), entry.getValue());
        }
        return result;
    }

    /**
     * 加载属性文件
     *
     * @param filePath String 属性文件路径
     * @return Properties对象
     * @throws IOException 异常
     */
    public static Properties loadPropertiesFile(String filePath) throws IOException {
        InputStream is = PropertiesHelper.class.getClassLoader().getResourceAsStream(filePath);
        return loadPropertiesFile(is, true);
    }

    /**
     * 加载属性文件
     *
     * @param is    InputStream 文件流
     * @param close boolean 是否需要关闭流
     * @return Properties对象
     * @throws IOException 异常
     */
    public static Properties loadPropertiesFile(InputStream is, boolean close) throws IOException {
        Properties prop;
        try {
            prop = new Properties();
            prop.load(is);
        } finally {
            if (close) {
                if (is != null) {
                    is.close();
                }
            }
        }

        return prop;
    }

    /**
     * 转换Properties为Map对象
     *
     * @param prop Properties 属性对象
     * @return map对象
     */
    public static Map<String, String> convertToMap(Properties prop) {
        if (prop == null) {
            return null;
        }

        Map<String, String> result = new HashMap<>(16);
        for (Object eachKey : prop.keySet()) {
            if (eachKey == null || StringUtils.isBlank(eachKey.toString())) {
                continue;
            }

            String key = eachKey.toString();
            String value = (String) prop.get(key);
            result.put(key, value);
            log.info("[load property]:[key={};value={}]", key, value);
        }
        return result;
    }

    /**
     * 加载属性文件并且转成map
     *
     * @param filePath String 文件路径
     * @return map对象
     */
    public static Map<String, String> loadValue(String filePath) {
        try {
            Properties prop = loadPropertiesFile(filePath);
            return convertToMap(prop);
        } catch (Exception e) {
            log.error("can not load properties file " + filePath, e);
        }

        return null;
    }

    /**
     * 加载属性文件流并且转成map
     *
     * @param is    InputStream 文件流
     * @param close boolean 是否需要关闭流
     * @return map对象
     */
    public static Map<String, String> loadValue(InputStream is, boolean close) {
        try {
            Properties prop = loadPropertiesFile(is, close);
            return convertToMap(prop);
        } catch (Exception e) {
            log.error("can not load properties InputStream", e);
        }

        return null;
    }

    /**
     * 将map转换成javabean
     *
     * @param map   键值对
     * @param clazz 需要转换的类
     * @param <T>   泛型
     */
    public static <T> void convertToClass(Map<String, String> map, Class<T> clazz) {
        try {
            if (null == map || map.size() == 0 || clazz == null) {
                return;
            }
            T instance = clazz.newInstance();

            Map<String, String> newMap = processMapKey(map);
            Field[] fields = clazz.getDeclaredFields();
            Object value;
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                value = newMap.get(field.getName().toUpperCase());
                if (value == null) {
                    continue;
                }

                field.setAccessible(true);
                field.set(instance, value);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            log.error("conver for class " + clazz.getName() + " error", e);
        }
    }

    public static <T> T convertToBean(Map<String, String> map, Class<T> clazz) {
        try {
            if (null == map || map.size() == 0 || clazz == null) {
                return null;
            }
            T instance = clazz.newInstance();

            Map<String, String> newMap = processMapKey(map);
            Field[] fields = clazz.getDeclaredFields();
            Object value;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                value = newMap.get(field.getName().toUpperCase());
                if (value == null) {
                    continue;
                }

                field.setAccessible(true);
                field.set(instance, value);
            }
            return instance;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            log.error("conver for class " + clazz.getName() + " error", e);
        }
        return null;
    }

    /**
     * 加载属性文件并且转成javabean
     *
     * @param filePath 属性文件路径
     * @param clazz    需要转换的类
     * @param <T>      泛型
     * @return map对象
     */
    public static <T> Map<String, String> loadValue(String filePath, Class<T> clazz) {
        Map<String, String> map = loadValue(filePath);
        convertToClass(map, clazz);
        return map;
    }
}
