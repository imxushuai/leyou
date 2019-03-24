package com.leyou.api;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /**
     * 查询指定ID的品牌
     *
     * @param ids 品牌ID集合
     * @return Brand
     */
    @GetMapping("brand/ids")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
