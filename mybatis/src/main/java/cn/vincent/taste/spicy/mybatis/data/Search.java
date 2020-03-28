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

import cn.vincent.taste.spicy.helper.convert.BeanConvert;
import cn.vincent.taste.spicy.helper.convert.BeanToMapConvert;
import cn.vincent.taste.spicy.support.data.PageData;
import cn.vincent.taste.spicy.support.data.PageForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2019/9/23 22:13
 */
@Slf4j
@Getter
@Setter
@ToString
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_PAGE_SIZE = 1000;

    /**
     * 分页信息
     */
    private Page page;
    /**
     * 查询条件
     */
    private Map<String, Object> search = new HashMap<>();

    public static <T> Search toSearch(T t) {
        Search search = new Search();
        if (t instanceof PageForm) {
            PageForm pageForm = (PageForm) t;
            Page page = new Page();
            if (pageForm.getPageSize() != null && pageForm.getPageSize() >= 0) {
                if (pageForm.getPageSize() <= MAX_PAGE_SIZE) {
                    page.setPageSize(pageForm.getPageSize());
                } else {
                    page.setPageSize(MAX_PAGE_SIZE);
                }
            }
            if (pageForm.getCurrentPage() != null && pageForm.getCurrentPage() > 0) {
                page.setCurrentPage(pageForm.getCurrentPage());
            }
            if (pageForm.getOffset() != null && pageForm.getOffset() > 0) {
                page.setOffset(pageForm.getOffset());
            }
            page.setSortKey(pageForm.getSortKey());
            page.setSortType(pageForm.getSortType());
            page.setRowBoundsWithCount(pageForm.getRowBoundsWithCount());
            page.setReasonable(pageForm.getReasonable());
            if (pageForm.getPageSizeZero() != null) {
                page.setPageSizeZero(pageForm.getPageSizeZero());
            }

            search.setPage(page);

            PageHelper.startPage(page.getCurrentPage(), page.getPageSize(),
                    page.getRowBoundsWithCount() == null || page.getRowBoundsWithCount(),
                    page.getReasonable(), page.isPageSizeZero());
        }
        try {
            Map<String, Object> searchMap = BeanToMapConvert.convertBean(t);
            search.setSearch(searchMap);
        } catch (Exception e) {
            log.error("transeform search error", e);
        }
        return search;
    }

    public <T extends Serializable> PageData<T> toData(List<T> data) {
        PageInfo<T> pages = new PageInfo<>(data);
        return new PageData<>(pages.getPageSize(), pages.getPageNum(), pages.getPages(), pages.getTotal(), data);
    }

    public <T1 extends Serializable, T2 extends Serializable> PageData<T2> toData(List<T1> data, Class<T2> clazz) {
        PageInfo<T1> pages = new PageInfo<>(data);
        List<T2> list = BeanConvert.copyListProperties(data, clazz);
        return new PageData<>(pages.getPageSize(), pages.getPageNum(), pages.getPages(), pages.getTotal(), list);
    }

    public <T1 extends Serializable, T2 extends Serializable> PageData<T2> toFilterData(List<T1> data, Class<T2> clazz, String... argNames) {
        PageInfo<T1> pages = new PageInfo<>(data);
        List<T2> list = BeanConvert.copyListFilterProperties(data, clazz, argNames);
        return new PageData<>(pages.getPageSize(), pages.getPageNum(), pages.getPages(), pages.getTotal(), list);
    }
}
