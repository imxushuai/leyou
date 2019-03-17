package com.leyou.api;

import com.leyou.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 品牌对外接口
 */
public interface BrandApi {
    /**
     * 查询指定ID的品牌
     *
     * @param brandId 品牌ID
     * @return Brand
     */
    @GetMapping("brand/{brandId}")
    Brand queryBrandById(@PathVariable("brandId") long brandId);
}
