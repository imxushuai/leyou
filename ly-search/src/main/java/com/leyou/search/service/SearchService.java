package com.leyou.search.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.JsonUtils;
import com.leyou.pojo.*;
import com.leyou.search.client.item.BrandClient;
import com.leyou.search.client.item.CategoryClient;
import com.leyou.search.client.item.GoodsClient;
import com.leyou.search.client.item.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.util.SearchAppConstans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.repository.support.PageableExecutionUtils;
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
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;


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
                    result.put(specParam.getString("k"), specParam.getString("v"));
                } else if (specParam.getBoolean("searchable") && specParam.containsKey("options")) {
                    JSONArray options = specParam.getJSONArray("options");
                    result.put(specParam.getString("k"), options != null && options.isEmpty() ? "" : options.getString(0));
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
    public SearchResult queryByPage(SearchRequest request) {
        // 获取分页参数，且elasticsearch页码从0开始，需要减1
        int page = request.getPage() - 1;
        int size = request.getSize();


        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 查询方式
//        QueryBuilder queryBuilder = QueryBuilders.matchQuery(SearchAppConstans.FIELD_ALL, request.getKey());
        QueryBuilder queryBuilder = buildBasicQuery(request);

        nativeSearchQueryBuilder.withQuery(queryBuilder);
        // 结果过滤
        nativeSearchQueryBuilder.withSourceFilter(
                new FetchSourceFilter(new String[]{SearchAppConstans.FIELD_ID, SearchAppConstans.FIELD_SUB_TITLE, SearchAppConstans.FIELD_SKUS}, null));
        // 分页查询
        PageRequest pageRequest = PageRequest.of(page, size);
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));

        // 聚合分类以及品牌
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms(SearchAppConstans.AGGREGATION_CATEGORY).field(SearchAppConstans.FIELD_CID_3));
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms(SearchAppConstans.AGGREGATION_BRAND).field(SearchAppConstans.FIELD_BRAND_ID));


        AggregatedPage<Goods> searchResult = template.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);

        // 分页结果
        Page<Goods> pageResult = PageableExecutionUtils.getPage(searchResult.getContent(), pageRequest, searchResult::getTotalElements);

        // 获取聚合结果
        Aggregations aggregations = searchResult.getAggregations();
        List<Category> categories = getCategoryAgg(aggregations.get(SearchAppConstans.AGGREGATION_CATEGORY));
        List<Brand> brands = getBrandAgg(aggregations.get(SearchAppConstans.AGGREGATION_BRAND));

        // 聚合规格参数
        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
            specs = getSpecsAgg(queryBuilder, categories.get(0));
        }

        return new SearchResult(pageResult.getTotalElements(), (long) pageResult.getTotalPages(),
                pageResult.getContent(), categories, brands, specs);
    }

    /**
     * 生成查询条件
     */
    private QueryBuilder buildBasicQuery(SearchRequest request) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        // 基础查询条件
        queryBuilder.must(QueryBuilders.matchQuery(SearchAppConstans.FIELD_ALL, request.getKey()));
        // 过滤查询条件
        Map<String, String> filter = request.getFilter();
        filter.forEach((key, value) -> {
            // 处理key
            if (!key.equals(SearchAppConstans.FIELD_CID_3) && !key.equals(SearchAppConstans.FIELD_BRAND_ID)) {
                key = SearchAppConstans.FIELD_SPECS + "." + key + ".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key, value));
        });

        return queryBuilder;
    }

    /**
     * 聚合规格参数
     */
    private List<Map<String, Object>> getSpecsAgg(QueryBuilder queryBuilder, Category category) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 查询规格参数
        String specString = specificationClient.querySpecificationByCategoryId(category.getId());
        JSONArray spec = JSON.parseArray(specString);

        // 遍历需要过滤的key
        List<String> searchableKeyList = new ArrayList<>();
        for (int i = 0; i < spec.size(); i++) {
            JSONArray specParams = spec.getJSONObject(i).getJSONArray("params");
            specParams.forEach(sp -> {
                JSONObject specParam = (JSONObject) sp;
                // 查看是否需要索引，需要则添加到索引规格Map中
                if (specParam.getBoolean("searchable")) {
                    searchableKeyList.add(specParam.getString("k"));
                }
            });
        }

        // 聚合需要过滤的规格参数
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        searchableKeyList.forEach(key -> nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms(key).field(SearchAppConstans.FIELD_SPECS + "." + key + ".keyword")));

        // 获取聚合结果
        Aggregations aggregations = template.queryForPage(nativeSearchQueryBuilder.build(), Goods.class).getAggregations();

        // 解析聚合结果
        searchableKeyList.forEach(key -> {
            StringTerms aggregation = aggregations.get(key);
            List<String> valueList = aggregation.getBuckets().stream()
                    .map(StringTerms.Bucket::getKeyAsString)
                    .filter(StringUtils::isNoneBlank)
                    .collect(Collectors.toList());
            // 放入结果集
            Map<String, Object> map = new HashMap<>();
            map.put("k", key);
            map.put("options", valueList);
            specs.add(map);
        });

        return specs;
    }

    /**
     * 获取分类聚合结果
     *
     * @param aggregation 聚合结果
     * @return 分类集合
     */
    private List<Category> getCategoryAgg(LongTerms aggregation) {
        try {
            List<Long> cids = aggregation.getBuckets().stream()
                    .map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            return categoryClient.queryCategoryListByIds(cids);
        } catch (Exception e) {
            log.error("获取分类聚合结果出错！ error message = [{}]", e.getMessage());
            return null;
        }
    }

    /**
     * 获取品牌聚合结果
     *
     * @param aggregation 聚合结果
     * @return 品牌集合
     */
    private List<Brand> getBrandAgg(LongTerms aggregation) {
        try {
            List<Long> bids = aggregation.getBuckets().stream()
                    .map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            return brandClient.queryBrandByIds(bids);
        } catch (Exception e) {
            log.error("获取品牌聚合结果出错！ error message = [{}]", e.getMessage());
            return null;
        }
    }
}
