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

import cn.vincent.taste.spicy.support.data.PageForm;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2019-04-26 21:29
 */
public class PageHelper {

    private static final String REQ_PAGE_SIZE = "pageSize";
    private static final String REQ_CURRENT_PAGE = "currentPage";
    private static final String REQ_OFFSET= "offset";
    private static final String REQ_SORT_KEY = "sortKey";
    private static final String REQ_SORT_TYPE = "sortType";
    private static final String REQ_REASONABLE = "reasonable";
    private static final String REQ_ROWBOUNDSWITHCOUNT = "rowBoundsWithCount";
    private static final String REQ_PAGESIZEZERO = "pageSizeZero";

    public static <T extends Serializable> PageForm<T> toPage(T t) {
        HttpServletRequest request = RequestHelper.getRequest();
        if (request == null) {
            return null;
        }
        int pageSize = NumberUtils.toInt(request.getParameter(REQ_PAGE_SIZE), 15);
        int currentPage = NumberUtils.toInt(request.getParameter(REQ_CURRENT_PAGE), 1);
        int offset = NumberUtils.toInt(request.getParameter(REQ_OFFSET), -1);
        String sortKey = request.getParameter(REQ_SORT_KEY);
        String sortType = request.getParameter(REQ_SORT_TYPE);

        Boolean reasonable = null;
        if (Boolean.TRUE.toString().equalsIgnoreCase(request.getParameter(REQ_REASONABLE)) || Boolean.FALSE.toString().equalsIgnoreCase(request.getParameter(REQ_REASONABLE))) {
            reasonable = BooleanUtils.toBoolean(request.getParameter(REQ_REASONABLE));
        }

        Boolean rowBoundsWithCount = null;
        if (Boolean.TRUE.toString().equalsIgnoreCase(request.getParameter(REQ_ROWBOUNDSWITHCOUNT)) || Boolean.FALSE.toString().equalsIgnoreCase(request.getParameter(REQ_ROWBOUNDSWITHCOUNT))) {
            rowBoundsWithCount = BooleanUtils.toBoolean(request.getParameter(REQ_ROWBOUNDSWITHCOUNT));
        }

        Boolean pageSizeZero = null;
        if (Boolean.TRUE.toString().equalsIgnoreCase(request.getParameter(REQ_PAGESIZEZERO)) || Boolean.FALSE.toString().equalsIgnoreCase(request.getParameter(REQ_PAGESIZEZERO))) {
            pageSizeZero = BooleanUtils.toBoolean(request.getParameter(REQ_PAGESIZEZERO));
        }
        return new PageForm<>(reasonable, rowBoundsWithCount, pageSizeZero, pageSize, currentPage, offset < 0 ? null : offset, sortKey, sortType, t);
    }
}
