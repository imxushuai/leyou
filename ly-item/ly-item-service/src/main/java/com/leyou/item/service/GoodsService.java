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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.BO.SpuBO;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

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

    /**
     * 新增商品
     *
     * @param goods 商品信息
     * @return SPU
     */
    @Transactional
    public Spu addGoods(SpuBO goods) {
        try {
            // 保存SPU
            goods.setSaleable(true); // 上架
            goods.setCreateTime(new Date()); // 新增时间
            goods.setLastUpdateTime(goods.getCreateTime()); // 最后更新时间
            goodsMapper.insert(goods);

            // 保存SPU描述
            SpuDetail spuDetail = goods.getSpuDetail();
            spuDetail.setSpuId(goods.getId()); // 设置ID
            spuDetailMapper.insert(spuDetail);

            // 保存SKU列表
            saveSkuList(goods);
        } catch (Exception e) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        return goods;
    }

    // 保存SKU列表
    private void saveSkuList(SpuBO goods) {
        goods.getSkuList().forEach(sku -> {
            if (sku.getEnable()) {
                // 保存sku信息
                sku.setCreateTime(goods.getCreateTime());
                sku.setSpuId(goods.getId());
                sku.setLastUpdateTime(goods.getCreateTime());
                skuMapper.insert(sku);
                // 保存库存信息
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insert(stock);
            }
        });
    }

    /**
     * 编辑商品
     *
     * @param goods 商品信息
     * @return SPU
     */
    @Transactional
    public Spu saveGoods(SpuBO goods) {
        try {
            // 保存SPU
            goods.setSaleable(true); // 上架
            goods.setLastUpdateTime(new Date()); // 最后更新时间
            goodsMapper.insert(goods);

            // 保存SPU描述
            spuDetailMapper.updateByPrimaryKeySelective(goods.getSpuDetail());
            // 保存SKU列表，需要先删除原先的SKU列表
            Sku sku = new Sku();
            sku.setSpuId(goods.getId());
            skuMapper.delete(sku);
            // 删除库存信息
            List<Sku> skuList = skuMapper.select(sku);
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
            // 保存更新后的数据
            saveSkuList(goods);
        } catch (Exception e) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        return goods;
    }

    /**
     * 按ID查询商品信息
     *
     * @param spuId spuid
     * @return 商品信息
     */
    public SpuBO queryGoodsById(Long spuId) {
        SpuBO spuBO = new SpuBO();
        // 查询spu基本信息
        Spu spu = goodsMapper.selectByPrimaryKey(spuId);
        if (spu == null || spu.getId() == null) {
            throw new LyException(LyExceptionEnum.GOODS_NOT_FOUND);
        }
        BeanUtils.copyProperties(spu, spuBO);
        // 查询商品描述信息
        spuBO.setSpuDetail(spuDetailMapper.selectByPrimaryKey(spuId));
        // 查询商品SKU列表
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        spuBO.setSkuList(skuList);

        return spuBO;
    }

    /**
     * 按spuid查询商品描述
     *
     * @param spuId id
     * @return 描述信息
     */
    public SpuDetail querySpuDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new LyException(LyExceptionEnum.GOODS_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 按spuid查询sku列表
     *
     * @param spuId id
     * @return sku列表
     */
    public List<Sku> querySkuListById(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (skuList.isEmpty()) {
            throw new LyException(LyExceptionEnum.SKU_LIST_NOT_FOUND);
        }
        // 查询每个sku的库存信息
        List<Stock> stockList = stockMapper.selectByIdList(skuList.stream().map(Sku::getId).collect(Collectors.toList()));
        if (stockList.isEmpty()) {
            throw new LyException(LyExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        // 将库存数量与skuId生成map
        Map<Long, Long> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        // 设置库存数量
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));

        return skuList;
    }


    /**
     * 更新商品状态（上下架）
     *
     * @param skuId  skuId
     * @param status 商品状态
     */
    public Sku updateGoodsStatus(Long skuId, Boolean status) {
        // 查询sku
        Sku sku = skuMapper.selectByPrimaryKey(skuId);
        if (sku == null) {
            throw new LyException(LyExceptionEnum.GOODS_NOT_FOUND);
        }
        // 修改商品状态
        sku.setEnable(status);
        // 保存
        skuMapper.updateByPrimaryKey(sku);
        return sku;
    }

    /**
     * 删除商品
     *
     * @param spuId 商品ID
     * @return 被删除的商品
     */
    @Transactional
    public Spu deleteGoods(Long spuId) {
        // 查询spu
        Spu spu = goodsMapper.selectByPrimaryKey(spuId);
        if (spu == null) {
            throw new LyException(LyExceptionEnum.GOODS_NOT_FOUND);
        }
        // 删除spu和spuDetail
        goodsMapper.deleteByPrimaryKey(spuId);
        spuDetailMapper.deleteByPrimaryKey(spuId);

        // 删除sku列表
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        skuMapper.deleteByIdList(ids);
        // 删除库存信息
        stockMapper.deleteByIdList(ids);

        return spu;
    }
}
