/**
 * Copyright © 2019-Now imxushuai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leyou.api;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 分类对外接口
 */
public interface CategoryApi {
    /**
     * 根据商品分类id查询分类集合
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据商品分类id查询名称
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("category/names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);
}
