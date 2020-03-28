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

package cn.vincent.taste.spicy.spring.constant;

/**
 * 配置文件常量
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:14
 */
public interface GlobalConfigurationConstant {

    /** JVM参数 */
    String CONF_NETWORKADDRESS_CACHE_TTL = "networkaddress.cache.ttl";
    /** JVM参数 */
    String CONF_NETWORKADDRESS_CACHE_NEGATIVE_TTL = "networkaddress.cache.negative.ttl";
    /** JVM参数 */
    String CONF_SUN_NET_CLIENT_DEFAULTCONNECTTIMEOUT = "sun.net.client.defaultConnectTimeout";
    /** JVM参数 */
    String CONF_SUN_NET_CLIENT_DEFAULTREADTIMEOUT = "sun.net.client.defaultReadTimeout";
    /** JVM参数:http代理 */
    String CONF_JVM_HTTP_PROXYHOST = "jvm.http.proxyHost";
    String CONF_JVM_HTTP_PROXYPORT = "jvm.http.proxyPort";
    String CONF_JVM_HTTP_NONPROXYHOSTS = "jvm.http.nonProxyHosts";
    /** JVM参数:https代理 */
    String CONF_JVM_HTTPS_PROXYHOST = "jvm.https.proxyHost";
    String CONF_JVM_HTTPS_PROXYPORT = "jvm.https.proxyPort";
    /** JVM参数:ftp代理 */
    String CONF_JVM_FTP_PROXYHOST = "jvm.ftp.proxyHost";
    /** JVM参数 */
    String CONF_JVM_FTP_PROXYPORT = "jvm.ftp.proxyPort";
    /** JVM参数 */
    String CONF_JVM_FTP_NONPROXYHOSTS = "jvm.ftp.nonProxyHosts";
    /** 配置文件刷新时间 */
    String CONF_SYS_RELOAD_TIMESPAN = "sys.config.reload.timespan";
}
