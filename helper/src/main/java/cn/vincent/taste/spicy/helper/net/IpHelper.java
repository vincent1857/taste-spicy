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

import cn.vincent.taste.spicy.helper.character.RegexHelper;
import cn.vincent.taste.spicy.helper.constant.Constant;
import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.object.ValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author vincent
 * @version 1.0 2017/8/20 04:23
 */
@Slf4j
public class IpHelper {

    private static final String LOCAL_IP_STAR_STR = "192.168.;10.0.;172.0.";
    private static final String LOCAL_IP_ZERO = "0.0.0.0";

    private static String LOCAL_IP;
    private static String LOCAL_HOST_NAME;

    private static void reloadLocal() {
        String[] starts = LOCAL_IP_STAR_STR.split(Constant.MARK_SEMICOLON);
        Enumeration<NetworkInterface> netInterfaces;
        String ip = null;
        String hostName = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        String addressIp = address.getHostAddress();
                        for (String string : starts) {
                            if (addressIp.startsWith(string)) {
                                ip = addressIp;
                                hostName = address.getHostName();
                                break;
                            }
                        }

                        if (StringUtils.isNotBlank(ip)) {
                            break;
                        }
                    }
                }

                if (StringUtils.isNotBlank(ip)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("IpUtil error.", e);
        }

        LOCAL_IP = StringUtils.isBlank(ip) ? "127.0.0.1" : ip;
        LOCAL_HOST_NAME = StringUtils.isBlank(hostName) ? "localhost" : hostName;
    }

    public static String getLocalIp() {
        if (LOCAL_IP != null && LOCAL_IP.trim().length() > 0) {
            return LOCAL_IP;
        }
        reloadLocal();
        return LOCAL_IP;
    }

    public static String getLocalHostName() {
        if (LOCAL_HOST_NAME != null && LOCAL_HOST_NAME.trim().length() > 0) {
            return LOCAL_HOST_NAME;
        }
        reloadLocal();
        return LOCAL_HOST_NAME;
    }

    /**
     * @param clientIp 获取到的客户端ip列表
     * @return 获取客户端ip
     */
    public static String getClientIp(String clientIp) {
        String ip = "127.0.0.1";
        if (StringUtils.isNotBlank(clientIp)) {
            ip = StringUtils.trim(StringUtils.split(clientIp, "\\,")[0]);
        }
        return ip;
    }

    public static long ipToLong(String ip) {
        String[] ipSnaps = ip.split("\\.");
        return 256 * 256 * 256 * Long.parseLong(ipSnaps[0]) +
                256 * 256 * Long.parseLong(ipSnaps[1]) +
                256 * Long.parseLong(ipSnaps[2]) +
                Long.parseLong(ipSnaps[3]);
    }

    public static boolean checkIp(String ipRange, String ip) {
        if (StringUtils.isBlank(ipRange) || StringUtils.isBlank(ip) || !ipRange.contains(Constant.MARK_SLASH)) {
            return false;
        }
        ValidationHelper.isTrue(RegexHelper.checkIpAddress(ip), SystemErrorCodes.INVALID_PARAM_FIELD_ERROR, "ip");

        String[] ipRangeSnaps = ipRange.split("/");
        ValidationHelper.isTrue(ipRangeSnaps.length == 2, SystemErrorCodes.INVALID_PARAM_FIELD_ERROR, "ipRange");
        ValidationHelper.isTrue(RegexHelper.checkIpAddress(ipRangeSnaps[0]), SystemErrorCodes.INVALID_PARAM_FIELD_ERROR, "ipRange");
        ValidationHelper.isTrue(NumberUtils.toInt(ipRangeSnaps[1], -1) >= 0, SystemErrorCodes.INVALID_PARAM_FIELD_ERROR, "ipRange");

        if (LOCAL_IP_ZERO.equals(ipRangeSnaps[0])) {
            return true;
        }

        long ipl = ipToLong(ip);
        long iprl = ipToLong(ipRangeSnaps[0]);
        long ml = NumberUtils.toInt(ipRangeSnaps[1]);

        long iplm = ipl >> (32 - ml);
        long iprlm = iprl >> (32 - ml);
        return iplm == iprlm;
    }
}
