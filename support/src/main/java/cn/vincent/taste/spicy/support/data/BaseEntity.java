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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2017/8/20 15:36
 */
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> conditions = new HashMap<>();

    /**
     * 查询开始时间
     */
    private Date queryStartTime;
    /**
     * 查询结束时间
     */
    private Date queryEndTime;
    /**
     * 查询用户
     */
    private Long queryUser;

    /**
     * 创建用户
     */
    private Long createUser;
    /**
     * 创建用户名称
     */
    private String createUserName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后一次更新用户id
     */
    private Long updateUser;
    /**
     * 最后一次更新用户
     */
    private String updateUserName;
    /**
     * 最后一次更新时间
     */
    private Date updateTime;
    /**
     * 物理标识
     */
    private Boolean deleted;

    /**
     * 添加参数值.
     *
     * @param key   参数的键
     * @param param 参数值
     */
    public void addCondition(String key, Object param) {
        this.conditions.put(key, param);
    }

    /**
     * 取得参数值
     *
     * @param key 参数的键
     * @return 存储的值
     */
    public Object readCondition(String key) {
        return this.conditions.get(key);
    }

    /**
     * 删除参数值
     *
     * @param key 参数的键
     * @return 存储的值
     */
    public Object removeCondition(String key) {
        return this.conditions.remove(key);
    }
}
