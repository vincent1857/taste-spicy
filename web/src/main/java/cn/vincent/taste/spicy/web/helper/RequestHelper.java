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
package cn.vincent.taste.spicy.web.helper;

import cn.vincent.taste.spicy.helper.character.CharsetKit;
import cn.vincent.taste.spicy.helper.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2019-04-26 21:29
 */
@Slf4j
public class RequestHelper {

    private static final String LOCLAIP = "127.0.0.1";
    private static final String GET = "GET";
    private static final String POST = "POST";

    private static final String SOAP = "soap";

    private static final String CONTENT_TYPE_STREAM = "stream";
    private static final String CONTENT_TYPE_MULTIPART = "multipart";
    private static final String CONTENT_TYPE_IMAGE = "image";

    private static final String UNKNOWN = "unknown";

    /**
     * 获取httprequest对象 需要在web.xml中增加一个监听 <listener>
     * <listener-class>org.springframework
     * .web.context.request.RequestContextListener</listener-class> </listener>
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes reqAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (reqAttr != null) {
            return reqAttr.getRequest();
        }
        return null;
    }

    /**
     * getRequestLog:打印请求url日志
     *
     * @param request httprequest对象
     * @param params  参数信息
     * @return 日志拼接字符串
     */
    public static String getRequestLog(HttpServletRequest request, String... params) {
        return getRequestLog(Constant.MARK_COMMA, request, params);
    }

    /**
     * getRequestLog:打印请求url日志
     *
     * @param split   分隔符
     * @param request httprequest对象
     * @param params  参数信息
     * @return 日志拼接字符串
     */
    public static String getRequestLog(String split, HttpServletRequest request, String... params) {
        String rq = "[" + request.getMethod() + "],[" + request.getRequestURL() + (StringUtils.isBlank(request.getQueryString()) ? "" : Constant.MARK_QUESTION + request.getQueryString()) + "]";
        List<String> paramList = Arrays.asList(params);
        if (paramList.size() == 0) {
            return rq;
        }
        return rq.concat(split).concat(StringUtils.join(paramList, split));
    }

    public static boolean isRpcCall(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        // 如果是hessian请求则认为是rpc调用
        if (isHessianRequest(request)) {
            return true;
        }

        // 如果是soap的web service请求则认为是rpc调用
        return isSoapRequest(request);

    }

    /**
     * @param request httprequest对象
     * @return 判断是否为soap调用
     */
    private static boolean isSoapRequest(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }

        // sopa1.2协议中，ContentType为application/soap+xml
        if (contentType.contains(SOAP)) {
            return true;
        }

