package com.leyou.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 规格对外接口
 */
public interface SpecificationApi {
    /**
     * 查询指定分类的规格数据
     *
     * @param categoryId 分类ID
     * @return Specification
     */
    @GetMapping("spec/{categoryId}")
    String querySpecificationByCategoryId(@PathVariable("categoryId") Long categoryId);
}
