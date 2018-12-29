package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.BO.SpuBO;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.GoodsMapper;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.pojo.Spu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询商品列表
     *
     * @param page     当前页
     * @param rows     每页记录数
     * @param sortBy   排序字段
     * @param desc     是否降序
     * @param key      查询关键字
     * @param saleable 是否过滤上下架
     * @return 商品列表
     */
    public PageResult<SpuBO> querySpuByPage(int page, int rows, String sortBy, Boolean desc, String key, Boolean saleable) {
        // 分页
        PageHelper.startPage(page, rows);
        // 封装查询关键字
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }
        // 执行查询
        List<Spu> spuList = goodsMapper.selectByExample(example);
        if (spuList.size() < 1) {
            throw new LyException(LyExceptionEnum.SPU_LIST_NOT_FOUND);
        }
        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);

        // 使用spu集合 构造spuBO集合
        List<SpuBO> spuBOList = pageInfo.getList().stream().map(spu -> {
            SpuBO spuBO = new SpuBO();
            BeanUtils.copyProperties(spu, spuBO);
            // 查询分类名称列表并处理成字符串
            Optional<String> categoryNameString = categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream()
                    .map(Category::getName)
                    .reduce((name1, name2) -> name1 + "/" + name2);
            // 查询品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            if (brand == null) {
                throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
            }
            // 设置分类以及品牌名称
            spuBO.setBname(brand.getName());
            spuBO.setCname(categoryNameString.get());
            return spuBO;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(), spuBOList);
    }
}
