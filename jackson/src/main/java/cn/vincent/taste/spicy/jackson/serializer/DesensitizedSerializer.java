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

package cn.vincent.taste.spicy.jackson.serializer;

import cn.vincent.taste.spicy.helper.character.DesensitizedHelper;
import cn.vincent.taste.spicy.support.annotation.json.JsonSerializerType;
import cn.vincent.taste.spicy.support.annotation.json.JsonValueParse;
import cn.vincent.taste.spicy.support.constant.JsonSerializeNameConstant;
import cn.vincent.taste.spicy.support.json.SensitiveType;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author vincent
 * @version 1.0 2017/8/20 02:23
 */
@Slf4j
@JsonSerializerType(JsonSerializeNameConstant.DESENSITIZED)
public class DesensitizedSerializer extends AbstractJsonSerializer {

    public DesensitizedSerializer(JsonValueParse jsonValueParse) {
        super(jsonValueParse);
    }

    @Override
    protected boolean replace() {
        return true;
    }

    @Override
    protected Object attach(Object value, JsonGenerator jsonGenerator) {
        String[] params = jsonValueParse.params();
        if (params.length == 0) {
            return null;
        }

        SensitiveType sensitiveType = SensitiveType.valueOf(params[0]);
        Object filedDesensitized;
        String v = (value == null ? StringUtils.EMPTY : value.toString());
        switch (sensitiveType) {
            case EMAIL:
                filedDesensitized = DesensitizedHelper.email(v);
                break;
            case ADDRESS:
                filedDesensitized = DesensitizedHelper.address(v, v.indexOf("市"));
                break;
            case ID_CARD:
                filedDesensitized = DesensitizedHelper.idCardNum(v);
                break;
            case PASSWORD:
                filedDesensitized = DesensitizedHelper.password(v);
                break;
            case BANK_CARD:
                filedDesensitized = DesensitizedHelper.bankCard(v);
                break;
            case FIXED_PHONE:
                filedDesensitized = DesensitizedHelper.fixedPhone(v);
                break;
            case CHINESE_NAME_NOMIDDLE:
                filedDesensitized = DesensitizedHelper.chineseMiddleName(v);
                break;
            case CHINESE_NAME_FIRST:
                filedDesensitized = DesensitizedHelper.chineseName(v);
                break;
            case MOBILE_PHONE:
                filedDesensitized = DesensitizedHelper.mobilePhone(v);
                break;
            case ALL:
                filedDesensitized = DesensitizedHelper.allNoCount(v);
                break;
            default:
                filedDesensitized = DesensitizedHelper.all(v);
                break;
        }
        return filedDesensitized;
    }
}
