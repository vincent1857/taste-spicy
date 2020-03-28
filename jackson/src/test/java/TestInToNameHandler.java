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

import cn.vincent.taste.spicy.support.json.IdToNameHandler;

/**
 * @author vincent
 * @version 1.0 2019/12/16 20:59
 */
public class TestInToNameHandler implements IdToNameHandler {
    @Override
    public Object idToName(Object id) {
        return "id:" + id;
    }
}
