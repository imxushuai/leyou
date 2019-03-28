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

import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 品牌对外接口
 */
public interface BrandApi {
    /**
     * 查询指定ID的品牌
     *
     * @param brandId 品牌ID
     * @return Brand
     */
    @GetMapping("brand/{brandId}")
    Brand queryBrandById(@PathVariable("brandId") long brandId);

    /**
     * 查询指定ID的品牌
     *
     * @param ids 品牌ID集合
     * @return Brand
     */
    @GetMapping("brand/ids")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
