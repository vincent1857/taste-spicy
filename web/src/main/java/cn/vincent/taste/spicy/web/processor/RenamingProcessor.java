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

package cn.vincent.taste.spicy.web.processor;

import cn.vincent.taste.spicy.spring.SpringApplicationContext;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * @author vincent
 * @version 1.0 2019-07-01 17:50
 */
public class RenamingProcessor extends ServletModelAttributeMethodProcessor {

    private static RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public RenamingProcessor() {
        super(true);
    }

    public RenamingProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        Object target = binder.getTarget();
        ParamNameDataBinder paramNameDataBinder = new ParamNameDataBinder(target, binder.getObjectName());
        if (requestMappingHandlerAdapter == null) {
            requestMappingHandlerAdapter = SpringApplicationContext.getBean(RequestMappingHandlerAdapter.class);
        }

        WebBindingInitializer webBindingInitializer = requestMappingHandlerAdapter.getWebBindingInitializer();
        if (webBindingInitializer == null) {
            throw new IllegalArgumentException("webBindingInitializer is null");
        }

        webBindingInitializer.initBinder(paramNameDataBinder);
        super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
    }
}
