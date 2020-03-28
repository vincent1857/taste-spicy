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

package cn.vincent.taste.spicy.helper.attack.istrip;

/**
 * 攻击过滤父类
 *
 * @author vincent
 * @version 1.0 2017/8/20 23:39
 */
public interface Istrip {

    /**
     * 脚本内容剥离
     *
     * @param value 待处理内容
     * @return 剥离后结果字符串
     */
    String strip(String value);
}
