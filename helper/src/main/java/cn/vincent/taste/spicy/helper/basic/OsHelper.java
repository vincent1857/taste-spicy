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

package cn.vincent.taste.spicy.helper.basic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 系统环境工具类
 *
 * @author vincent
 * @version 1.0 2017/8/20 03:53
 */
public class OsHelper {

    private static Boolean OS_LINUX = null;

    /**
     * 判断当前系统是否为 linux
     *
     * @return true linux, false windows
     */
    public static boolean isLinux() {
        if (OS_LINUX == null) {
            String osName = System.getProperty("os.name").toLowerCase();
            OS_LINUX = !osName.contains("windows");
        }
        return OS_LINUX;
    }

    /**
     * 返回当前系统变量的函数 结果放至 Properties
     *
     * @return Properties对象
     */
    public static Properties getEnv() {
        Properties prop = new Properties();
        try {
            Process p;
            if (isLinux()) {
                p = Runtime.getRuntime().exec("sh -c set");
            } else {
                // windows
                p = Runtime.getRuntime().exec("cmd /c set");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                int i = line.indexOf("=");
                if (i > -1) {
                    String key = line.substring(0, i);
                    String value = line.substring(i + 1);
                    prop.setProperty(key, value);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
