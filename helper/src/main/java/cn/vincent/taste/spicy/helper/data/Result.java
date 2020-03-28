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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.vincent.taste.spicy.helper.data;

import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vincent
 * @version 1.0 2019/10/23 11:38
 */
@Getter
@Setter
@ToString
public class Result<T> {

    private String code;
    private String message;
    private T data;

    public Result() {
        this.code = SystemErrorCodes.SUCCESS.detail().getCode();
    }

    public Result(T data) {
        this.code = SystemErrorCodes.SUCCESS.detail().getCode();
        this.data = data;
    }

    public static Result<Void> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }
}
