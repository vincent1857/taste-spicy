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

/**
 * @author vincent
 * @version 1.0 2019/9/29 14:09
 */
@Slf4j
public class ProjectConfiguration {

    private static String machine;
    private static boolean debug = false;
    private static boolean logPrint = false;

    public static String getMachine() {
        return machine;
    }

    public static void setMachine(String machine) {
        ProjectConfiguration.machine = machine;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        ProjectConfiguration.debug = debug;
    }

    public static boolean isLogPrint() {
        return logPrint;
    }

    public static void setLogPrint(boolean logPrint) {
        ProjectConfiguration.logPrint = logPrint;
    }
}
