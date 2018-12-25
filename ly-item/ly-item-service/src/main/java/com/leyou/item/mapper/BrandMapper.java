package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand(brand_id, category_id) VALUES(#{brandId}, #{categoryId})")
    int insertCategoryBrand(@Param("brandId") long brandId, @Param("categoryId") long categoryId);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{brandId}")
    int deleteCategoryBrandByBrandId(@Param("brandId") long brandId);
}
