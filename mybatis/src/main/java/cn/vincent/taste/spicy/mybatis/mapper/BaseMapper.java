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

package cn.vincent.taste.spicy.mybatis.mapper;

import cn.vincent.taste.spicy.mybatis.data.Search;

import java.util.List;

/**
 * 基础mapper
 *
 * @author vincent
 * @version 1.0 2017/8/20 00:30
 */
public interface BaseMapper<Entity, PrimaryKey> {

    /**
     * 分页查询
     *
     * @param search 查询对象
     * @return 查询结果
     */
    List<Entity> queryPageList(Search search);

    /**
     * 所有信息
     *
     * @return 所有信息
     */
    List<Entity> queryList();

    /**
     * 根据id查询
     *
     * @param paramPrimaryKey 主键
     * @return 结果详情
     */
    Entity queryById(PrimaryKey paramPrimaryKey);

    /**
     * 根据id查询,锁记录
     *
     * @param paramPrimaryKey 主键
     * @return 结果详情
     */
    Entity lockById(PrimaryKey paramPrimaryKey);

    /**
     * 插入数据
     *
     * @param paramEntity 详情
     * @return 影响行数
     */
    int insert(Entity paramEntity);

    /**
     * 根据主键更新
     *
     * @param paramEntity 详情
     * @return 影响行数
     */
    int updateById(Entity paramEntity);

    /**
     * 根据主键删除
     *
     * @param paramPrimaryKey 主键
     * @return 影响行数
     */
    int deleteById(PrimaryKey paramPrimaryKey);
}
