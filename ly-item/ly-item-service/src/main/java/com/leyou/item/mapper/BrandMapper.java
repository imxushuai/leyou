/**
 * Copyright © 2019-2019 imxushuai
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
package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long>{

    @Insert("INSERT INTO tb_category_brand(brand_id, category_id) VALUES(#{brandId}, #{categoryId})")
    int insertCategoryBrand(@Param("brandId") long brandId, @Param("categoryId") long categoryId);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{brandId}")
    int deleteCategoryBrandByBrandId(@Param("brandId") long brandId);

    @Select("SELECT b.* FROM tb_category_brand cb, tb_brand b WHERE cb.category_id = #{categoryId} AND cb.brand_id = b.id")
    List<Brand> queryBrandByCategoryId(Long categoryId);
}