        // sopa协议中有一个请求头SOAPAction的项，该项可能为空串 因此只判断是否为null
        return request.getHeader("SOAPAction") != null;

    }

    /**
     * @param request httprequest对象
     * @return 判断是否为hessian调用
     */
    private static boolean isHessianRequest(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }

        // Hessian协议的ContentType为x-application/hessian
        return contentType.contains("hessian");

    }

    private static String getFirstValidIp(String ipList) {
        if (ipList == null || ipList.length() == 0) {
            return null;
        }

        String[] ips = ipList.split(",");
        for (String ip : ips) {
            if (StringUtils.isBlank(ip)) {
                continue;
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                continue;
            }

            return ip;
        }

        return null;
    }

    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }

        String ip = getFirstValidIp(request.getHeader("X-Forwarded-For"));

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("X-FORWARDED-FOR"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("Proxy-Client-IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("WL-Proxy-Client-IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("HTTP_CLIENT_IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("HTTP_X_FORWARDED_FOR"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = request.getRemoteAddr();
        } else {
            return ip;
        }

        if (ip != null) {
            return ip.trim();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request 请求对象
     * @return ip地址
     */
    public static String getRequestIps(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCLAIP.equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("IpHelper error." + e.toString(), e);
                }
            }
        }
        return ip;
    }

    /**
     * 获取原始的http请求的内容，主要用于获取web接口中请求内容
     *
     * @param request 请求对象
     * @return 原始请求内容
     */
    public static String getRequestString(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        // 如果是rpc调用，则不获取请求内容，rpc调用请求的内容是特定格式
        if (isRpcCall(request)) {
            return null;
        }

        // 是GET方法则从query string中获取
        String method = request.getMethod();
        if (method == null) {
            method = Constant.EMPTY;
        }

        if (GET.equalsIgnoreCase(method)) {
            return request.getQueryString();
        }

        // 如果是post方法则从请求的body中获取,但需要区分文件上传的 情况
        if (POST.equalsIgnoreCase(method)) {
            try {
                ServletInputStream inputStream = request.getInputStream();
                int length = request.getContentLength();
                if (length <= 0) {
                    return null;
                }

                byte[] bytes = new byte[length];
                int readSize = inputStream.read(bytes);
                if (readSize > 0) {
                    return new String(bytes, 0, readSize);
                } else {
                    return Constant.EMPTY;
                }
            } catch (Throwable t) {
                log.error("get post data body from request input stream fail", t);
            }
        }

        return null;
    }

    public static boolean isMultipart(HttpServletRequest request) {
        if (!isHttpPost(request)) {
            return false;
        }

        String contentType = request.getContentType();
        return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
    }

    public static boolean isHttpPost(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        String method = request.getMethod();
        return "post".equalsIgnoreCase(method);
    }

    /**
     * 检查http请求是否是请求的传入的二进制数据，对于octet-stream，image，multipart文件 都认为是二进制的
     *
     * @param request 请求对象
     * @return true/false
     */
    public static boolean isBinayBodyData(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        contentType = contentType.toLowerCase();

        // 判断Content-Type是否指定为流数据
        if (contentType.contains(CONTENT_TYPE_STREAM)) {
            return true;
        }

        // 判断Content-Type是否指定为文件上传
        if (contentType.contains(CONTENT_TYPE_MULTIPART)) {
            return true;
        }

        // 判断Content-Type是否指定为图片
        return contentType.contains(CONTENT_TYPE_IMAGE);

    }

    public static String getParameterMapString(HttpServletRequest request) {
        if (request == null) {
            return Constant.EMPTY;
        }

        Map<String, String[]> map = request.getParameterMap();

        if (map == null || map.size() <= 0) {
            return Constant.EMPTY;
        }

        StringBuilder sb = new StringBuilder(100);
        // 是否首次拼接
        boolean bfirst = true;
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            for (String item : entry.getValue()) {
                if (!bfirst) {
                    sb.append("&");
                } else {
                    bfirst = false;
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(item);
            }
        }

        return sb.toString();
    }

    /**
     * 获取HttpServletResponse对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes reqAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (reqAttr != null) {
            return reqAttr.getResponse();
        }
        return null;
    }

    /**
     * 输出 图像
     *
     * @param file     文件
     * @param name     文件名
     * @param download 是否下载请求头
     */
    public static void ajaxFile(File file, String name, boolean download) {
        HttpServletResponse response = getResponse();
        assert response != null;
        ajaxFile(file, name, download, response);
    }

    public static void ajaxFile(File file, String name, boolean download, HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        if (download) {
            response.setContentType("application/octet-stream");
        } else {
            response.setContentType("image/jpeg");
        }
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        try {
            name = URLEncoder.encode(name, CharsetKit.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        response.setHeader("Content-Disposition", "filename=".concat(name));

        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)) {
            int len;
            byte[] buffer = new byte[1024 * 1024];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            log.error("ajaxFile error", e);
        }
    }

    /**
     * 输出图像
     *
     * @param buffImg 图形
     */
    public static void ajaxImage(BufferedImage buffImg) {
        HttpServletResponse response = getResponse();
        assert response != null;
        ajaxImage(buffImg, response);
    }

    /**
     * 输出图像
     *
     * @param buffImg  图形
     * @param response 响应流
     */
    public static void ajaxImage(BufferedImage buffImg, HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(buffImg, "jpeg", response.getOutputStream());
        } catch (Exception e) {
            log.error("[ajaxImage] occurs error", e);
        }
    }

    /**
     * 允许 JS 跨域设置
     * <p>
     * <p>
     * <!-- 使用 nginx 注意在 nginx.conf 中配置 --> http { ...... add_header
     * Access-Control-Allow-Origin *; ...... }
     * </p>
     * <p>
     * <p>
     * 非 ngnix 下，如果该方法设置不管用、可以尝试增加下行代码。
     * response.setHeader("Access-Control-Allow-Origin", "*");
     * </p>
     *
     * @param response 响应请求
     */
    public static void allowJsCrossDomain(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    /**
     * 判断请求是否为 AJAX
     *
     * @param request 当前请求
     * @return true/false
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * GET 请求
     *
     * @param request 当前请求
     * @return boolean
     */
    public static boolean isGet(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }

    /**
     * POST 请求
     *
     * @param request 当前请求
     * @return boolean
     */
    public static boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    /**
     * 请求重定向至地址 location
     *
     * @param response 请求响应
     * @param location 重定向至地址
     */
    public static void sendRedirect(HttpServletResponse response, String location) {
        try {
            response.sendRedirect(location);
        } catch (IOException e) {
            log.error("sendRedirect location:" + location, e);
        }
    }

    /**
     * 获取Request Playload 内容
     *
     * @param request 当前请求
     * @return Request Playload 内容
     */
    public static String requestPlayload(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }
}
