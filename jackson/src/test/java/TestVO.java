/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.vincent.taste.spicy.support.annotation.json.JsonValueParse;
import cn.vincent.taste.spicy.support.constant.JsonSerializeNameConstant;
import cn.vincent.taste.spicy.support.json.JsonScope;
import cn.vincent.taste.spicy.support.json.SensitiveType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2017/8/20 2018/10/12 02:52
 */
@Getter
@Setter
@ToString
public class TestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonValueParse(type = JsonSerializeNameConstant.ID_TO_NAME, scopes = JsonScope.ALL, params = "test")
    private String id;

    @JsonValueParse(type = JsonSerializeNameConstant.DESENSITIZED, params = SensitiveType.CHINESE_NAME_NOMIDDLE_STRING)
    private String nameDesc;
}
