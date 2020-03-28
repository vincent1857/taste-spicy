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

package cn.vincent.taste.spicy.mybatis.data;

import lombok.*;

import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2019/9/23 22:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页大小 */
    private int pageSize = 15;
    /** 当前页 */
    private int currentPage = 1;
    /** 分页开始 */
    private Integer offset;
    /** 排序字段名 */
    private String sortKey;
    /** 排序方式 */
    private String sortType;

    private Boolean reasonable;
    private Boolean rowBoundsWithCount;
    private boolean pageSizeZero = false;
}
