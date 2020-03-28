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

package cn.vincent.taste.spicy.helper.net;

import cn.vincent.taste.spicy.helper.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 主机信息或者是访问信息
 *
 * @author vincent
 * @version 1.0 2017/8/20 02:34
 */
@Slf4j
@Getter
@Setter
public class RemoteHost implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PROTOCOL_URL_SPLIT = "://";
    private static final String PROTOCOL_SPLIT = ":";
    private static final int HOST_LENGTH = 2;

    private String host;
    private String protocol;
    private int port = -1;

    /**
     * @param hostPath 用host字串配置信息初始化，内部再分割
     */
    public RemoteHost(String hostPath) {
        try {
            parseHost(hostPath);
        } catch (URISyntaxException e) {
            log.error("fail to parse host info:" + hostPath, e);
        }
    }

    public RemoteHost(String host, String protocol, int port) {
        this.host = host;
        this.protocol = protocol;
        this.port = port;
    }

    /**
     * 解析字串中的host信息
     *
     * @param hostPath String 形如 http://localhost:8080
     */
    protected void parseHost(String hostPath) throws URISyntaxException {
        if (hostPath == null || hostPath.trim().length() == 0) {
            return;
        }
        // 去除空格
        hostPath = StringUtils.replaceEachRepeatedly(hostPath, new String[]{" ", "\t"}, new String[]{Constant.EMPTY, Constant.EMPTY});
        // 如果是以://标记的则按URI解析
        if (hostPath.contains(PROTOCOL_URL_SPLIT)) {
            URI uri = new URI(hostPath);
            this.host = uri.getHost();
            this.port = uri.getPort();
            this.protocol = uri.getScheme();
        } else {
            String[] infos = hostPath.split(PROTOCOL_SPLIT);
            this.host = infos[0];
            if (infos.length >= HOST_LENGTH) {
                this.port = Integer.parseInt(infos[1]);
            }
        }
    }

    /**
     * 根据请求url获取域名
     *
     * @param url String 请求url
     * @return 域名
     */
    public static String host(String url) {
        return new RemoteHost(url).getHost();
    }

    public static RemoteHost instance(String url) {
        return new RemoteHost(url);
    }

    public static RemoteHost domain(String url) {
        String subUrl = url;
        String protocol = null;
        if (url.contains(PROTOCOL_URL_SPLIT)) {
            protocol = url.substring(0, url.indexOf("://"));
            subUrl = url.substring(url.indexOf("://") + 3);
        }

        String host;
        int port = -1;
        if (subUrl.contains(Constant.MARK_COLON)) {
            host = subUrl.substring(0, subUrl.indexOf(Constant.MARK_COLON));
            if (subUrl.contains(Constant.MARK_SLASH)) {
                port = NumberUtils.toInt(subUrl.substring(subUrl.indexOf(Constant.MARK_COLON) + 1, subUrl.indexOf(Constant.MARK_SLASH)), -1);
            } else {
                port = NumberUtils.toInt(subUrl.substring(subUrl.indexOf(Constant.MARK_COLON) + 1), -1);
            }
        } else {
            if (subUrl.contains(Constant.MARK_SLASH)) {
                host = subUrl.substring(0, subUrl.indexOf(Constant.MARK_SLASH));
            } else {
                host = subUrl;
            }
        }

        return new RemoteHost(host, protocol, port);
    }

    public static void main(String[] args) {
        RemoteHost remoteHost = RemoteHost.domain("*.bbb.cn/sdfhe.sdfe?dsfrf");
        System.out.println(remoteHost.protocol);
        System.out.println(remoteHost.host);
        System.out.println(remoteHost.port);
    }
}
