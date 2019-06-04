package com.leyou.page.service;

import com.alibaba.fastjson.JSON;
import com.leyou.BO.SpuBO;
import com.leyou.client.item.BrandClient;
import com.leyou.client.item.CategoryClient;
import com.leyou.client.item.GoodsClient;
import com.leyou.client.item.SpecificationClient;
import com.leyou.page.util.PageServiceConstants;
import com.leyou.page.util.ThreadUtils;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ly.page.destPath}")
    private String pagePath;

    /**
     * 加载商品详情页需要的数据
     *
     * @param spuId 商品ID
     * @return Map数据
     */
    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model = new HashMap<>();
        // 获取spu信息
        SpuBO spuBO = goodsClient.queryGoodsById(spuId);
        // 分类数据
        List<Category> categories = categoryClient.queryCategoryListByIds(Arrays.asList(spuBO.getCid1(), spuBO.getCid2(), spuBO.getCid3()));
        // 品牌数据
        Brand brand = brandClient.queryBrandById(spuBO.getBrandId());

        model.put(PageServiceConstants.MODEL_BRAND, brand);
        model.put(PageServiceConstants.MODEL_CATEGORIES, categories);
        model.put(PageServiceConstants.MODEL_DETAIL, spuBO.getSpuDetail());
        model.put(PageServiceConstants.MODEL_SKUS, spuBO.getSkuList());
        model.put(PageServiceConstants.MODEL_SPECS, JSON.parseObject(spuBO.getSpuDetail().getSpecTemplate()));
        model.put(PageServiceConstants.MODEL_SPU, spuBO);

        return model;
    }

    /**
     * 生成商品详情页并保存到指定目录
     *
     * @param spuId 商品ID
     */
    public void createHtml(Long spuId) {
        Context context = new Context();
        context.setVariables(loadModel(spuId));

        // 输出流
        File file = new File(pagePath + "/" + spuId + ".html");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            // 生成页面
            templateEngine.process(PageServiceConstants.TEMPLATE_NAME_ITEM, context, printWriter);
        } catch (FileNotFoundException e) {
            log.error("[生成商品详情页失败，商品ID = {}]", spuId, e);
        }
    }

    /**
     * 新建线程处理页面静态化
     *
     * @param spuId 商品ID
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(() -> createHtml(spuId));
    }

    /**
     * 删除商品详情页
     *
     * @param id 商品id
     */
    public void deleteHtml(Long id) {
        File file = new File(pagePath + "/" + id + ".html");
        if (file.exists()) {
            // 删除文件
            file.delete();
        }
    }

}
