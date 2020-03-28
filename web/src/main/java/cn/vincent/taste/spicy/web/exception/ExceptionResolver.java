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

package cn.vincent.taste.spicy.web.exception;

import cn.vincent.taste.spicy.helper.data.Result;
import cn.vincent.taste.spicy.helper.data.ResultHelper;
import cn.vincent.taste.spicy.helper.error.i18n.ErrorCode;
import cn.vincent.taste.spicy.helper.error.i18n.ErrorMessageTranslator;
import cn.vincent.taste.spicy.helper.error.i18n.SimpleErrorMessageTranslator;
import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import cn.vincent.taste.spicy.helper.exception.BizException;
import cn.vincent.taste.spicy.helper.exception.ExceptionDetail;
import cn.vincent.taste.spicy.helper.exception.SystemException;
import cn.vincent.taste.spicy.web.convert.HttpMessageConverterContainer;
import cn.vincent.taste.spicy.web.helper.RequestHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 统一异常处理
 *
 * @author vincent
 * @version 1.0 2019-06-25 15:22
 */
@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver, Ordered {

    public static final String KEY_EXCEPTIONS_PAGE_VIEW = "EXCEPTIONS_PAGE_VIEW";
    private static final String EXCEPTION_VIEW = "exceptions/exception";

    @Setter
    private String defaultErrorView = EXCEPTION_VIEW;
    @Setter
    private boolean loginRedirect = false;
    @Setter
    private String loginUrl = "/login";
    @Setter
    private int defaultResponseCode = -1;
    @Setter
    private ErrorMessageTranslator translator = new SimpleErrorMessageTranslator();
    @Setter
    private int order = 0;
    @Setter
    private List<HttpMessageConverter<?>> messageConverters;
    @Setter
    private boolean useHttpMessageConverterContainer = true;

    private BizException adapterException(Exception ex) {
        BizException exception;
        if (ex instanceof BizException) {
            exception = (BizException) ex;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            exception = new SystemException(SystemErrorCodes.UPLOAD_FILE_TOO_LARGE, ex);
        } else {
            exception = new SystemException(SystemErrorCodes.SYSTEM_ERROR, ex);
        }
        return exception;
    }

    private String getViewName(HttpServletRequest request) {
        Object exceptionView = request.getAttribute(KEY_EXCEPTIONS_PAGE_VIEW);
        if (exceptionView == null || StringUtils.isBlank(exceptionView.toString())) {
            exceptionView = StringUtils.isBlank(this.defaultErrorView) ? EXCEPTION_VIEW : this.defaultErrorView;
        }
        assert exceptionView != null;
        return exceptionView.toString();
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exp) {
        // 处理异常转换
        BizException exception = this.adapterException(exp);
        ExceptionDetail expDetail = exception.getDetail();
        if (expDetail == null) {
            expDetail = new ExceptionDetail();
        }

        ErrorCode errorCode = this.translator.translate(expDetail);
        if (SystemErrorCodes.INVALID_LOGIN_STATUS.detail().eq(errorCode.getCode())) {
            log.warn(RequestHelper.getRequestLog(request, errorCode.toString()));
        } else {
            log.error(RequestHelper.getRequestLog(request, errorCode.toString()), exp);
        }

        int responseCode = this.defaultResponseCode;
        if (responseCode <= 0) {
            responseCode = errorCode.getReponseCode();
        }
        response.setStatus(responseCode);

        if (this.loginRedirect && SystemErrorCodes.INVALID_LOGIN_STATUS.detail().eq(errorCode.getCode())) {
            try {
                response.sendRedirect(loginUrl);
                return new ModelAndView();
            } catch (IOException e) {
                log.error("redirect fail", e);
                return null;
            }
        }

        Result<?> result = ResultHelper.fail(errorCode.getCode(), errorCode.getMessage());

        boolean needConvert = false;
        if (handler != null && handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBodyAnno = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class);
            if (responseBodyAnno == null) {
                responseBodyAnno = AnnotationUtils.findAnnotation(handlerMethod.getMethod().getDeclaringClass(), ResponseBody.class);
            }

            needConvert = (responseBodyAnno != null);
        }

        // 无需httpMessageConvert
        if (!needConvert) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName(this.getViewName(request));
            mv.addObject(result);
            return mv;
        }

        try {
            return handleResponseError(result, request, response);
        } catch (Exception e) {
            log.error("handleResponseError error", e);
            return null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ModelAndView handleResponseError(Result<?> returnValue, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        Class<?> returnValueType = returnValue.getClass();
        List<HttpMessageConverter<?>> messageConverters = this.getMessageConverters();
        for (MediaType acceptedMediaType : acceptedMediaTypes) {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                    messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                    return new ModelAndView();
                }
            }
        }
        if (log.isWarnEnabled()) {
            log.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
        }
        return null;
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters;
        if (this.useHttpMessageConverterContainer) {
            converters = HttpMessageConverterContainer.getConverters();
        } else {
            converters = this.messageConverters;
        }

        if (converters == null || converters.size() == 0) {
            this.useHttpMessageConverterContainer = false;
            StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
            // see SPR-7316
            stringHttpMessageConverter.setWriteAcceptCharset(false);

            this.messageConverters = new ArrayList<>();
            this.messageConverters.add(new ByteArrayHttpMessageConverter());
            this.messageConverters.add(stringHttpMessageConverter);
            try {
                this.messageConverters.add(new SourceHttpMessageConverter<>());
            } catch (Error err) {
                // Ignore when no TransformerFactory implementation is available
            }
            this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

            converters = this.getMessageConverters();
        }

        return converters;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
