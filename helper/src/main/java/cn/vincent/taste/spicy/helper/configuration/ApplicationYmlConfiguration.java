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

/**
 * @author vincent
 * @version 1.0 2019/9/24 20:29
 */
public class ApplicationYmlConfiguration extends YmlConfiguration {

    private static final String APPLICATION_YML_PATH = "application.yml";

    private static ApplicationYmlConfiguration configuration;
    private static String CONFIG_PATH = APPLICATION_YML_PATH;

    private ApplicationYmlConfiguration() {
        super(CONFIG_PATH);
    }

    public static ApplicationYmlConfiguration getInstance() {
        if (configuration == null) {
            synchronized (ApplicationYmlConfiguration.class) {
                if (configuration == null) {
                    configuration = new ApplicationYmlConfiguration();
                }
            }
        }
        return configuration;
    }

    public static void setConfigPath(String appConfigPath) {
        CONFIG_PATH = appConfigPath;
        configuration = null;
    }
}
