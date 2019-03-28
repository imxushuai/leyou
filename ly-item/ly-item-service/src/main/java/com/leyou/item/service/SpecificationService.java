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
package com.leyou.item.service;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
