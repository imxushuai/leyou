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
