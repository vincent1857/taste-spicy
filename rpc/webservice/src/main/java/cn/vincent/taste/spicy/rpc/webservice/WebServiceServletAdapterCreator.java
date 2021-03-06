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

package cn.vincent.taste.spicy.rpc.webservice;

import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.Servlet;

/**
 * @author vincent
 * @version 1.0 2017/8/20 06:03
 */
public interface WebServiceServletAdapterCreator {

    /**
     * 创建服务
     *
     * @param servlet 服务
     * @return 控制器
     */
    Controller create(Servlet servlet);
}
