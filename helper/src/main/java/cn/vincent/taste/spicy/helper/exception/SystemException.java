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

package cn.vincent.taste.spicy.helper.exception;

import cn.vincent.taste.spicy.helper.error.code.SystemErrorCodeEnum;
import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;

import java.io.Serializable;

/**
 * 统一业务异常实现
 *
 * @author vincent
 * @version 1.0 2017/8/20 02:04
 */
public class SystemException extends RuntimeException implements BizException {

    private static final long serialVersionUID = 1L;

    /** 错误信息 */
    private ExceptionDetail detail;

    public SystemException() {
        super(SystemErrorCodes.SYSTEM_ERROR.detail().getMessage());
        this.detail = new ExceptionDetail();
    }

    public SystemException(Throwable cause) {
        super(SystemErrorCodes.SYSTEM_ERROR.detail().getMessage());
        this.detail = new ExceptionDetail();
    }

    public SystemException(SystemErrorCodeEnum error) {
        super(error.detail().getMessage());
        this.detail = error.detail().toErrorCode();
    }

    public SystemException(SystemErrorCodeEnum error, Throwable cause) {
        super(error.detail().getMessage(), cause);
        this.detail = error.detail().toErrorCode();
    }

    public SystemException(SystemErrorCodeEnum error, Serializable... args) {
        super(error.detail().toErrorCode(args).getMessage());
        this.detail = error.detail().toErrorCode(args);
    }

    public SystemException(SystemErrorCodeEnum error, Throwable cause, Serializable... args) {
        super(error.detail().toErrorCode(args).getMessage(), cause);
        this.detail = error.detail().toErrorCode(args);
    }

    @Override
    public ExceptionDetail getDetail() {
        return this.detail;
    }
}
