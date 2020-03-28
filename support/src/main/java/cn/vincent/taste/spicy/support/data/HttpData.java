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

package cn.vincent.taste.spicy.support.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2019/9/23 21:10
 */
@Getter
@Setter
@ToString
public class HttpData implements Serializable {

    private static final long serialVersionUID = 364279869314183596L;

    /** 请求唯一序号 */
    private String serialNo;
    /** 请求开始记录时间戳 */
    private long startTime;
    /** 客户端IP */
    private String clientIp;
    /** 客户端host */
    private String clientHost;
}
