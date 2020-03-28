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

import lombok.*;

import java.io.Serializable;

/**
 * 分页参数
 *
 * @author vincent
 * @version 1.0 2017/8/20 15:33
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageForm<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean reasonable;
    private Boolean rowBoundsWithCount;
    private Boolean pageSizeZero;

    /** 页大小 */
    private Integer pageSize;
    /** 当前页 */
    private Integer currentPage;
    /** 分页开始 */
    private Integer offset;
    /** 排序字段名 */
    private String sortKey;
    /** 排序方式 */
    private String sortType;
    /** 参数对象 */
    private T form;
}