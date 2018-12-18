package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
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
        // 获取分页信息并返回
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(), brands);
    }

    /**
     * 新增品牌
     * @param brand 品牌
     * @return 品牌
     */
    public Brand addBrand(Brand brand) {
        return null;
    }

    /**
     * 编辑品牌
     * @param brand 品牌
     * @return 品牌
     */
    public Brand saveBrand(Brand brand) {
        return null;
    }

    /**
     * 删除品牌
     * @param brandId 品牌ID
     * @return 被删除的品牌
     */
    public Brand deleteBrand(long brandId) {
        return null;
    }
}
