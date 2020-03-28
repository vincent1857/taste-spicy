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

package cn.vincent.taste.spicy.spring.configuration;

import cn.vincent.taste.spicy.helper.constant.Constant;
import cn.vincent.taste.spicy.helper.net.RemoteHost;
import cn.vincent.taste.spicy.spring.constant.GlobalConfigurationConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统参数配置
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:38
 */
@Slf4j
public class AppConfiguration extends ReloadableConfiguration {

    private static final String DEFAULT_CONFIG_PATH = "classpath:appconfig";
    private static int DEFAULT_RELOAD_TIMESPAN = 30;

    private static AppConfiguration appConfiguration = null;

    private static int RELOAD_TIMESPAN = DEFAULT_RELOAD_TIMESPAN;
    private static String CONFIG_PATH = DEFAULT_CONFIG_PATH;

    private AppConfiguration() {
        super(CONFIG_PATH, RELOAD_TIMESPAN, null);
    }

    public static AppConfiguration getInstance() {
        if (appConfiguration == null) {
            synchronized (AppConfiguration.class) {
                if (appConfiguration == null) {
                    appConfiguration = new AppConfiguration();
                }
            }
        }
        return appConfiguration;
    }

    public static void setAppConfigPath(String appConfigPath) {
        CONFIG_PATH = appConfigPath;
        appConfiguration = null;
    }

    public static void setAppConfigPath(int reloadTimespan) {
        RELOAD_TIMESPAN = reloadTimespan;
    }

    @Override
    protected void initResource() {
        super.initResource();
        this.loadSysProperties();
        this.loadNetProperties();
    }

