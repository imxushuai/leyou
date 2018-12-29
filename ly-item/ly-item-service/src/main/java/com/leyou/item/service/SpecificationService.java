package com.leyou.item.service;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 按分类ID查询规格(此表中分类ID其实为该表的ID)
     *
     * @param categoryId 分类ID
     * @return 规格信息
     */
    public Specification queryById(Long categoryId) {
        return specificationMapper.selectByPrimaryKey(categoryId);
    }

    /**
     * 新增规格
     *
     * @param specification 规格信息
     * @return 规格信息
     */
    @Transactional
    public Specification addSpecification(Specification specification) {
        // 先查询并判断是否有该分类
        if (categoryMapper.selectByPrimaryKey(specification.getCategoryId()) == null) {
            throw new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 新增
        if (specificationMapper.insert(specification) == 1)
            return specification;

        throw new LyException(LyExceptionEnum.SAVE_FAILURE);
    }

    /**
     * 编辑规格
     * @param specification 规格信息
     * @return 规格信息
     */
    public Specification saveSpecification(Specification specification) {
        // 先查询并判断是否有该分类
        if (categoryMapper.selectByPrimaryKey(specification.getCategoryId()) == null) {
            throw new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 编辑
        if (specificationMapper.updateByPrimaryKey(specification) == 1)
            return specification;

        throw new LyException(LyExceptionEnum.SAVE_FAILURE);
    }
}
