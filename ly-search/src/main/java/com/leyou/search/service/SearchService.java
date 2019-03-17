package com.leyou.search.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.search.client.item.BrandClient;
import com.leyou.search.client.item.CategoryClient;
import com.leyou.search.client.item.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.util.SearchAppConstans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;


    /**
     * 使用Spu数据构建索引库Goods结构
     *
     * @param spu 商品数据
     * @return Goods
     */
    public Goods buildGoods(Spu spu) {
        Goods goods = new Goods();
        // 基本数据处理
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setId(spu.getId());

        try {
            // 处理搜索字段 all
            goods.setAll(buildAll(spu));

            // 处理SkuList
            List<Sku> skuList = goodsClient.querySkuListById(spu.getId());
            if (skuList == null || skuList.isEmpty()) {
                throw new LyException(LyExceptionEnum.SKU_LIST_NOT_FOUND);
            }
            goods.setSkus(buildSkus(skuList));

            // 处理price
            goods.setPrice(skuList.stream().map(Sku::getPrice).collect(Collectors.toList()));

            // 处理规格
            goods.setSpecs(buildSpecs(spu));
        } catch (Exception e) {
            log.error("error message = [{}], spuId = [{}]", e.getMessage(), spu.getId());
            return null;
        }

        return goods;
    }

    /**
     * 处理规格信息
     */
    private Map<String, Object> buildSpecs(Spu spu) {
        // 获取Spu的规格参数
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        if (spuDetail == null) {
            throw new LyException(LyExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        JSONArray spuSpec = JSON.parseArray(spuDetail.getSpecifications());

        // 封装规格索引信息
        Map<String, Object> result = new HashMap<>();
        // 遍历需要索引的key，并获取相应的值
        for (int i = 0; i < spuSpec.size(); i++) {
            JSONArray specParams = spuSpec.getJSONObject(i).getJSONArray("params");
            specParams.forEach(sp -> {
                JSONObject specParam = (JSONObject) sp;
                // 查看是否需要索引，需要则添加到索引规格Map中
                if (specParam.getBoolean("searchable") && specParam.containsKey("v")) {
                    result.put(specParam.getString("k"), specParam.get("v"));
                } else if (specParam.getBoolean("searchable") && specParam.containsKey("options")) {
                    JSONArray options = specParam.getJSONArray("options");
                    result.put(specParam.getString("k"), options != null && options.isEmpty() ? "" : options.get(0));
                }
            });
        }

        return result;
    }

    /**
     * 处理skus信息和价格信息
     */
    private String buildSkus(List<Sku> skuList) {
        List<Map<String, Object>> skus = new ArrayList<>();
        // 获取需要的字段并封装到map
        skuList.forEach(sku -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));

            skus.add(map);
        });
        return JsonUtils.serialize(skus);
    }

    /**
     * 处理搜索字段
     */
    private String buildAll(Spu spu) {
        StringBuilder stringBuilder = new StringBuilder(spu.getTitle());

        // 分类信息
        List<String> categoryNames = categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        stringBuilder.append(StringUtils.join(categoryNames, " "));

        // 品牌信息
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
        }
        stringBuilder.append(brand.getName());

        return stringBuilder.toString();
    }

    /**
     * 分页查询goods集合
     *
     * @param request 搜索参数
     * @return goods分页对象
     */
    public PageResult<Goods> queryByPage(SearchRequest request) {
        // 获取分页参数，且elasticsearch页码从0开始，需要减1
        int page = request.getPage() - 1;
        int size = request.getSize();

        // 分页查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));
        // 结果过滤
        nativeSearchQueryBuilder.withSourceFilter(
                new FetchSourceFilter(new String[]{SearchAppConstans.FIELD_ID, SearchAppConstans.FIELD_SUB_TITLE, SearchAppConstans.FIELD_SKUS}, null));
        // 查询方式
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(SearchAppConstans.FIELD_ALL, request.getKey()));

        Page<Goods> goodsPage = goodsRepository.search(nativeSearchQueryBuilder.build());

        return new PageResult<>(goodsPage.getTotalElements(), (long) goodsPage.getTotalPages(), goodsPage.getContent());
    }
}
