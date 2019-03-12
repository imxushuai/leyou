package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌列表
     *
     * @param page   当前页码
     * @param rows   每页记录数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @param key    查询关键字
     * @return 品牌分页列表
     */
    public PageResult<Brand> queryBrandByPage(int page, int rows, String sortBy, Boolean desc, String key) {
        // 分页查询
        PageHelper.startPage(page, rows);
        // 封装查询条件
        Example example = new Example(Brand.class);
        if (StringUtils.isNoneBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%");
        }
        // 排序
        if (StringUtils.isNoneBlank(sortBy)) {
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        if (brands.size() < 1) {
            throw new LyException(LyExceptionEnum.BRAND_LIST_NOT_FOUND);
        }
        // 获取分页信息并返回
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(), brands);
    }

    /**
     * 新增品牌
     *
     * @param brand      品牌
     * @param categories 品牌所属分类
     * @return 品牌
     */
    public Brand addBrand(Brand brand, List<Long> categories) {
        // 新增品牌
        if (brandMapper.insert(brand) != 1) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        // 添加关联的品牌分类
        insertCategoryBrand(brand, categories);

        return brand;
    }

    /**
     * 编辑品牌
     *
     * @param brand      品牌
     * @param categories 品牌所属分类
     * @return 品牌
     */
    public Brand saveBrand(Brand brand, List<Long> categories) {
        // 更新品牌
        if (brandMapper.updateByPrimaryKeySelective(brand) != 1) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        // 更新关联的品牌分类
        brandMapper.deleteCategoryBrandByBrandId(brand.getId());
        insertCategoryBrand(brand, categories);
        return brand;
    }

    /**
     * 新增品牌分类关联数据(品牌 1 -> n 分类)
     *
     * @param brand      品牌
     * @param categories 分类
     */
    private void insertCategoryBrand(Brand brand, List<Long> categories) {
        categories.forEach(categoryId -> {
            if (brandMapper.insertCategoryBrand(categoryId, brand.getId()) != 1) {
                throw new LyException(LyExceptionEnum.SAVE_FAILURE);
            }
        });
    }

    /**
     * 删除品牌
     *
     * @param brandId 品牌ID
     * @return 被删除的品牌
     */
    public Brand deleteBrand(long brandId) {
        // 查询要删除的品牌
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand == null) {
            throw new LyException(LyExceptionEnum.BRAND_LIST_NOT_FOUND);
        }
        // 删除品牌
        if (brandMapper.delete(brand) != 1) {
            throw new LyException(LyExceptionEnum.DELETE_FAILURE);
        }
        // 删除品牌的关联分类数据
        brandMapper.deleteCategoryBrandByBrandId(brandId);
        return brand;
    }

    /**
     * 按分类ID查询品牌列表
     *
     * @param categoryId 分类ID
     * @return 品牌列表
     */
    public List<Brand> queryBrandByCategoryId(Long categoryId) {
        List<Brand> brands = brandMapper.queryBrandByCategoryId(categoryId);
        if (brands.isEmpty()) {
            throw new LyException(LyExceptionEnum.BRAND_LIST_NOT_FOUND);
        }
        return brands;
    }

    /**
     * 按品牌ID查询品牌
     *
     * @param brandId 品牌
     * @return Brand
     */
    public Brand queryById(long brandId) {
        Brand brand = new Brand();
        brand.setId(brandId);
        return brandMapper.selectOne(brand);
    }
}
