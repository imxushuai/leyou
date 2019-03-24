package com.leyou.search.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索模块 controller
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 分页查询goods集合
     *
     * @param request 搜索参数
     * @return goods分页结果
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> queryGoodsByPage(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.queryByPage(request));
    }

}
