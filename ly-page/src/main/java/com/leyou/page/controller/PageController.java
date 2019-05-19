package com.leyou.page.controller;

import com.leyou.page.service.PageService;
import com.leyou.page.util.PageServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 生成数据并返回到item视图
     */
    @GetMapping("item/{id}.html")
    public String spuPageInfo(@PathVariable("id") Long spuId, Model model) {
        // 获取数据
        Map<String, Object> attributes = pageService.loadModel(spuId);
        // 封装数据
        model.addAllAttributes(attributes);

        // 生成商品详情页
        pageService.asyncExcute(spuId);

        return PageServiceConstants.TEMPLATE_NAME_ITEM;
    }
}
