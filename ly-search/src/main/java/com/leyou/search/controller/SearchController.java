/**
 * Copyright © 2019-2019 imxushuai
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