    protected void loadSysProperties() {
        // 设置JVM变量
        String cacheTtl = this.get(GlobalConfigurationConstant.CONF_NETWORKADDRESS_CACHE_TTL);
        if (cacheTtl == null || cacheTtl.trim().length() == 0) {
            cacheTtl = "120";
        }
        java.security.Security.setProperty("networkaddress.cache.ttl", cacheTtl);
        java.security.Security.setProperty("sun.net.inetaddr.ttl", cacheTtl);

        String cacheNegativeTtl = this.get(GlobalConfigurationConstant.CONF_NETWORKADDRESS_CACHE_NEGATIVE_TTL);
        if (cacheNegativeTtl == null || cacheNegativeTtl.trim().length() == 0) {
            cacheNegativeTtl = "120";
        }
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", cacheNegativeTtl);
        java.security.Security.setProperty("sun.net.inetaddr.negative.ttl", cacheNegativeTtl);

        String connectionTimeout = this.get(GlobalConfigurationConstant.CONF_SUN_NET_CLIENT_DEFAULTCONNECTTIMEOUT);
        if (connectionTimeout == null || connectionTimeout.trim().length() == 0) {
            connectionTimeout = "60000";
        }
        String readTimeout = this.get(GlobalConfigurationConstant.CONF_SUN_NET_CLIENT_DEFAULTREADTIMEOUT);
        if (readTimeout == null || readTimeout.trim().length() == 0) {
            readTimeout = "60000";
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", connectionTimeout);
        System.setProperty("sun.net.client.defaultReadTimeout", readTimeout);
    }

    protected void loadNetProperties() {
        this.loadNetHttpProxtProperties();
        this.loadNetHttpsProxtProperties();
        this.loadNetFtpProxtProperties();
    }

    protected void loadNetHttpProxtProperties() {
        // http的代理设置
        String httpProxyHost = this.get(GlobalConfigurationConstant.CONF_JVM_HTTP_PROXYHOST);
        String httpProxyPort = this.get(GlobalConfigurationConstant.CONF_JVM_HTTP_PROXYPORT);
        String nonProxyHosts = this.get(GlobalConfigurationConstant.CONF_JVM_HTTP_NONPROXYHOSTS);

        if (StringUtils.isBlank(httpProxyHost)) {
            RemoteHost proxyHost = new RemoteHost(System.getenv("http_proxy"));
            httpProxyHost = proxyHost.getHost();

            if (StringUtils.isBlank(httpProxyPort) && proxyHost.getPort() > 0) {
                httpProxyPort = String.valueOf(proxyHost.getPort());
            }
        }

        if (StringUtils.isBlank(nonProxyHosts)) {
            nonProxyHosts = System.getenv("no_proxy");
            if (StringUtils.isNotBlank(nonProxyHosts)) {
                nonProxyHosts = StringUtils.replaceEachRepeatedly(nonProxyHosts, new String[]{" ", "\t", ","}, new String[]{Constant.EMPTY, Constant.EMPTY, Constant.MARK_VERTICAL});
            }
        }

        log.debug("jvm:http.proxyHost={},http.proxyPort={},http.nonProxyHosts={}", httpProxyHost, httpProxyPort, nonProxyHosts);

        if (StringUtils.isNotBlank(httpProxyHost)) {
            System.getProperties().setProperty("http.proxyHost", httpProxyHost);
        }

        if (StringUtils.isNotBlank(httpProxyPort)) {
            System.getProperties().setProperty("http.proxyPort", httpProxyPort);
        }

        if (StringUtils.isNotBlank(nonProxyHosts)) {
            System.getProperties().setProperty("http.nonProxyHosts", nonProxyHosts);
        }
    }

    protected void loadNetHttpsProxtProperties() {
        // https的代理设置
        String httpsProxyHost = this.get(GlobalConfigurationConstant.CONF_JVM_HTTPS_PROXYHOST);
        String httpsProxyPort = this.get(GlobalConfigurationConstant.CONF_JVM_HTTPS_PROXYPORT);

        if (StringUtils.isBlank(httpsProxyHost)) {
            RemoteHost proxyHost = new RemoteHost(System.getenv("https_proxy"));
            httpsProxyHost = proxyHost.getHost();

            if (StringUtils.isBlank(httpsProxyPort) && proxyHost.getPort() > 0) {
                httpsProxyPort = String.valueOf(proxyHost.getPort());
            }
        }

        log.debug("jvm:https.proxyHost={},https.proxyPort={}", httpsProxyHost, httpsProxyPort);

        if (StringUtils.isNotBlank(httpsProxyHost)) {
            System.getProperties().setProperty("https.proxyHost", httpsProxyHost);
        }

        if (StringUtils.isNotBlank(httpsProxyPort)) {
            System.getProperties().setProperty("https.proxyPort", httpsProxyPort);
        }
    }

    protected void loadNetFtpProxtProperties() {
        // ftp的代理设置
        String ftpProxyHost = this.get(GlobalConfigurationConstant.CONF_JVM_FTP_NONPROXYHOSTS);
        String ftpProxyPort = this.get(GlobalConfigurationConstant.CONF_JVM_FTP_PROXYPORT);
        String ftpnonProxyHosts = this.get(GlobalConfigurationConstant.CONF_JVM_FTP_NONPROXYHOSTS);

        if (StringUtils.isBlank(ftpProxyHost)) {
            RemoteHost proxyHost = new RemoteHost(System.getenv("ftp_proxy"));
            ftpProxyHost = proxyHost.getHost();

            if (StringUtils.isBlank(ftpProxyPort) && proxyHost.getPort() > 0) {
                ftpProxyPort = String.valueOf(proxyHost.getPort());
            }
        }

        if (StringUtils.isBlank(ftpnonProxyHosts)) {
            ftpnonProxyHosts = System.getenv("no_proxy");
            if (StringUtils.isNotBlank(ftpnonProxyHosts)) {
                ftpnonProxyHosts = StringUtils.replaceEachRepeatedly(ftpnonProxyHosts, new String[]{" ", "\t", ","}, new String[]{Constant.EMPTY, Constant.EMPTY, Constant.MARK_VERTICAL});
            }
        }

        log.debug("jvm:ftp.proxyHost={},ftp.proxyPort={},ftp.nonProxyHosts={}", ftpProxyHost, ftpProxyPort, ftpnonProxyHosts);

        if (StringUtils.isNotBlank(ftpProxyHost)) {
            System.getProperties().setProperty("ftp.proxyHost", ftpProxyHost);
        }

        if (StringUtils.isNotBlank(ftpProxyPort)) {
            System.getProperties().setProperty("ftp.proxyPort", ftpProxyPort);
        }

        if (StringUtils.isNotBlank(ftpnonProxyHosts)) {
            System.getProperties().setProperty("ftp.nonProxyHosts", ftpnonProxyHosts);
        }
    }
}
